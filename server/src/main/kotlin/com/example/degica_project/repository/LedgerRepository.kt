package com.example.degica_project.repository

import com.example.degica_project.entity.LedgerEntity
import com.example.degica_project.entity.LedgerEntryEntity
import com.example.degica_project.repository.mapper.LedgerEntriesMapper
import com.example.degica_project.repository.mapper.LedgerMapper
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class LedgerRepository(
    private val ledgerMapper: LedgerMapper,
    private val ledgerEntriesMapper: LedgerEntriesMapper
) {

    fun createLedger(ledgerEntity: LedgerEntity): LedgerEntity {
        ledgerMapper.createLedger(ledgerEntity)
        return ledgerEntity
    }
    fun getLedger(ledgerId: Long): LedgerEntity? {
        val result = ledgerMapper.findLedger(ledgerId)
        return result
    }

    fun getLedgers(limit: Int, lastIdSeen: Long? = null): List<LedgerEntity> = ledgerMapper.getLedgers(limit, lastIdSeen ?: Long.MAX_VALUE)

    fun findAllEntities(ledgerId: Long, lastIdSeen: Long = Long.MAX_VALUE, limit: Int) = ledgerEntriesMapper.findAllEntries(ledgerId, lastIdSeen, limit)

    fun addEntry(ledgerId: Long, createEntryToLedger: LedgerEntryEntity, currencyId: Long): Long {
        return ledgerEntriesMapper.addEntry(ledgerId, createEntryToLedger, currencyId, Instant.now().toEpochMilli())
    }
}
