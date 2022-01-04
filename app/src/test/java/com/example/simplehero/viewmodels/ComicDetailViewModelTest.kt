package com.example.simplehero.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.simplehero.MainCoroutineRule
import com.example.simplehero.getOrAwaitValue
import com.example.simplehero.models.comic.Comic
import com.example.simplehero.repositories.ComicRepository
import com.example.simplehero.utils.OpResult
import com.example.simplehero.utils.UIEvent
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Exception

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ComicDetailViewModelTest {

    @Mock
    private lateinit var comicRepository: ComicRepository

    private lateinit var comicDetailViewModel: ComicDetailViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this)
        comicDetailViewModel =
            ComicDetailViewModelFactory(comicRepository).create(ComicDetailViewModel::class.java)
    }

    @Test
    fun requestComic_onSuccess_setComicSelected() = runBlockingTest {
        val comicId = 1
        val comic = Comic(comicId)

        `when`(comicRepository.getComic(comicId)).thenReturn(OpResult.Success(comic))
        comicDetailViewModel.getComic(comicId)

        assertThat(comicDetailViewModel.comicSelected.value).isEqualTo(comic)
        assertThat(comicDetailViewModel.showComicSelected.value).isEqualTo(true)
        assertThat(comicDetailViewModel.showStateInfo.value).isEqualTo(false)
    }

    @Test
    fun requestComic_onError_eventNoResults() = runBlockingTest {
        val comicId = 1

        `when`(comicRepository.getComic(comicId)).thenReturn(OpResult.Error(Exception()))
        comicDetailViewModel.getComic(comicId)

        val uiState = comicDetailViewModel.uiState.getOrAwaitValue()
        assertThat(comicDetailViewModel.comicSelected.value).isNull()
        assertThat(comicDetailViewModel.showComicSelected.value).isEqualTo(false)
        assertThat(uiState.getContentIfNotHandled()).isEqualTo(UIEvent.NoResults)
    }

}
