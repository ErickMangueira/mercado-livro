package com.mercadolivro.controller.request

import com.mercadolivro.validation.EmailAvailable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class PostCustomerRequest (

    @field:NotEmpty(message = "Nome inválido")
    var nome: String,

    @field:Email(message = "Email inválido")
    @EmailAvailable
    var email: String

)