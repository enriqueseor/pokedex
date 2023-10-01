package net.atos.pokedex.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PokemonAPIResponse {
    @SerializedName("results")
    @Expose
    val response: ArrayList<*>? = null
}