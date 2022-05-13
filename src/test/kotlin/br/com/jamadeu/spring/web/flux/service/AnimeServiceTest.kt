package br.com.jamadeu.spring.web.flux.service

import br.com.jamadeu.spring.web.flux.model.Anime
import br.com.jamadeu.spring.web.flux.repository.AnimeRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.anyLong
import org.mockito.BDDMockito.`when`
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import kotlin.random.Random

@ExtendWith(SpringExtension::class)
internal class AnimeServiceTest {

    @Mock
    lateinit var animeRepository:AnimeRepository

    @InjectMocks
    lateinit var animeService: AnimeService

    @Test
    fun `findAll returns a flux of anime when successful`(){
        `when`(animeRepository.findAll()).thenReturn(Flux.just(anime()))

        StepVerifier.create(animeService.findAll())
            .expectSubscription()
            .expectNext(anime())
            .verifyComplete()
    }

    @Test
    fun `findById returns a mono with anime when it exists`(){
        val anime = anime()
        `when`(animeRepository.findById(anime.id)).thenReturn(Mono.just(anime))

        StepVerifier.create(animeService.findById(anime.id))
            .expectSubscription()
            .expectNext(anime)
            .verifyComplete()
    }

    @Test
    fun `findById returns a mono error when anime does not exists`(){
        `when`(animeRepository.findById(anyLong())).thenReturn(Mono.empty())

        StepVerifier.create(animeService.findById(Random.nextLong()))
            .expectSubscription()
            .expectError(ResponseStatusException::class.java)
            .verify()
    }



    private fun anime(
        id: Long = 1L,
        name: String = "Bleach"
    ) = Anime(id, name)
}