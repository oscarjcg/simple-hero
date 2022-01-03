package com.example.simplehero.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplehero.models.comic.Comic
import com.example.simplehero.repositories.ComicRepository
import com.example.simplehero.utils.Event
import com.example.simplehero.utils.INVALID_COMIC_ID
import com.example.simplehero.utils.OpResult
import com.example.simplehero.utils.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ComicDetailViewModel
    @Inject
    constructor(private val comicRepository: ComicRepository) : BaseViewModel() {

    val comicSelected = MutableLiveData<Comic>()
    val showComicSelected = MutableLiveData(false)

    fun getComic(comicId: Int) {
        setLoading(true)
        setStateInfo(false)

        viewModelScope.launch {
            val opResult = comicRepository.getComic(comicId)

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

    private fun handleSuccess(data: Comic) {
        comicSelected.value = data
    }

    /**
     * Check errors and communicate them to UI.
     */
    private fun handleError(exception: Exception) {
        exception.printStackTrace()
        uiState.value = Event(UIEvent.CheckInternet)

        if (comicSelected.value == null || comicSelected.value!!.id == INVALID_COMIC_ID) {
            uiState.value = Event(UIEvent.NoResults)
        }
    }

    fun setShowComicSelected(show: Boolean) {
        showComicSelected.value = show
    }
}
