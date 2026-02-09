package com.mercadolivro.security

import com.mercadolivro.repository.CustomerRepository
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class CustomerSecurity (
    private val customerRepository: CustomerRepository
) {
    fun canAccessCustomer(id: Int, email: String): Boolean {
       val customer = customerRepository.findById(id).orElse(null)
       return customer?.email == email
    }
    fun isAdmin(authentication: Authentication): Boolean {
        return authentication.authorities
            .any { it.authority == "ROLE_ADMIN" }
    }
}