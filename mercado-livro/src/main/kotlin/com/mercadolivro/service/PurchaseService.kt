package com.mercadolivro.service

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.model.PurchaseModel
import com.mercadolivro.repository.PurchaseRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
data class PurchaseService (
    private val purchaseRepository: PurchaseRepository,
    private val applicationEventPublisher: ApplicationEventPublisher

    ) {
    fun create(purchase: PurchaseModel) {
        purchaseRepository.save(purchase)


        applicationEventPublisher.publishEvent(PurchaseEvent(purchase, this))
    }
    fun update(purchase: PurchaseModel) {
            purchaseRepository.save(purchase)
        }

}

