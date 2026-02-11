package com.mercadolivro.repository

import com.mercadolivro.helper.buildCustomer
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerRepositoryTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @BeforeEach
    fun setUp(){
        customerRepository.deleteAll()
    }

    @Test
    fun `should return name containing`(){
        val pageable: Pageable = PageRequest.of(0, 10)

        val marcos = customerRepository.save(buildCustomer(name = "Marcos"))
        val matheus = customerRepository.save(buildCustomer(name = "Matheus"))
        val alex = customerRepository.save(buildCustomer(name = "Alex"))

       val  customersSend = listOf(marcos, matheus, alex)

        val page = PageImpl(customersSend, pageable, customersSend.size.toLong())

        val customers = customerRepository.findByNomeContaining("Ma", pageable)

        assertEquals(listOf(marcos, matheus), customers.content)
    }
    @Nested
    inner class `exists by email`{
        @Test
        fun `should return true when email exists`(){
            val email = "email@test.com"
            customerRepository.save(buildCustomer(email = email))

            val exists = customerRepository.existsByEmail(email)

            assertTrue(exists)
        }
        @Test
        fun `should return false when email not exists`(){
            val email = "email@test.com"

            val exists = customerRepository.existsByEmail(email)

            assertFalse(exists)
        }

    }
    @Nested
    inner class `find by email`{
        @Test
        fun `should return customer when email exists`(){
            val email = "email@test.com"
            val customer = customerRepository.save(buildCustomer(email = email))

            val result = customerRepository.findByEmail(email)

            assertNotNull(result)
            assertEquals(customer, result)
        }
        @Test
        fun `should return null when email not exists`(){
            val email = "notExistEmail@test.com"

            val result = customerRepository.findByEmail(email)

            assertNull(result)
        }


    }
}