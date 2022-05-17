package br.com.jamadeu.spring.web.flux.integration

import br.com.jamadeu.spring.web.flux.exception.handler.CustomAttributes
import br.com.jamadeu.spring.web.flux.model.Anime
import br.com.jamadeu.spring.web.flux.model.dto.CreateAnimeRequest
import br.com.jamadeu.spring.web.flux.model.dto.UpdateAnimeRequest
import br.com.jamadeu.spring.web.flux.repository.AnimeRepository
import br.com.jamadeu.spring.web.flux.service.AnimeService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.WebProperties.Resources
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.random.Random

@ExtendWith(SpringExtension::class)
@WebFluxTest
@Import(AnimeService::class, Resources::class, CustomAttributes::class)
internal class AnimeControllerIt {

    @MockBean
    lateinit var animeRepository: AnimeRepository

    @Autowired
    lateinit var webClient: WebTestClient

    @Test
    fun `listAll returns a flux of anime when successful`() {
        val anime = anime()
        `when`(animeRepository.findAll()).thenReturn(Flux.just(anime))

        webClient
            .get()
            .uri("/animes")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Anime::class.java)
            .hasSize(1)
            .contains(anime)
    }

    @Test
    fun `findById returns a mono with anime when it exists`() {
        val anime = anime()
        `when`(animeRepository.findById(anime.id)).thenReturn(Mono.just(anime))

        webClient
            .get()
            .uri("/animes/${anime.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody(Anime::class.java)
            .isEqualTo<BodySpec<Anime, *>>(anime)
    }

    @Test
    fun `findById returns a mono error when anime does not exists`() {
        `when`(animeRepository.findById(anyLong())).thenReturn(Mono.empty())

        webClient
            .get()
            .uri("/animes/${Random.nextLong()}")
            .exchange()
            .expectStatus().isNotFound
            .expectBody()
            .jsonPath("$.status").isEqualTo(404)
            .jsonPath("$.developerMessage").isEqualTo("A ResponseStatusException Happened")
    }

    @Test
    fun `save creates an anime when successful`() {
        val anime = anime()
        val request = createAnimeRequest(anime.name)
        `when`(animeRepository.save(any(Anime::class.java))).thenReturn(Mono.just(anime))

        webClient
            .post()
            .uri("/animes")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isCreated
            .expectBody(Anime::class.java)
            .isEqualTo<BodySpec<Anime, *>>(anime)
    }

    @Test
    fun `save returns bad request when name is`() {
        val request = createAnimeRequest(null)

        webClient
            .post()
            .uri("/animes")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.status").isEqualTo(400)
            .jsonPath("$.developerMessage").isEqualTo("A ResponseStatusException Happened")
    }

    @Test
    fun `delete removes the anime when successful`() {
        `when`(animeRepository.deleteById(anyLong())).thenReturn(Mono.empty())

        webClient
            .delete()
            .uri("/animes/${Random.nextLong()}")
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `update save updated anime and returns empty Mono when successful`() {
        val anime = anime()
        val request = updateAnimeRequest(anime.name)
        `when`(animeRepository.save(anime)).thenReturn(Mono.empty())
        `when`(animeRepository.findById(anime.id)).thenReturn(Mono.just(anime))

        webClient
            .put()
            .uri("/animes/${anime.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `update not found when anime does not exist`() {
        val request = updateAnimeRequest()
        `when`(animeRepository.findById(anyLong())).thenReturn(Mono.empty())

        webClient
            .put()
            .uri("/animes/${Random.nextLong()}")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isNotFound
            .expectBody()
            .jsonPath("$.status").isEqualTo(404)
            .jsonPath("$.developerMessage").isEqualTo("A ResponseStatusException Happened")
    }

    @Test
    fun `update bad request when anime name is null`() {
        val request = updateAnimeRequest(null)
        `when`(animeRepository.findById(anyLong())).thenReturn(Mono.empty())

        webClient
            .put()
            .uri("/animes/${Random.nextLong()}")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.status").isEqualTo(400)
            .jsonPath("$.developerMessage").isEqualTo("A ResponseStatusException Happened")
    }

    private fun anime(
        id: Long = 1L,
        name: String = "Bleach"
    ) = Anime(id, name)

    private fun createAnimeRequest(
        name: String? = "Bleach"
    ) = CreateAnimeRequest(name = name)

    private fun updateAnimeRequest(
        name: String? = "Bleach"
    ) = UpdateAnimeRequest(name = name)
}