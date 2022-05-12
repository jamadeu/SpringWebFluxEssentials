package br.com.jamadeu.spring.web.flux.controller

import br.com.jamadeu.spring.web.flux.model.Anime
import br.com.jamadeu.spring.web.flux.service.AnimeService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("animes")
class AnimeController(
    val animeService: AnimeService
) {
    private val logger = LoggerFactory.getLogger(AnimeController::class.java)

    @GetMapping
    fun listAll(): Flux<Anime> {
        return animeService.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: Long): Mono<Anime> =
        animeService.findById(id)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found")))

}