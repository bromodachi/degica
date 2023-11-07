package com.example.degica_project

import com.example.degica_project.dto.response.GetLedgerDetailResponseDto
import com.example.degica_project.dto.response.GetLedgersResponseDto
import com.example.degica_project.entity.LedgerEntity
import com.example.degica_project.entity.LedgerEntryEntity
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class IntegrationTest : IntegrationTestBase() {
    @Test
    fun getLedgers() {
        val response = getRequest(LEDGER_ENDPOINT, mapOf("limit" to 100))
        Assertions.assertNotNull(response)

        Assertions.assertEquals(200, response!!.statusCode.value())
        val data: GetLedgersResponseDto = TestUtil.instance.objectMapper.readValue(response.body!!)
        Assertions.assertTrue(data.ledgers.isNotEmpty())
        Assertions.assertTrue(data.ledgers.any { it.name == "my ledger" })
    }

    @Test
    fun createLedger() {
        val response = postRequest(LEDGER_ENDPOINT, mapOf("ledgerName" to "another"))
        Assertions.assertEquals(201, response!!.statusCode.value())
        val getResponse = getRequest(LEDGER_ENDPOINT, mapOf())

        Assertions.assertNotNull(getResponse)
        Assertions.assertEquals(200, getResponse!!.statusCode.value())
        val data: GetLedgersResponseDto = TestUtil.instance.objectMapper.readValue(getResponse.body!!)
        Assertions.assertTrue(data.ledgers.isNotEmpty())
        Assertions.assertTrue(data.ledgers.any { it.name == "another" })
    }

    @Test
    fun paginationTest() {
        for (i in (1..6)) {
            postRequest(LEDGER_ENDPOINT, mapOf("ledgerName" to "another_$i"))
        }
        var getResponse = getRequest(LEDGER_ENDPOINT, mapOf("limit" to 3))
        Assertions.assertNotNull(getResponse)
        Assertions.assertEquals(200, getResponse!!.statusCode.value())
        var data: GetLedgersResponseDto = TestUtil.instance.objectMapper.readValue(getResponse.body!!)
        Assertions.assertEquals(3, data.ledgers.size)
        // checking that the lats one exists
        val anotherThree = data.ledgers.first { it.name == "another_4" }
        Assertions.assertNotNull(anotherThree)

        getResponse = getRequest(LEDGER_ENDPOINT, mapOf("limit" to 3, "lastId" to anotherThree.id))
        Assertions.assertNotNull(getResponse)
        Assertions.assertEquals(200, getResponse!!.statusCode.value())
        data = TestUtil.instance.objectMapper.readValue(getResponse.body!!)
        Assertions.assertEquals("another_1", data.ledgers.last().name)
    }

    @Test
    fun getLedgerDetails() {
        val response = getLedgerDetails(1)
        Assertions.assertEquals(200, response.statusCode.value())
        var data: Map<String, Any> = TestUtil.instance.objectMapper.readValue(response.body!!)
        val dataAsGetLedgerDetailResponseDto: GetLedgerDetailResponseDto = TestUtil.instance.objectMapper.readValue(response.body!!)
        Assertions.assertTrue(data.containsKey("ledger"))
        Assertions.assertTrue(data.containsKey("ledgerEntries"))

        Assertions.assertEquals("my ledger", dataAsGetLedgerDetailResponseDto.ledger.name)
        Assertions.assertEquals(1, dataAsGetLedgerDetailResponseDto.ledger.id)
        Assertions.assertEquals(
            LedgerEntryEntity(
                id = 10,
                amount = 27.7,
                currency = "USD",
                isCredit = false,
                description = "U+1F32D",
                datetime = 1643628595000
            ),
            dataAsGetLedgerDetailResponseDto.ledgerEntries.first()
        )
    }

    @Test
    fun addLedgerToEntry() {
        // create the ledger
        val createdLedger = postRequest(LEDGER_ENDPOINT, mapOf("ledgerName" to "another"))
        Assertions.assertEquals(201, createdLedger!!.statusCode.value())
        var createdLedgerEntity: LedgerEntity = TestUtil.instance.objectMapper.readValue(createdLedger.body!!)
        // assure the entity was created correctly
        var getLedgerDetailResponse = getLedgerDetails(createdLedgerEntity.id)
        Assertions.assertEquals(200, getLedgerDetailResponse.statusCode.value())
        var data: Map<String, Any> = TestUtil.instance.objectMapper.readValue(getLedgerDetailResponse.body!!)
        var dataAsGetLedgerDetailResponseDto: GetLedgerDetailResponseDto = TestUtil.instance.objectMapper.readValue(getLedgerDetailResponse.body!!)
        // just making sure the values exists and has the correct data
        Assertions.assertTrue(data.containsKey("ledger"))
        Assertions.assertTrue(data.containsKey("ledgerEntries"))
        Assertions.assertEquals("another", dataAsGetLedgerDetailResponseDto.ledger.name)
        Assertions.assertEquals(createdLedgerEntity.id, dataAsGetLedgerDetailResponseDto.ledger.id)
        Assertions.assertTrue(dataAsGetLedgerDetailResponseDto.ledgerEntries.isEmpty())
        // create the entry
        val postResponse = postRequest(
            ledgerDetailsEndpoint(createdLedgerEntity.id),
            mapOf(
                "amount" to 40,
                "currency" to "JPY",
                "isCredit" to false,
                "datetime" to 16992710280000
            )
        )
        Assertions.assertEquals(201, postResponse!!.statusCode.value())

        // once again, just grabbing the data.
        getLedgerDetailResponse = getLedgerDetails(createdLedgerEntity.id)
        Assertions.assertEquals(200, getLedgerDetailResponse.statusCode.value())
        dataAsGetLedgerDetailResponseDto = TestUtil.instance.objectMapper.readValue(getLedgerDetailResponse.body!!)

        Assertions.assertEquals("another", dataAsGetLedgerDetailResponseDto.ledger.name)
        Assertions.assertEquals(2, dataAsGetLedgerDetailResponseDto.ledger.id)
        Assertions.assertTrue(
            dataAsGetLedgerDetailResponseDto.ledgerEntries.isNotEmpty()
        )

        // set the id as 0 as we don't know what id will be set.
        Assertions.assertEquals(
            LedgerEntryEntity(
                id = 0L,
                amount = 40.0,
                isCredit = false,
                description = null,
                datetime = 16992710280000,
                currency = "JPY"
            ),
            dataAsGetLedgerDetailResponseDto.ledgerEntries.first().copy(id = 0)
        )
    }

    companion object {
        @Container
        private val postgresContainer: PostgreSQLContainer<*> = PostgreSQLContainer(
            DockerImageName.parse("postgres:15-alpine")
        ).withInitScript("db/migration/V1__create_tables.sql")

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
            registry.add("spring.flyway.enabled", { true })
        }
    }
}
