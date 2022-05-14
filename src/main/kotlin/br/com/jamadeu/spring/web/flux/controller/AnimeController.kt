package br.com.jamadeu.spring.web.flux.controller

import br.com.jamadeu.spring.web.flux.model.Anime
import br.com.jamadeu.spring.web.flux.model.dto.CreateAnimeRequest
import br.com.jamadeu.spring.web.flux.model.dto.UpdateAnimeRequest
import br.com.jamadeu.spring.web.flux.service.AnimeService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateAnimeRequest): Mono<Anime> =
        animeService.save(request.toAnime())
            .log()

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable("id") id: Long, @Valid @RequestBody request: UpdateAnimeRequest): Mono<Void> =
        animeService.update(request.toAnime(id))

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("id") id: Long): Mono<Void> = animeService.delete(id)
}