package com.mercadolivro.config

import com.mercadolivro.security.JwtUtil
import com.mercadolivro.service.UserDetailServiceImpl
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter (
    private val jwtUtil: JwtUtil,
    private val userDetailServiceImpl: UserDetailServiceImpl
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            val username = jwtUtil.getUserName(token)

            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetail = userDetailServiceImpl.loadUserByUsername(username)

                val auth = UsernamePasswordAuthenticationToken(
                    userDetail,
                    null,
                    userDetail?.authorities
                )
                auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = auth
            }
        }
        filterChain.doFilter(request, response)
    }
}