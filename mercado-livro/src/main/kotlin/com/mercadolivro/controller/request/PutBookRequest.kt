package com.mercadolivro.controller.request

import java.math.BigDecimal

data class PutBookRequest (

    var nome: String?,
    var preco: BigDecimal?
)
