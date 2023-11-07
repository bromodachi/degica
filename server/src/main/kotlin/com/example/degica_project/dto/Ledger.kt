package com.example.degica_project.dto

data class Ledger(
    val totalAmount: Double,
    val totalAmountInCurrency: String,
    val currencyRates: Map<String, Map<String, Double>> = CURRENT_RATES,
    val ledgerDetail: List<LedgerDetail>
)
const val USD = "USD"
const val WON = "KRW"
const val YEN = "JPY"

val CURRENT_RATES = mapOf(
    USD to mapOf(
        YEN to 149.37,
        WON to 1_309.02,
        USD to 1.0
    ),
    WON to mapOf(
        USD to 0.00076,
        YEN to 0.11,
        WON to 1.0
    ),
    YEN to mapOf(
        USD to 0.0067,
        WON to 8.76,
        YEN to 1.0
    )
)
