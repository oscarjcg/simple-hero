package com.example.simplehero.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.simplehero.repositories.ComicRepository

@Suppress("UNCHECKED_CAST")
class ComicDetailViewModelFactory (
    private val comicRepository: ComicRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (ComicDetailViewModel(comicRepository) as T)
}
