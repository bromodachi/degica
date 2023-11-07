package com.example.degica_project.dto.request

import jakarta.validation.constraints.NotBlank

data class CreateLedgerDto(
    @field:NotBlank
    val ledgerName: String
)
