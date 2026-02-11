package com.mercadolivro.service


import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.helper.buildCustomer
import com.mercadolivro.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.Optional
import java.util.Random
import java.util.UUID


@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder

    @InjectMockKs
    @SpyK
    private lateinit var customerService: CustomerService

    @Test
    fun `should return all customers`() {
        val pageable: Pageable = PageRequest.of(0, 10)
        val fakeCustomers = listOf(buildCustomer(), buildCustomer())
        val page = PageImpl(fakeCustomers, pageable, fakeCustomers.size.toLong())

        every { customerRepository.findAll(pageable) } returns page
        val result = customerService.getAll(null, pageable)

        assertEquals(fakeCustomers, result.content)
        verify(exactly = 1) { customerRepository.findAll(pageable) }
        verify(exactly = 0) { customerRepository.findByNomeContaining(any(), any()) }
    }

    @Test
    fun `should return all customers when name is informed`() {
        val name = Math.random().toString()
        val pageable: Pageable = PageRequest.of(0, 10)
        val fakeCustomers = listOf(buildCustomer(), buildCustomer())
        val page = PageImpl(fakeCustomers, pageable, fakeCustomers.size.toLong())

        every { customerRepository.findByNomeContaining(name, pageable) } returns page
        val result = customerService.getAll(name, pageable)

        assertEquals(fakeCustomers, result.content)
        verify(exactly = 0) { customerRepository.findAll(pageable) }
        verify(exactly = 1) { customerRepository.findByNomeContaining(name, any()) }
    }
    @Test
    fun `should create a customer and encode password`() {
        val initialPassword = Math.random().toString()
        val fakeCustomer = buildCustomer(password = initialPassword)
        val fakePassword = UUID.randomUUID().toString()
        val fakeCustomerEncrpted = fakeCustomer.copy(password = fakePassword)

        every { customerRepository.save(fakeCustomerEncrpted) } returns fakeCustomer
        every { bCrypt.encode(initialPassword) } returns fakePassword

        customerService.create(fakeCustomer)

        verify(exactly = 1) { customerRepository.save(fakeCustomerEncrpted) }
        verify(exactly = 1) { bCrypt.encode(initialPassword) }

    }
    @Test
    fun `should return customer by id`(){
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.findById(id) } returns Optional.of(fakeCustomer)

        val customer = customerService.getById(id)

        assertEquals(fakeCustomer, customer)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should throw error when customer not found`() {
        val id = Random().nextInt()


        every { customerRepository.findById(id) } returns Optional.empty()

        val error = assertThrows<NotFoundException>{
            customerService.getById(id)
        }

        assertEquals("Customer [${id}] not exists", error.message)
        assertEquals("ML-1102", error.errorCode)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should update customer`() {
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns true
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer

        customerService.update(fakeCustomer)
        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 1) { customerRepository.save(fakeCustomer) }

    }

    @Test
    fun `should throw not found update customer`() {
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns false
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer

        val error = assertThrows<NotFoundException> {
            customerService.update(fakeCustomer)
        }

        assertEquals("Customer [${id}] not exists", error.message)
        assertEquals("ML-1102", error.errorCode)


        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 0) { customerRepository.save(any()) }

    }
    @Test
    fun `should delete customer`() {
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)
        val expectedCustomer = fakeCustomer.copy(status = CustomerStatus.INATIVO)

        every { customerRepository.findById(id) } returns Optional.of(fakeCustomer)
        every { bookService.deleteByCustomer(fakeCustomer) } just runs
        every { customerRepository.save(expectedCustomer) } returns expectedCustomer

        customerService.delete(id)

        verify(exactly = 1) { customerRepository.findById(id) }
        verify(exactly = 1) { bookService.deleteByCustomer(fakeCustomer) }
        verify(exactly = 1) { customerRepository.save(expectedCustomer) }

    }
    @Test
    fun `should throw not  found delete customer`() {
        val id = Random().nextInt()

        every { customerRepository.findById(id) } throws NotFoundException(Errors.ML1102.message.format(id), Errors.ML1102.code)

        val error = assertThrows<NotFoundException> {
            customerService.delete(id)
        }

        assertEquals("Customer [${id}] not exists", error.message)
        assertEquals("ML-1102", error.errorCode)

        verify(exactly = 1) { customerRepository.findById(id) }
        verify(exactly = 0) { bookService.deleteByCustomer(any()) }
        verify(exactly = 0) { customerRepository.save(any()) }

    }
    @Test
    fun `should return true when email available`() {
        val email = "${UUID.randomUUID()}@email.com"

        every { customerRepository.existsByEmail(email) } returns false

        val emailAvailable = customerService.emailAvailable(email)

        assertTrue(emailAvailable)

        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }
    @Test
    fun `should return false when email unavaible`() {
        val email = "${UUID.randomUUID()}@email.com"

        every { customerRepository.existsByEmail(email) } returns true

        val emailAvailable = customerService.emailAvailable(email)

        assertFalse(emailAvailable)

        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }

    }