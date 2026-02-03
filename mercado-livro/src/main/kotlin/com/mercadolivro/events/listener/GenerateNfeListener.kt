package com.mercadolivro.events.listener

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.service.PurchaseService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class GenerateNfeListener (
    private val purchaseService: PurchaseService
){

    @EventListener
    fun listen(event: PurchaseEvent) {
        val nfe = UUID.randomUUID().toString()
        val purchase = event.purchase.copy(nfe = nfe)
        purchaseService.update(purchase)

    }
}