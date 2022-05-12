package br.com.jamadeu.spring.web.flux.service

import br.com.jamadeu.spring.web.flux.model.Anime
import br.com.jamadeu.spring.web.flux.repository.AnimeRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class AnimeService(
    val animeRepository: AnimeRepository
) {
    fun findAll(): Flux<Anime> {
        return animeRepository.findAll()
    }


}