package com.example.degica_project.network

import com.example.degica_project.config.ClientConfig
import com.example.degica_project.dto.LedgerDetail
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

// TODO: Put in config
@FeignClient(name = "ProxyNetwork", url = "https://take-home-test-api.herokuapp.com/", configuration = [ClientConfig::class])
interface ProxyNetwork {
    @RequestMapping(method = [RequestMethod.GET], value = ["/invoices"])
    fun getInvoices(): List<LedgerDetail>
}
