package com.mercadolivro.enums

import org.aspectj.bridge.Message

enum class Errors(val code: String, val message: String ) {
    ML0001("ML-0001","Invalid Request"),
    ML1001("ML-1001","Book  [%S] not exists"),
    ML1002("ML-1002","Cannot update book with status [%s]"),
    ML1101("ML-1102", "Customer [%s] not exists")
}