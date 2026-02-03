package com.mercadolivro.events

import com.mercadolivro.model.PurchaseModel
import org.springframework.context.ApplicationEvent

class PurchaseEvent (
    val purchase: PurchaseModel,
    source: Any
) : ApplicationEvent(source)