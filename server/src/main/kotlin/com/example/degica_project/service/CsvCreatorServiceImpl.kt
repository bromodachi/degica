package com.example.degica_project.service

import com.example.degica_project.dto.CsvFormat
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.springframework.stereotype.Service
import java.io.StringWriter

@Service
class CsvCreatorServiceImpl() : CsvCreatorService {

    override fun createCSV(
        headers: List<String>,
        data: List<CsvFormat>
    ): ByteArray {
        val stringWriter = StringWriter()
        val format = CSVFormat.DEFAULT
            .builder()
            .setHeader(*headers.toTypedArray())
            .build()
        CSVPrinter(stringWriter, format).use { printer ->
            data.forEach {
                printer.printRecord(it.getOrder())
            }
        }
        return stringWriter.toString().encodeToByteArray()
    }
}
