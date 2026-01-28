package com.mercadolivro.extencion

import com.mercadolivro.controller.reponse.BookResponse
import com.mercadolivro.controller.reponse.CustomerResponse
import com.mercadolivro.controller.request.PostBookRequest
import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel

fun PostCustomerRequest.toCustomerModel(): CustomerModel {
    return CustomerModel(null, this.nome, this.email, CustomerStatus.ATIVO)
}
fun PutCustomerRequest.toCustomerModel(previousValue: CustomerModel): CustomerModel {
    return CustomerModel(id = previousValue.id, this.nome, this.email, previousValue.status)
}

fun PostBookRequest.toBookModel(customer: CustomerModel) : BookModel {
    val book = BookModel(
        nome = this.nome,
        preco = this.preco,
        customer = customer

    )
    book.status = BookStatus.ATIVO

    return book
}
fun PutBookRequest.toBookModel(previousValue: BookModel): BookModel {
    val book = BookModel(
        id = previousValue.id,
        nome = this.nome ?: previousValue.nome,
        preco = this.preco ?: previousValue.preco,
        customer = previousValue.customer
    )

    book.status = previousValue.status

    return book
}
fun CustomerModel.toResponse(): CustomerResponse {
    return CustomerResponse (
        id = this.id,
        nome = this.nome,
        email = this.email,
        status = this.status
    )
}

fun BookModel.toResponse(): BookResponse{
    return BookResponse(
        id = this.id,
        nome = this.nome,
        preco = this.preco,
        customer = this.customer,
        status = this.status
    )
}


