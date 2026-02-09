package com.mercadolivro.controller

import com.mercadolivro.controller.request.LoginRequest
import com.mercadolivro.security.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/login")
class AuthController (
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
){

    @PostMapping
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Map<String, String>> {
        val authToken = UsernamePasswordAuthenticationToken(
            request.email,
            request.password
        )
        authenticationManager.authenticate(authToken)

        val token = jwtUtil.generateToken(request.email)

        return ResponseEntity.ok().header("Authorization", "Bearer $token").build()
    }
}