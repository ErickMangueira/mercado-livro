package com.mercadolivro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import java.math.BigDecimal

data class PostBookRequest (
    var nome: String,

    var preco: BigDecimal,

    @JsonAlias("customer_id")
    var customerId: Int
)


