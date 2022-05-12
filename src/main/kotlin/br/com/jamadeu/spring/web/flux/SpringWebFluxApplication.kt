package br.com.jamadeu.spring.web.flux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SpringWebFluxApplication

fun main(args: Array<String>) {
	runApplication<SpringWebFluxApplication>(*args)
}
