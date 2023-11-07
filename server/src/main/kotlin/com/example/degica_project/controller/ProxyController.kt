package com.example.degica_project.controller

import com.example.degica_project.dto.Ledger
import com.example.degica_project.dto.LedgerDetail
import com.example.degica_project.service.CsvCreatorService
import com.example.degica_project.service.ProxyService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ProxyController(
    private val proxyService: ProxyService,
    private val csvCreatorService: CsvCreatorService
) {
    @GetMapping("/invoices")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun proxyInvoices(): Ledger {
        return proxyService.getLedger()
    }

    @GetMapping("/invoices/download")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun createCsv(): ResponseEntity<ByteArray> {
        val invoices = proxyService.getLedger()
        val tempFile = csvCreatorService.createCSV(LedgerDetail.HEADERS, invoices.ledgerDetail)

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_OCTET_STREAM
        headers.setContentDispositionFormData("attachment", "ledger_details.csv")

        return ResponseEntity<ByteArray>(tempFile, headers, HttpStatus.OK)
    }
}
