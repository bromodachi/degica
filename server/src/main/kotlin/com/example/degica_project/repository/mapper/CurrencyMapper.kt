package com.example.degica_project.repository.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Component

@Mapper
@Component
interface CurrencyMapper {
    fun isValidCurrency(@Param("name") currency: String): Boolean

    fun getCurrencyId(@Param("name") currency: String): Long?
}
