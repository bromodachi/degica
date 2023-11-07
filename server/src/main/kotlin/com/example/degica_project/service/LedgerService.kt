package com.example.degica_project.service

import com.example.degica_project.dto.request.CreateEntryToLedger
import com.example.degica_project.dto.request.CreateLedgerDto
import com.example.degica_project.dto.response.GetLedgerDetailResponseDto
import com.example.degica_project.dto.response.GetLedgersResponseDto
import com.example.degica_project.entity.LedgerEntity
import com.example.degica_project.entity.LedgerEntryEntity

interface LedgerService {

    fun createLedger(ledger: CreateLedgerDto): LedgerEntity

    fun listLedgers(limit: Int, lastId: Long?): GetLedgersResponseDto

    fun getLedger(
        ledgerId: Long,
        limit: Int = 20,
        lastId: Long? = null
    ): GetLedgerDetailResponseDto

    fun addEntry(ledgerId: Long, createEntryToLedger: CreateEntryToLedger): LedgerEntryEntity

    fun allEntries(ledgerId: Long): List<LedgerEntryEntity>
}
