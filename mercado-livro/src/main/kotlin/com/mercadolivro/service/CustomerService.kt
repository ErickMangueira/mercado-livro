package com.mercadolivro.service

import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@Service
class CustomerService(
    val customerRepository: CustomerRepository,
    val bookService: BookService) {

    fun getAll(name: String?): List<CustomerModel> {
        name?.let {  return customerRepository.findByNomeContaining(it) }

        return customerRepository.findAll().toList()
    }

    fun create( customer : CustomerModel) {

        customerRepository.save(customer)
    }
    fun getById(id: Int): CustomerModel {

        return  customerRepository.findById(id).orElseThrow()
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
}
