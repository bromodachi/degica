package com.example.degica_project.entity

import com.example.degica_project.dto.CsvFormat
import com.example.degica_project.dto.request.CreateEntryToLedger
import com.fasterxml.jackson.annotation.JsonIgnore

data class LedgerEntryEntity(
    val id: Long,
    val amount: Double,
    val currency: String,
    val isCredit: Boolean,
    val description: String?,
    val datetime: Long
) : CsvFormat {
    @JsonIgnore
    override fun getOrder(): List<*> {
        return listOf(amount, currency, isCredit, description, datetime)
    }
    companion object {
        fun createFromCreateEntryToLedger(data: CreateEntryToLedger) = LedgerEntryEntity(
            // setting to id 0 but it will actually be set by mybatis
            id = 0,
            amount = data.amount,
            isCredit = data.isCredit,
            description = data.description,
            datetime = data.datetime,
            // not used
            currency = data.currency
        )
    }
}
