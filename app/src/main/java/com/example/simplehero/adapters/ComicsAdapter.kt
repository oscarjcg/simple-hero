package com.example.simplehero.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplehero.databinding.ComicViewholderBinding
import com.example.simplehero.models.Comic
import com.example.simplehero.utils.IMAGE_VARIANT_STANDARD_LARGE
import com.example.simplehero.utils.ImageUtils

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

        val thumbnailUrl = ImageUtils.buildImageUrl(
            comic.thumbnail.path,
            IMAGE_VARIANT_STANDARD_LARGE,
            comic.thumbnail.extension)

        Glide
            .with(holder.binding.image)
            .load(thumbnailUrl)
            .into(holder.binding.image)
    }

    override fun getItemCount(): Int {
        return comics.size
    }

    fun setComics(comics: List<Comic>) {
        this.comics = comics
        notifyDataSetChanged()
    }
}
