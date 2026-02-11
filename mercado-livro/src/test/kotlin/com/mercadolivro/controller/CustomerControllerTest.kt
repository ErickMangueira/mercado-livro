package com.mercadolivro.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.helper.buildCustomer
import com.mercadolivro.repository.CustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(roles = ["ADMIN"])
class CustomerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should return all customer`() {
        val customer1 = customerRepository.save(buildCustomer())
        val customer2 = customerRepository.save(buildCustomer())

        mockMvc.perform(get("/customers"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].id").value(customer1.id))
            .andExpect(jsonPath("$.content[0].nome").value(customer1.nome))
            .andExpect(jsonPath("$.content[0].email").value(customer1.email))
            .andExpect(jsonPath("$.content[0].status").value(customer1.status.name))
            .andExpect(jsonPath("$.content[1].id").value(customer2.id))
            .andExpect(jsonPath("$.content[1].nome").value(customer2.nome))
            .andExpect(jsonPath("$.content[1].email").value(customer2.email))
            .andExpect(jsonPath("$.content[1].status").value(customer2.status.name))

    }
    @Test
    fun `should filter all customer by name when get all`() {
        val customer1 = customerRepository.save(buildCustomer(name = "Erick"))
        customerRepository.save(buildCustomer(name = "Martin"))

        mockMvc.perform(get("/customers?name=Eri"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(jsonPath("$.content[0].id").value(customer1.id))
            .andExpect(jsonPath("$.content[0].nome").value(customer1.nome))
            .andExpect(jsonPath("$.content[0].email").value(customer1.email))
            .andExpect(jsonPath("$.content[0].status").value(customer1.status.name))


    }
    @Test
    fun `should throw error when create customer has invalid information`(){
        val request = PostCustomerRequest("", "email@fakeemail.com","123456")
        mockMvc.perform(post("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.httpCode").value(422))
            .andExpect(jsonPath("$.message").value("Invalid Request"))
    }

    @Test
    fun `should create customer`() {
        val request = PostCustomerRequest("fake name", "email@fakeemail.com","123456")
        mockMvc.perform(post("/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated)

        val customer = customerRepository.findAll().toList()
        assertEquals(1, customer.size)
        assertEquals(request.nome, customer[0].nome)

    }
    @Test
    fun `should get user by id when user has same id`(){
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(get("/customers/${customer.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(customer.id))
            .andExpect(jsonPath("$.nome").value(customer.nome))
            .andExpect(jsonPath("$.email").value(customer.email))
            .andExpect(jsonPath("$.status").value(customer.status.name))
    }
    @Test
    fun `should return forbidden when user has diferrent id`(){
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(get("/customers/0"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.httpCode").value(404))
            .andExpect(jsonPath("$.message").value("Customer [0] not exists"))
          
    }
    @Test
    fun `should update customer`() {
        val customer = customerRepository.save(buildCustomer())
        val request = PutCustomerRequest("Erick", "emailupdate@email.com")

        mockMvc.perform(put("/customers/${customer.id}")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNoContent)

        val customers = customerRepository.findAll().toList()
        assertEquals(1, customers.size)
        assertEquals(request.nome, customers[0].nome)

    }
    @Test
    fun `should throw error when update customer has invalid information`(){
        val request = PutCustomerRequest("", "email@fakeemail.com")
        mockMvc.perform(put("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.httpCode").value(422))
            .andExpect(jsonPath("$.message").value("Invalid Request"))
    }

    @Test
    fun `should delete customer`(){
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(delete("/customers/${customer.id}"))
            .andExpect(status().isNoContent)

        val customerDeleted = customerRepository.findById(customer.id!!)
        assertEquals(CustomerStatus.INATIVO, customerDeleted.get().status)

    }
    @Test
    fun `should return not found when delete customer not exists`(){
        mockMvc.perform(delete("/customers/1"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.httpCode").value(404))
            .andExpect(jsonPath("$.message").value("Customer [1] not exists"))

    }
    @Test
    fun `should return not found when update customer not existing `() {
        val request = PutCustomerRequest("Erick", "emailupdate@email.com")

        mockMvc.perform(put("/customers/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.httpCode").value(404))
            .andExpect(jsonPath("$.message").value("Customer [1] not exists"))

    }


}
