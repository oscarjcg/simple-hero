package com.example.simplehero.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplehero.models.comic.Comic
import com.example.simplehero.repositories.CharacterRepository
import com.example.simplehero.repositories.ComicRepository
import com.example.simplehero.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ComicViewModel
    @Inject
    constructor(private val comicRepository: ComicRepository,
                private val characterRepository: CharacterRepository) : ViewModel() {

    val comics = MutableLiveData<List<Comic>>()
    val comicSelected = MutableLiveData<Comic>()
    val showComicSelected = MutableLiveData(false)
    val loading = MutableLiveData(false)
    var offsetRequest = 0
    val uiState = MutableLiveData<Event<UIEvent<Nothing>>>()
    val showStateInfo = MutableLiveData(false)
    val stateInfo = MutableLiveData<String>()

    fun getComics(characterId: Int) {
        setLoading(true)
        setStateInfo(false)

        viewModelScope.launch {
            val opResult = characterRepository.getComics(characterId, offsetRequest, COMIC_REQUEST_LIMIT)
            when (opResult) {
                is OpResult.Success -> {
                    handleSuccess(opResult.data)
                }
                is OpResult.Error -> {
                    handleError(opResult.exception)
                }
            }

            setLoading(false)
        }
    }

    private fun handleSuccess(data: List<Comic>) {
        val newComics = ArrayList(data)
        if (comics.value.isNullOrEmpty())
            comics.value = newComics
        else
            newComics.addAll(0, comics.value!!)
        comics.value = newComics
        offsetRequest = comics.value!!.size
    }

    private fun handleError(exception: Exception) {
        exception.printStackTrace()
        uiState.value = Event(UIEvent.CheckInternet)

        comics.value.let {
            if (it.isNullOrEmpty()) {
                uiState.value = Event(UIEvent.NoResults)
            }
        }
    }

    fun getComic(comicId: Int) {
        setLoading(true)
        setStateInfo(false)

        viewModelScope.launch {
            val opResult = comicRepository.getComic(comicId)

            when (opResult) {
                is OpResult.Success -> {
                    comicSelected.value = opResult.data
                }
                is OpResult.Error -> {
                    handleComicError(opResult.exception)
                }
            }

            setLoading(false)
        }

    }

    private fun handleComicError(exception: Exception) {
        exception.printStackTrace()
        uiState.value = Event(UIEvent.CheckInternet)

        if (comicSelected.value == null || comicSelected.value!!.id == INVALID_COMIC_ID) {
            uiState.value = Event(UIEvent.NoResults)
        }
    }

    fun setLoading(loading: Boolean) {
        this.loading.value = loading
    }

    fun setStateInfo(show: Boolean, message: String = "") {
        showStateInfo.value = show
        stateInfo.value = message
    }

    fun setShowComicSelected(show: Boolean) {
        showComicSelected.value = show
    }
}
