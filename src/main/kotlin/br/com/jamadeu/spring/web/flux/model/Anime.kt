package br.com.jamadeu.spring.web.flux.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import javax.validation.constraints.NotBlank

@Table("anime")
data class Anime(
        @Id
        val id: Long,
        @NotBlank(message = "The name of anime cannot be null or empty")
        val name: String
)