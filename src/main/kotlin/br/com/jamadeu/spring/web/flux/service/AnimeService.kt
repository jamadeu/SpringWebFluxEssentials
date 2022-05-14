package br.com.jamadeu.spring.web.flux.service

import br.com.jamadeu.spring.web.flux.model.Anime
import br.com.jamadeu.spring.web.flux.repository.AnimeRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class AnimeService(
    val animeRepository: AnimeRepository
) {
    fun findAll(): Flux<Anime> = animeRepository.findAll()

    fun findById(id: Long): Mono<Anime> =
        animeRepository.findById(id)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found")))

    fun save(anime: Anime): Mono<Anime> =
        animeRepository.save(anime)

    fun update(anime: Anime): Mono<Void> =
        findById(anime.id)
            .flatMap { animeRepository.save(anime) }
            .then()

    fun delete(id: Long): Mono<Void> = animeRepository.deleteById(id)

}