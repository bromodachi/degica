package com.example.degica_project.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class LedgerDetail(
    val amount: Double,
    val currency: String,
    @JsonProperty("is_credit")
    val isCredit: Boolean,
    val description: String? = "",
    // TODO: Transform to UTC.
    @JsonProperty("created_at")
    // 2022-01-31 14:29:55 +0900
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z")
    val datetime: Date
) : CsvFormat {
    @JsonIgnore
    override fun getOrder(): List<*> {
        return listOf(amount, currency, isCredit, description, datetime)
    }

    companion object {
        val HEADERS = listOf(
            "amount",
            "currency",
            "is_credit",
            "description",
            "datetime"
        )
    }
}
