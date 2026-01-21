package com.mercadolivro.extencion

import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.model.CustomerModel

fun PostCustomerRequest.toCustomerModel(): CustomerModel {
    return CustomerModel(null, this.nome, this.email)
}
fun PutCustomerRequest.toCustomerModel(id: Int): CustomerModel {
    return CustomerModel(id, this.nome, this.email)
}