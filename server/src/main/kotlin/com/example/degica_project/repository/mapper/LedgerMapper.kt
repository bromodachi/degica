package com.example.degica_project.repository.mapper

import com.example.degica_project.entity.LedgerEntity
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface LedgerMapper {

    fun createLedger(
        @Param("ledgerEntity") ledgerEntity: LedgerEntity
    ): Long
    fun findLedger(
        @Param("ledgerId") ledgerId: Long
    ): LedgerEntity?

    fun getLedgers(
        @Param("limit") limit: Int,
        @Param("lastId") lastLedgerId: Long
    ): List<LedgerEntity>
}
