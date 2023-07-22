package net.atos.pokedex.data.remote

import net.atos.pokedex.data.model.PokemonFetchResults
import retrofit2.Call
import retrofit2.http.GET

interface PokemonAPIService {
    @get:GET("pokemon?limit=151&offset=0")
    val pokemons: Call<PokemonFetchResults?>?
}