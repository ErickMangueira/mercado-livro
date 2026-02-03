package com.mercadolivro.controller.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class PutCustomerRequest (
    @field:NotEmpty(message = "Nome inválido")
    var nome: String,
    @field:Email(message = "Email inválido")
    var email: String

)