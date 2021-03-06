package br.com.jamadeu.spring.web.flux.model.dto

import br.com.jamadeu.spring.web.flux.model.Anime
import javax.validation.constraints.NotBlank

data class CreateAnimeRequest(
    @field:NotBlank(message = "The name of anime cannot be null or empty")
    val name: String?
) {
    fun toAnime(): Anime =
        Anime(name = name ?: throw RuntimeException("The name of anime cannot be null or empty"))
}
