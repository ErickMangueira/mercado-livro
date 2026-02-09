package com.mercadolivro.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("admin")
class AdiminController() {

    @GetMapping("/report")
    fun report(): String {
        return "This is a Report. Only admins can access this"
    }
}