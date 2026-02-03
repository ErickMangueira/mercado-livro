package com.mercadolivro

import com.mercadolivro.repository.BookRepository
import com.mercadolivro.repository.CustomerRepository
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(
	properties = [
		"spring.autoconfigure.exclude=" +
				"org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
				"org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
		"springdoc.api-docs.enabled=false",
		"springdoc.swagger-ui.enabled=false"
	]
)
class MercadoLivroApplicationTests {

	@MockBean
	lateinit var customerRepository: CustomerRepository

	@MockBean
	lateinit var bookRepository: BookRepository

	@Test
	fun contextLoads() {
	}
}

