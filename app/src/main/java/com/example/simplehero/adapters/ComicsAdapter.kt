package com.example.simplehero.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplehero.databinding.ComicViewholderBinding
import com.example.simplehero.models.Comic

class ComicsAdapter(private var comics: List<Comic>) : RecyclerView.Adapter<ComicsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ComicViewholderBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= ComicViewholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comic = comics[position]

        holder.binding.title.text = comic.title
    }

    override fun getItemCount(): Int {
        return comics.size
    }

    fun setComics(comics: List<Comic>) {
        this.comics = comics
        notifyDataSetChanged()
    }
}
