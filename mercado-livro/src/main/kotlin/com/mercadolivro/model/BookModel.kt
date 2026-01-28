package com.mercadolivro.model

import com.mercadolivro.enums.BookStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal

@Entity(name = "book")
data class BookModel (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var nome: String,

    @Column
    var preco: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer : CustomerModel? = null

) {
    @Column
    @Enumerated(EnumType.STRING)
    var status: BookStatus? = null
        set(value) {
        if(field == BookStatus.CANCELADO || field == BookStatus.DELETADO) {
            throw Exception("Não é possível alterar o status de um livro cancelado ou deletado")
        }
        field = value
    }
}