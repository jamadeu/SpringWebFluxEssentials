package br.com.jamadeu.spring.web.flux

import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(WebProperties.Resources::class)
class SpringWebFluxApplicationTests {

	@Test
	fun contextLoads() {
	}

}
