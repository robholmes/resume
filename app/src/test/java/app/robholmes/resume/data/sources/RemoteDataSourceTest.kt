/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.data.sources

import app.robholmes.resume.data.api.ResumeApi
import app.robholmes.resume.data.model.Resume
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class RemoteDataSourceTest {

    @RelaxedMockK
    private lateinit var resumeApi: ResumeApi
    @RelaxedMockK
    private lateinit var resume: Resume

    @InjectMockKs
    private lateinit var dataSource: RemoteDataSource

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `get should return resume from api`() {
        coEvery { resumeApi.resume() } returns resume

        val result = runBlocking { dataSource.get() }

        assertNotNull(result)
        assertSame(resume, result)
        coVerify(exactly = 1) { resumeApi.resume() }
    }

    @Test
    fun `get should return null when error occurs`() {
        coEvery { resumeApi.resume() } throws mockk<HttpException>(relaxed = true)

        val result = runBlocking { dataSource.get() }

        assertNull(result)
        coVerify(exactly = 1) { resumeApi.resume() }
    }
}