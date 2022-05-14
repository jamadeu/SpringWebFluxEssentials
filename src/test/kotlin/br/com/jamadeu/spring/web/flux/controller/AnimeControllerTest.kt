package br.com.jamadeu.spring.web.flux.controller

import br.com.jamadeu.spring.web.flux.model.Anime
import br.com.jamadeu.spring.web.flux.model.dto.CreateAnimeRequest
import br.com.jamadeu.spring.web.flux.model.dto.UpdateAnimeRequest
import br.com.jamadeu.spring.web.flux.service.AnimeService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.`when`
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(SpringExtension::class)
internal class AnimeControllerTest {
    @Mock
    lateinit var animeService: AnimeService

    @InjectMocks
    lateinit var animeController: AnimeController

    @Test
    fun `findAll returns a flux of anime when successful`() {
        val anime = anime()
        `when`(animeService.findAll()).thenReturn(Flux.just(anime))

        StepVerifier.create(animeController.listAll())
            .expectSubscription()
            .expectNext(anime)
            .verifyComplete()
    }

    @Test
    fun `findById returns a mono with anime when it exists`() {
        val anime = anime()
        `when`(animeService.findById(anime.id)).thenReturn(Mono.just(anime))

        StepVerifier.create(animeController.findById(anime.id))
            .expectSubscription()
            .expectNext(anime)
            .verifyComplete()
    }

    @Test
    fun `save creates an anime when successful`() {
        val anime = anime()
        val request = createAnimeRequest()
        `when`(animeService.save(request.toAnime())).thenReturn(Mono.just(anime))

        StepVerifier.create(animeController.create(request))
            .expectSubscription()
            .expectNext(anime)
            .verifyComplete()
    }

    @Test
    fun `delete removes the anime when successful`() {
        val anime = anime()
        `when`(animeService.delete(anime.id)).thenReturn(Mono.empty())

        StepVerifier.create(animeController.delete(anime.id))
            .expectSubscription()
            .verifyComplete()
    }

    @Test
    fun `update save updated anime and returns empty Mono when successful`() {
        val anime = anime()
        val request = updateAnimeRequest()
        `when`(animeService.update(request.toAnime(anime.id))).thenReturn(Mono.empty())

        StepVerifier.create(animeController.update(anime.id, request))
            .expectSubscription()
            .verifyComplete()
    }

    private fun anime(
        id: Long = 1L,
        name: String = "Bleach"
    ) = Anime(id, name)

    private fun createAnimeRequest(
        name: String = "Bleach"
    ) = CreateAnimeRequest(name)

    private fun updateAnimeRequest(
        name: String = "Bleach"
    ) = UpdateAnimeRequest(name)
}