package net.atos.pokedex.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.internal.LinkedTreeMap
import net.atos.pokedex.R
import net.atos.pokedex.data.model.PokemonModel
import java.util.Locale

class PokemonAdapter(context: Context?) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    private val dataset: ArrayList<PokemonModel> = ArrayList()

    init {
        Companion.context = context
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addPokemon(pokemonList: ArrayList<PokemonModel>?) {
        pokemonList?.let {
            dataset.addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = dataset.getOrNull(position) as? LinkedTreeMap<*, *>
        p?.run {
            holder.tvPokemonName.text = this["name"].toString()
            val number = this["url"].toString().split("/").getOrNull(6)
            number?.let { num ->
                Glide.with(context!!)
                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$num.png")
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivPokemon)
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardPokemon: CardView = itemView.findViewById(R.id.cardPokemon)
        val ivPokemon: ImageView = itemView.findViewById(R.id.ivPokemon)
        val tvPokemonName: TextView = itemView.findViewById(R.id.tvPokemonName)

        init {
            cardPokemon.setOnClickListener {
                val pokemonName = tvPokemonName.text.toString().uppercase(Locale.getDefault())
                Toast.makeText(itemView.context.applicationContext, pokemonName, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private var context: Context? = null
    }
}