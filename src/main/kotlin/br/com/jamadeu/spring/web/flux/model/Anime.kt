package br.com.jamadeu.spring.web.flux.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import javax.annotation.Generated
import javax.validation.constraints.NotBlank

@Table("anime")
data class Anime(
    @Id
    var id: Long = 0,
    @NotBlank(message = "The name of anime cannot be null or empty")
    var name: String
) {

}