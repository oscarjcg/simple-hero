package com.example.simplehero.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.simplehero.MainCoroutineRule
import com.example.simplehero.getOrAwaitValue
import com.example.simplehero.models.comic.Comic
import com.example.simplehero.repositories.CharacterRepository
import com.example.simplehero.utils.OpResult
import com.example.simplehero.utils.UIEvent
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ComicViewModelTest {

    @Mock
    private lateinit var characterRepository: CharacterRepository

    private lateinit var comicViewModel: ComicViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val characterId = 1
    private val comicId = 2
    private val offset = 0
    private val limit = 20

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this)
        comicViewModel =
            ComicViewModelFactory(characterRepository).create(ComicViewModel::class.java)
    }

    @Test
    fun requestComics_onSuccess_setComics() = runBlockingTest {
        val comics = createFakeComics(limit, comicId)

        `when`(characterRepository.getComics(characterId, offset, limit)).thenReturn(OpResult.Success(comics))
        comicViewModel.getComics(characterId)

        assertThat(comicViewModel.comics.value).isEqualTo(comics)
        assertThat(comicViewModel.offsetRequest).isEqualTo(comics.size)
        assertThat(comicViewModel.showStateInfo.value).isEqualTo(false)
    }

    @Test
    fun requestComic_onError_eventNoResults() = runBlockingTest {

        `when`(characterRepository.getComics(characterId, offset, limit))
            .thenReturn(OpResult.Error(Exception()))
        comicViewModel.getComics(characterId)

        val uiState = comicViewModel.uiState.getOrAwaitValue()
        assertThat(comicViewModel.comics.value).isNull()
        assertThat(uiState.getContentIfNotHandled()).isEqualTo(UIEvent.NoResults)
    }

    private fun createFakeComics(n: Int, comicId: Int): ArrayList<Comic> {
        val comics = ArrayList<Comic>()
        val createdAt = Date()
        for (i in 1..n) {
            val comic = Comic(comicId + i)
            comic.createdAt = createdAt
            comics.add(comic)
        }
        return comics
    }
}
