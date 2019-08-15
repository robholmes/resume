/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.data

import app.robholmes.resume.data.model.Resume
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class ResumeRepositoryTest {

    @RelaxedMockK
    private lateinit var localDataSource: WritableDataSource
    @RelaxedMockK
    private lateinit var remoteDataSource: DataSource
    @MockK
    private lateinit var localResume: Resume
    @MockK
    private lateinit var remoteResume: Resume

    @InjectMockKs
    private lateinit var repo: ResumeRepository

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `resume should return local resume when available`() {
        coEvery { localDataSource.get() } returns localResume

        val result = runBlocking { repo.resume() }

        assertNotNull(result)
        assertSame(localResume, result)

        coVerify(exactly = 0) { remoteDataSource.get() }
    }

    @Test
    fun `resume should return remote resume and save locally, when local resume unavailable`() {
        coEvery { localDataSource.get() } returns null
        coEvery { remoteDataSource.get() } returns remoteResume

        val result = runBlocking { repo.resume() }

        assertNotNull(result)
        assertSame(remoteResume, result)

        coVerifySequence {
            localDataSource.get()
            remoteDataSource.get()
            localDataSource.save(remoteResume)
        }
    }

    @Test
    fun `resume should return null when local and remote resume unavailable`() {
        coEvery { localDataSource.get() } returns null
        coEvery { remoteDataSource.get() } returns null

        val result = runBlocking { repo.resume() }

        assertNull(result)

        coVerifySequence {
            localDataSource.get()
            remoteDataSource.get()
        }
    }
}