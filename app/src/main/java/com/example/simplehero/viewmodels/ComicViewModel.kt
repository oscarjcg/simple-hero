package com.example.simplehero.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplehero.models.Comic
import com.example.simplehero.repositories.ComicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComicViewModel
    @Inject
    constructor(private val comicRepository: ComicRepository) : ViewModel() {

    val comics = MutableLiveData<List<Comic>>()

    fun getComics(characterId: Int) {
        viewModelScope.launch {
            comics.value = comicRepository.getComics(characterId)
        }
    }
}
