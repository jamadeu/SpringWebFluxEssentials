package br.com.jamadeu.spring.web.flux.model.dto

import br.com.jamadeu.spring.web.flux.model.Anime
import javax.validation.constraints.NotBlank

data class UpdateAnimeRequest(
    @NotBlank(message = "The name of anime cannot be null or empty")
    val name: String
){
    fun toAnime(id: Long) = Anime(id, name)
}
