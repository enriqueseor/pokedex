package net.atos.pokedex.ui.pokemon

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
import java.util.Objects

class PokemonAdapter(context: Context?) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    private val dataset: ArrayList<PokemonModel>

    init {
        Companion.context = context
        dataset = ArrayList()
    }

    fun addPokemon(pokemonList: ArrayList<PokemonModel>?) {
        dataset.addAll(pokemonList!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p: Any = dataset[position]
        val linkedTreeMap: LinkedTreeMap<*, *> = p as LinkedTreeMap<*, *>
        holder.tvPokemonName.text = Objects.requireNonNull(linkedTreeMap["name"]).toString()
        val strNumber = Objects.requireNonNull(linkedTreeMap["url"]).toString()
        val pokemonNumber = strNumber.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val finalStrNumber = pokemonNumber[6]
        println(finalStrNumber)
        Glide.with(context!!)
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$finalStrNumber.png")
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.ivPokemon)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardPokemon: CardView = itemView.findViewById(R.id.cardPokemon)
        val ivPokemon: ImageView = itemView.findViewById(R.id.ivPokemon)
        val tvPokemonName: TextView = itemView.findViewById(R.id.tvPokemonName)

        init {
            cardPokemon.setOnClickListener {
                val pokemonName = tvPokemonName.text.toString().uppercase(Locale.getDefault())
                val toastContext = itemView.context.applicationContext
                Toast.makeText(toastContext, pokemonName, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private var context: Context? = null
    }
}