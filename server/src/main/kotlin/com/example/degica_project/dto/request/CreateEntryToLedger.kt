package com.example.degica_project.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class CreateEntryToLedger(
    val amount: Double,
    @field:NotBlank
    val currency: String,
    val isCredit: Boolean,
    val description: String? = null,
    @field:Min(0)
    val datetime: Long
)
