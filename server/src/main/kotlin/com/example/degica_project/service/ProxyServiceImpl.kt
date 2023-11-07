package com.example.degica_project.service

import com.example.degica_project.dto.CURRENT_RATES
import com.example.degica_project.dto.Ledger
import com.example.degica_project.dto.LedgerDetail
import com.example.degica_project.dto.USD
import com.example.degica_project.network.ProxyNetwork
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class ProxyServiceImpl(private val network: ProxyNetwork) : ProxyService {

    private fun getBigDecimal(ledgerDetail: LedgerDetail, currency: String): BigDecimal {
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
        ledgerDetail: List<LedgerDetail>,
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

    override fun getLedger(): Ledger {
        val invoices = network.getInvoices().sortedByDescending { it.datetime }
        val totalAmount = calculateTotalAmount(invoices, USD)
        return Ledger(
            totalAmount = totalAmount.toDouble(),
            totalAmountInCurrency = USD,
            currencyRates = CURRENT_RATES,
            ledgerDetail = invoices
        )
    }
}
