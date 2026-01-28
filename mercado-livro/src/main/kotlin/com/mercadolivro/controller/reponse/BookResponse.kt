package com.mercadolivro.controller.reponse

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.model.CustomerModel
import java.math.BigDecimal

data class BookResponse (

    var id: Int? = null,


    var nome: String,


    var preco: BigDecimal,


    var customer : CustomerModel? = null,

    var status: BookStatus? = null


)
