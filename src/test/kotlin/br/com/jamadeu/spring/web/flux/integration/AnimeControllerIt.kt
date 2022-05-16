package br.com.jamadeu.spring.web.flux.integration

import br.com.jamadeu.spring.web.flux.exception.handler.ExceptionHandler
import br.com.jamadeu.spring.web.flux.model.Anime
import br.com.jamadeu.spring.web.flux.repository.AnimeRepository
import br.com.jamadeu.spring.web.flux.service.AnimeService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.WebProperties.Resources
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux

@ExtendWith(SpringExtension::class)
@WebFluxTest
@Import(AnimeService::class, Resources::class)
internal class AnimeControllerIt {

    @MockBean
    lateinit var animeRepository: AnimeRepository

    @Autowired
    lateinit var webClient: WebTestClient

    @Autowired
    lateinit var exceptionHandler: ExceptionHandler

    @Test
    fun `listAll returns a flux of anime when successful`() {
        val anime = anime()
        BDDMockito.`when`(animeRepository.findAll()).thenReturn(Flux.just(anime))

        webClient
            .get()
            .uri("/animes")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody()
            .jsonPath("$.[0].id").isEqualTo(anime.id)
            .jsonPath("$.[0].name").isEqualTo(anime.name)
    }

    private fun anime(
        name: String = "Bleach"
    ) = Anime(name = name)
}