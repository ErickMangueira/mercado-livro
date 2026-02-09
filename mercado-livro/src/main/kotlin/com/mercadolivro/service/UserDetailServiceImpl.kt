package com.mercadolivro.service

import com.mercadolivro.repository.CustomerRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
data class UserDetailServiceImpl(
    private  val customerRepository: CustomerRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails? {
        val customer = customerRepository.findByEmail(username) ?: throw UsernameNotFoundException("Usuário não encontrado")


        return User(
            customer.email,
            customer.password,
            customer.roles.map { SimpleGrantedAuthority(it.description)
            }
        )
    }
}
