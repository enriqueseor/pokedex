package net.atos.pokedex.ui.pokemon

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.atos.pokedex.R
import net.atos.pokedex.data.model.PokemonModel
import net.atos.pokedex.data.remote.PokemonAPIService
import net.atos.pokedex.data.remote.PokemonResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private var retrofit: Retrofit? = null
    private var pokemonAdapterList: PokemonAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<View>(R.id.recyclerViewPokemon) as RecyclerView
        pokemonAdapterList = PokemonAdapter(this.baseContext)
        recyclerView.adapter = pokemonAdapterList
        recyclerView.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        getData()
    }

    private fun getData() {
        val service = retrofit!!.create(PokemonAPIService::class.java)
        val pokemonFetchResultsCall = service.pokemons
        pokemonFetchResultsCall?.enqueue(object : Callback<PokemonResponse?> {
            override fun onResponse(
                call: Call<PokemonResponse?>,
                response: Response<PokemonResponse?>
            ) {
                if (response.isSuccessful) {
                    val pokemonFetchResults = response.body()!!
                    val pokemonList = pokemonFetchResults.results
                    for (i in pokemonList?.indices!!) {
                        pokemonList[i]
                    }
                    pokemonAdapterList!!.addPokemon(pokemonList as ArrayList<PokemonModel>?)
                }
            }
            override fun onFailure(call: Call<PokemonResponse?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}