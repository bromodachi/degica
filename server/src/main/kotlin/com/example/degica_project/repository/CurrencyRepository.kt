package com.example.degica_project.repository

import com.example.degica_project.repository.mapper.CurrencyMapper
import org.springframework.stereotype.Repository

@Repository
class CurrencyRepository(
    private val currencyMapper: CurrencyMapper
) {

    fun getCurrencyId(currency: String) = currencyMapper.getCurrencyId(currency)
}
