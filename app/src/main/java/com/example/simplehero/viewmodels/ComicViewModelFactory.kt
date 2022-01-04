package com.example.simplehero.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.simplehero.repositories.CharacterRepository

@Suppress("UNCHECKED_CAST")
class ComicViewModelFactory (
    private val characterRepository: CharacterRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (ComicViewModel(characterRepository) as T)
}
