package com.mercadolivro.controller.reponse


data class ErrorResponse(
    var httpCode: Int,
    var message: String,
    var internalCodes: String,
    var errors : List<FieldErrorResponse>?
)
