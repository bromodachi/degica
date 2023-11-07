package com.example.degica_project.repository.mapper

import com.example.degica_project.entity.LedgerEntryEntity
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Component

@Mapper
@Component
interface LedgerEntriesMapper {
    fun findAllEntries(
        @Param("ledgerId") ledgerId: Long,
        @Param("lastIdSeen") lastIdSeen: Long,
        @Param("limit") size: Int
    ): List<LedgerEntryEntity>

    fun addEntry(
        @Param("ledgerId") ledgerId: Long,
        @Param("ledgerToCreate") createEntryToLedger: LedgerEntryEntity,
        @Param("currencyId") currencyId: Long,
        // TODO: Would be better if the DB creates it.
        @Param("currTime") currTime: Long
    ): Long
}
