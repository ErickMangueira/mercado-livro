package com.mercadolivro.exception

data class AuthenticationException(override  val message: String, val errorCode: String) : Exception()
