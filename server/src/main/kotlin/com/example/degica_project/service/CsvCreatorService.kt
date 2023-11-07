package com.example.degica_project.service

import com.example.degica_project.dto.CsvFormat

interface CsvCreatorService {
    fun createCSV(
        headers: List<String>,
        data: List<CsvFormat>
    ): ByteArray
}
