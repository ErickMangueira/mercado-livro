package com.mercadolivro.controller.reponse

import com.mercadolivro.enums.CustomerStatus

data class CustomerResponse(
    var id: Int? = null,

    var nome: String,


    var email: String,


    var status: CustomerStatus
)




