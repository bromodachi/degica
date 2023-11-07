package com.example.degica_project.service

import com.example.degica_project.dto.CURRENT_RATES
import com.example.degica_project.dto.USD
import com.example.degica_project.dto.request.CreateEntryToLedger
import com.example.degica_project.dto.request.CreateLedgerDto
import com.example.degica_project.dto.response.GetLedgerDetailResponseDto
import com.example.degica_project.dto.response.GetLedgersResponseDto
import com.example.degica_project.dto.response.LedgerDetails
import com.example.degica_project.entity.LedgerEntity
import com.example.degica_project.entity.LedgerEntryEntity
import com.example.degica_project.exceptions.BadRequestException
import com.example.degica_project.exceptions.NotFoundException
import com.example.degica_project.repository.CurrencyRepository
import com.example.degica_project.repository.LedgerRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class LedgerServiceImpl(
    private val ledgerRepository: LedgerRepository,
    private val currencyRepository: CurrencyRepository
) : LedgerService {
    override fun createLedger(ledger: CreateLedgerDto): LedgerEntity {
        return ledgerRepository.createLedger(LedgerEntity.create(ledger.ledgerName))
    }

    override fun listLedgers(limit: Int, lastId: Long?): GetLedgersResponseDto {
        val ledgers = ledgerRepository.getLedgers(limit + 1, lastId ?: Long.MAX_VALUE)
        val hasMore = ledgers.size > limit
        return GetLedgersResponseDto(if (hasMore) ledgers.dropLast(1) else ledgers, hasMore)
    }

    // Repeating ourselves but I don't have time to refactor

    private fun getBigDecimal(ledgerDetail: LedgerEntryEntity, currency: String): BigDecimal {
        val rate = requireNotNull(CURRENT_RATES[ledgerDetail.currency]?.get(currency))
        return BigDecimal.valueOf(ledgerDetail.amount).multiply(BigDecimal.valueOf(rate)).multiply(
            BigDecimal.valueOf(
                if (ledgerDetail.isCredit) {
                    -1
                } else {
                    1
                }
            )
        )
    }

    private fun calculateTotalAmount(
        ledgerDetail: List<LedgerEntryEntity>,
        currency: String
    ): BigDecimal {
        if (ledgerDetail.isEmpty()) {
            return BigDecimal.ZERO
        }

        var mutableBigDecimal = getBigDecimal(ledgerDetail.first(), currency)

        for (next in ledgerDetail.drop(1)) {
            mutableBigDecimal = mutableBigDecimal.plus(getBigDecimal(next, currency))
        }
        return mutableBigDecimal.setScale(2, RoundingMode.HALF_UP)
    }
    override fun getLedger(
        ledgerId: Long,
        limit: Int,
        lastId: Long?
    ): GetLedgerDetailResponseDto {
        val ledger = ledgerRepository.getLedger(ledgerId) ?: throw NotFoundException(message = "Not found $ledgerId")
        val entries = ledgerRepository.findAllEntities(ledgerId, lastId ?: Long.MAX_VALUE, limit + 1)
        // TODO: Do not fetch all the data. It would be better to precalculate this every time we add a new entry.
        // TODO: Use count to find how many entities we have. But again, don't have time to edit this.
        val allEntities = allEntries(ledgerId)
        val hasMore = entries.size > limit
        val amount = calculateTotalAmount(allEntities, USD)
        return GetLedgerDetailResponseDto(
            ledger =
            LedgerDetails(
                id = ledger.id,
                name = ledger.name,
                totalAmount = amount.toDouble(),
                totalAmountInCurrency = USD
            ),
            if (entries.size > limit) entries.dropLast(1) else entries, hasMore, allEntities.size
        )
    }

    override fun addEntry(ledgerId: Long, createEntryToLedger: CreateEntryToLedger): LedgerEntryEntity {
        val currencyId = currencyRepository.getCurrencyId(createEntryToLedger.currency.uppercase()) ?: throw BadRequestException(message = "Invalid Currency! ${createEntryToLedger.currency}")
        val toCreate = LedgerEntryEntity.createFromCreateEntryToLedger(createEntryToLedger)
        ledgerRepository.addEntry(ledgerId, toCreate, currencyId)
        return toCreate
    }

    override fun allEntries(ledgerId: Long): List<LedgerEntryEntity> = ledgerRepository.findAllEntities(ledgerId, Long.MAX_VALUE, Int.MAX_VALUE)
}
