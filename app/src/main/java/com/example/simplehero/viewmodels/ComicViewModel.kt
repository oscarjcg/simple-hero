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
    constructor(private val characterRepository: CharacterRepository) : BaseViewModel() {

    val comics = MutableLiveData<List<Comic>>()
    var offsetRequest = 0

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

}
