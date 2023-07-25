package net.atos.pokedex.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.atos.pokedex.R
import net.atos.pokedex.adapter.PokemonAdapter
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
    private var pokemonAdapter: PokemonAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        setupRetrofit()
        getData()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<View>(R.id.recyclerViewPokemon) as RecyclerView
        pokemonAdapter = PokemonAdapter(this.baseContext)
        recyclerView.adapter = pokemonAdapter
        recyclerView.setHasFixedSize(true)

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
    }

    private fun setupRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getData() {
        val service = retrofit!!.create(PokemonAPIService::class.java)
        val pokemonResponse = service.pokemons
        pokemonResponse?.enqueue(object : Callback<PokemonResponse?> {
            override fun onResponse(call: Call<PokemonResponse?>, response: Response<PokemonResponse?>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    responseBody.let {
                        val pokemonList = it.response
                        pokemonAdapter?.addPokemon(pokemonList as ArrayList<PokemonModel>?)
                    }
                }
            }
            override fun onFailure(call: Call<PokemonResponse?>, t: Throwable) {
                Log.e("MainActivity", "Network request failed: ${t.message}")
                Toast.makeText(this@MainActivity, "Network request failed.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}