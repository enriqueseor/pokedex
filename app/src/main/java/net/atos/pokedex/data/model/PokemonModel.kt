package net.atos.pokedex.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PokemonModel {
    @SerializedName("name")
    @Expose
    var name: String? = null
}