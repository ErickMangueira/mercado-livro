package com.mercadolivro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class PostBookRequest (

    @field:NotEmpty(message = "Nome inválido")
    var nome: String,

    @field:NotNull(message = "Preço inválido")
    var preco: BigDecimal,

    @JsonAlias("customer_id")
    var customerId: Int
)


