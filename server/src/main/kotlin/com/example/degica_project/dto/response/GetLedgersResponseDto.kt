package com.example.degica_project.dto.response

import com.example.degica_project.entity.LedgerEntity

data class GetLedgersResponseDto(
    val ledgers: List<LedgerEntity>,
    val hasMore: Boolean
)
