package com.example.simplehero.repositories

import com.example.simplehero.database.dao.ComicDao
import com.example.simplehero.webservices.ComicWebService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class ComicRepositoryTest : BaseRepositoryTest() {
    @Mock
    private lateinit var comicWebService: ComicWebService
    @Mock
    private lateinit var comicDao: ComicDao
    @InjectMocks
    private lateinit var comicRepository: ComicRepository

    private val comicId = 1
    private val webServiceId: Int = 100
    private val cacheId: Int = 200

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun requestComic_withoutCache_getComicFromWebService() = runBlockingTest {
        val response = createFakeResponseComics(1, webServiceId)
        val cacheComic = null

        `when`(comicDao.getComicWithPrice(comicId)).thenReturn(cacheComic)
        `when`(comicWebService
            .getComic(eq(comicId), anyString(), anyString(), anyString()))
            .thenReturn(response)
        val comic = getOpResultData(comicRepository.getComic(comicId))

        verify(comicWebService)
            .getComic(eq(comicId), anyString(), anyString(), anyString())
        assertThat(comic).isEqualTo(response.data!!.results!![0])
    }


    @Test
    fun requestComic_withCache_getComicFromCache() = runBlockingTest {
        val response = createFakeResponseComics(1, webServiceId)
        val cacheComic = createFakeComicsWithPrices(1, cacheId)

        `when`(comicDao.getComicWithPrice(comicId)).thenReturn(cacheComic[0])
        `when`(comicWebService
            .getComic(eq(comicId), anyString(), anyString(), anyString()))
            .thenReturn(response)
        val comic = getOpResultData(comicRepository.getComic(comicId))

        verify(comicWebService, never())
            .getComic(eq(comicId), anyString(), anyString(), anyString())
        assertThat(comic).isEqualTo(buildComics(cacheComic)[0])
    }
}
