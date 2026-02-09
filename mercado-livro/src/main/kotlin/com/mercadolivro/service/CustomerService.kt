package com.mercadolivro.service

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.enums.Profile
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomerService(
   private val customerRepository: CustomerRepository,
   private val bookService: BookService,
   private val bCrypt: BCryptPasswordEncoder) {

    fun getAll(name: String?, pageable: Pageable): Page<CustomerModel> {
        name?.let {  return customerRepository.findByNomeContaining(it, pageable) }

        return customerRepository.findAll(pageable)
    }

    fun create( customer : CustomerModel) {
         val customercopy = customer.copy(
            roles = setOf(Profile.CUSTOMER),
            password = bCrypt.encode(customer.password)
        )
        customerRepository.save(customercopy)
    }
    fun getById(id: Int): CustomerModel {

        return  customerRepository.findById(id).orElseThrow{ NotFoundException(Errors.ML1101.message.format(id), Errors.ML1101.code) }
    }
    fun update( customer: CustomerModel) {
        if(!customerRepository.existsById(customer.id!!)){
            throw Exception()
        }
        customerRepository.save(customer)
    }
    fun delete(id: Int) {

        val customer = getById(id)

        bookService.deleteByCustomer(customer)

        customer.status = CustomerStatus.INATIVO

        customerRepository.save(customer)
    }

    fun emailAvailable(email: String): Boolean {
        return !customerRepository.existsByEmail(email)
    }
}
