package com.example.degica_project.dto.response

import com.example.degica_project.dto.CURRENT_RATES
import com.example.degica_project.entity.LedgerEntryEntity

data class GetLedgerDetailResponseDto(
    val ledger: LedgerDetails,
    val ledgerEntries: List<LedgerEntryEntity>,
    val hasMore: Boolean,
    val totalEntries: Int
)

data class LedgerDetails(
    val id: Long,
    val name: String,
    val totalAmount: Double,
    val totalAmountInCurrency: String,
    val currencyRates: Map<String, Map<String, Double>> = CURRENT_RATES
)
