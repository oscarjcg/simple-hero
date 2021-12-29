package com.example.simplehero.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplehero.models.Comic
import com.example.simplehero.repositories.CharacterRepository
import com.example.simplehero.repositories.ComicRepository
import com.example.simplehero.utils.COMIC_REQUEST_LIMIT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComicViewModel
    @Inject
    constructor(private val comicRepository: ComicRepository,
                private val characterRepository: CharacterRepository) : ViewModel() {

    val comics = MutableLiveData<List<Comic>>()
    val comicSelected = MutableLiveData<Comic>()
    val loading = MutableLiveData(false)
    var offsetRequest = 0

    fun getComics(characterId: Int) {
        setLoading(true)

        viewModelScope.launch {
            val newComics = ArrayList(characterRepository.getComics(characterId, offsetRequest, COMIC_REQUEST_LIMIT))

            if (comics.value.isNullOrEmpty())
                comics.value = newComics
            else
                newComics.addAll(0, comics.value!!)
            comics.value = newComics
            offsetRequest = comics.value!!.size

            setLoading(false)
        }
    }

    fun getComic(comicId: Int) {
        setLoading(true)

        viewModelScope.launch {
            comicSelected.value = comicRepository.getComic(comicId)

            setLoading(false)
        }

    }

    fun setLoading(loading: Boolean) {
        this.loading.value = loading
    }
}
