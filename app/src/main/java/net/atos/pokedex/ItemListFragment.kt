package net.atos.pokedex

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.DragShadowBuilder
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import net.atos.pokedex.api.PokemonFetchResults
import net.atos.pokedex.databinding.FragmentItemListBinding
import net.atos.pokedex.databinding.ItemListContentBinding
import net.atos.pokedex.placeholder.PlaceholderContent.ITEMS
import net.atos.pokedex.placeholder.PlaceholderContent.PlaceholderItem
import net.atos.pokedex.service.PokemonAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItemListFragment : Fragment() {

    private var binding: FragmentItemListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .build()

        val pokemonApiService = retrofit.create(PokemonAPIService::class.java)

        val call = pokemonApiService.pokemons
        call?.enqueue(object : Callback<PokemonFetchResults?> {
            override fun onResponse(
                call: Call<PokemonFetchResults?>,
                response: Response<PokemonFetchResults?>
            ) {
                if (response.isSuccessful) {
                    assert(response.body() != null)
                    response.body()!!.results
                } else {
                    Log.d("Error:", "Couldn't get list")
                }
            }
            override fun onFailure(call: Call<PokemonFetchResults?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        onClickListener: View.OnClickListener,
        onContextClickListener: View.OnContextClickListener
    ) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(
            ITEMS,
            onClickListener,
            onContextClickListener
        )
    }

    class SimpleItemRecyclerViewAdapter internal constructor(
        private val mValues: List<PlaceholderItem>,
        private val mOnClickListener: View.OnClickListener,
        private val mOnContextClickListener: View.OnContextClickListener
    ) : RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mIdView.text = mValues[position].id
            holder.mContentView.text = mValues[position].content
            holder.itemView.tag = mValues[position]
            holder.itemView.setOnClickListener(mOnClickListener)
            holder.itemView.setOnContextClickListener(mOnContextClickListener)
            holder.itemView.setOnLongClickListener { v: View ->
                val clipItem = ClipData.Item(mValues[position].id)
                val dragData = ClipData(
                    (v.tag as PlaceholderItem).content,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    clipItem
                )
                v.startDragAndDrop(
                    dragData,
                    DragShadowBuilder(v),
                    null,
                    0
                )
                true
            }
        }

        override fun getItemCount(): Int {
            return mValues.size
        }

        class ViewHolder(binding: ItemListContentBinding) : RecyclerView.ViewHolder(binding.root) {
            val mIdView: TextView
            val mContentView: TextView

            init {
                mIdView = binding.idText
                mContentView = binding.content
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}