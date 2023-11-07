package com.example.degica_project.controller

import com.example.degica_project.dto.LedgerDetail
import com.example.degica_project.dto.USD
import com.example.degica_project.dto.WON
import com.example.degica_project.dto.YEN
import com.example.degica_project.dto.request.CreateEntryToLedger
import com.example.degica_project.dto.request.CreateLedgerDto
import com.example.degica_project.dto.response.CreateLedgerEntryDto
import com.example.degica_project.dto.response.GetLedgerDetailResponseDto
import com.example.degica_project.dto.response.GetLedgersResponseDto
import com.example.degica_project.entity.LedgerEntity
import com.example.degica_project.exceptions.BadRequestException
import com.example.degica_project.service.CsvCreatorService
import com.example.degica_project.service.LedgerService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class LedgerController(
    private val csvCreatorService: CsvCreatorService,
    private val ledgerService: LedgerService
) {
    @PostMapping("/ledger")
    @CrossOrigin(origins = ["http://localhost:3000"])
    @ResponseStatus(HttpStatus.CREATED)
    fun createLedger(
        @RequestBody @Validated
        createLedgerDto: CreateLedgerDto
    ): LedgerEntity {
        return ledgerService.createLedger(createLedgerDto)
    }

    @GetMapping("/ledger")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun getLedgers(
        @RequestParam(value = "lastId", required = false) lastId: Long? = null,
        @RequestParam(value = "limit", required = false, defaultValue = "5") limit: Int
    ): GetLedgersResponseDto {
        return ledgerService.listLedgers(limit = limit, lastId = lastId)
    }

    @GetMapping("/ledger/{id}/details")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun getLedgerDetails(
        @PathVariable("id") ledgerId: Long,
        @RequestParam(value = "lastId", required = false) lastId: Long? = null,
        @RequestParam(value = "limit", required = false, defaultValue = "5") limit: Int
    ): GetLedgerDetailResponseDto {
        return ledgerService.getLedger(ledgerId, lastId = lastId, limit = limit)
    }

    @PostMapping("/ledger/{id}/details")
    @CrossOrigin(origins = ["http://localhost:3000"])
    @ResponseStatus(HttpStatus.CREATED)
    fun addEntryToLedger(
        @PathVariable("id") ledgerId: Long,
        @RequestBody @Validated
        createEntryToLedger: CreateEntryToLedger
    ): CreateLedgerEntryDto {
        if (createEntryToLedger.amount < 0 || createEntryToLedger.amount > Int.MAX_VALUE) {
            throw BadRequestException(message = "Amount must be between 0 and ${Int.MAX_VALUE}")
        }
        if (createEntryToLedger.currency.uppercase() !in VALID_CURRENCIES) {
            throw BadRequestException(message = "We only support $VALID_CURRENCIES currently. Sorry")
        }
        if (createEntryToLedger.currency.uppercase() in NON_DECIMAL && createEntryToLedger.amount.mod(1.0) != 0.0) {
            throw BadRequestException(message = "${createEntryToLedger.currency} was given but this currency doesn't support decimals!")
        }
        return CreateLedgerEntryDto(ledgerService.addEntry(ledgerId, createEntryToLedger))
    }

    @GetMapping("/ledger/{id}/details/download")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun createCsv(
        @PathVariable("id") ledgerId: Long
    ): ResponseEntity<ByteArray> {
        val entries = ledgerService.allEntries(ledgerId)
        val tempFile = csvCreatorService.createCSV(LedgerDetail.HEADERS, entries)

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_OCTET_STREAM
        headers.setContentDispositionFormData("attachment", "ledger_details_for_id_$ledgerId.csv")

        return ResponseEntity<ByteArray>(tempFile, headers, HttpStatus.OK)
    }

    companion object {
        // TODO: Would be better to check with the DB.
        // However, time constraint.
        private val VALID_CURRENCIES = setOf(USD, YEN, WON)
        private val NON_DECIMAL = setOf(YEN, WON)
    }
}
