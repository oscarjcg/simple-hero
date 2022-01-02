package com.example.simplehero.repositories

import com.example.simplehero.database.dao.ComicDao
import com.example.simplehero.models.ComicWithPrices
import com.example.simplehero.webservices.CharacterWebService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CharacterRepositoryTest : BaseRepositoryTest() {

    @Mock
    private lateinit var characterWebService: CharacterWebService
    @Mock
    private lateinit var comicDao: ComicDao
    @InjectMocks
    private lateinit var characterRepository: CharacterRepository

    private val nComics = 20
    private val characterId = 1
    private val webServiceId: Int = 100
    private val cacheId: Int = 200
    private val limit: Int = 20

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun request20Comics_withoutCache_get20ComicsFromWebService() = runBlockingTest {
        val offset = 0
        val response = createFakeResponseComics(nComics, webServiceId)
        val cacheComics = ArrayList<ComicWithPrices>()

        `when`(comicDao.getComicsWithPrices(characterId, limit, offset)).thenReturn(cacheComics)
        `when`(characterWebService
            .getComics(eq(characterId), anyString(), anyString(), anyString(), eq(offset), eq(limit)))
            .thenReturn(response)
        val comics = getOpResultData(characterRepository.getComics(characterId, offset, limit))

        verify(characterWebService)
            .getComics(eq(characterId), anyString(), anyString(), anyString(), eq(offset), eq(limit))
        assertThat(comics).isEqualTo(response.data!!.results!!)
        assertThat(comics.size).isEqualTo(response.data!!.results!!.size)
    }

    @Test
    fun request20Comics_withCache_get20ComicsFromCache() = runBlockingTest {
        val offset = 0
        val response = createFakeResponseComics(nComics, webServiceId)
        val cacheComics = createFakeComicsWithPrices(nComics, cacheId)

        `when`(comicDao.getComicsWithPrices(characterId, limit, offset)).thenReturn(cacheComics)
        `when`(characterWebService
            .getComics(eq(characterId), anyString(), anyString(), anyString(), eq(offset), eq(limit)))
            .thenReturn(response)
        val comics = getOpResultData(characterRepository.getComics(characterId, offset, limit))

        verify(characterWebService, never())
            .getComics(eq(characterId), anyString(), anyString(), anyString(), eq(offset), eq(limit))
        assertThat(comics).isEqualTo(buildComics(cacheComics))
        assertThat(comics.size).isEqualTo(cacheComics.size)
    }

}
