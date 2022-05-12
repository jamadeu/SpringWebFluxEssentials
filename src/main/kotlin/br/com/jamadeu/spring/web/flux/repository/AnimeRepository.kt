package br.com.jamadeu.spring.web.flux.repository

import br.com.jamadeu.spring.web.flux.model.Anime
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AnimeRepository : ReactiveCrudRepository<Anime, Long> {
}