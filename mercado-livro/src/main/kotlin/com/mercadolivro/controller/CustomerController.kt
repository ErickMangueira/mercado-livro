package com.mercadolivro.controller

import com.mercadolivro.controller.reponse.CustomerResponse
import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.extencion.toCustomerModel
import com.mercadolivro.extencion.toResponse
import com.mercadolivro.service.CustomerService
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("customers")
class CustomerController(
     val customerService: CustomerService) {


    @GetMapping
    @PreAuthorize("@customerSecurity.isAdmin(authentication)")
    fun getAll(
        @RequestParam(required = false) name: String?,

        @ParameterObject
        @PageableDefault(page = 0, size = 10)
        @SortDefault(sort = ["id"], direction = Sort.Direction.ASC)
        @Parameter(hidden = true)
        pageable: Pageable
    ): Page<CustomerResponse> {
        return customerService.getAll(name, pageable).map { it.toResponse() }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid customer : PostCustomerRequest) {
        customerService.create(customer.toCustomerModel())
    }

    @GetMapping("/{id}")
    @PreAuthorize("@customerSecurity.isAdmin(authentication) || " +
        "@customerSecurity.canAccessCustomer(#id, authentication.name)")
    fun getCustomer(@PathVariable id: Int): CustomerResponse {
       return customerService.getById(id).toResponse()
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Int, @RequestBody @Valid customerRequest: PutCustomerRequest) {
           val customerSaved = customerService.getById(id)
           customerService.update(customerRequest.toCustomerModel(customerSaved))
        }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Int, @RequestBody customerRequest: PutCustomerRequest) {
        customerService.delete(id)
    }

}