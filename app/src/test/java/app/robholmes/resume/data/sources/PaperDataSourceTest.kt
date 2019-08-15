/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.data.sources

import app.robholmes.resume.data.sources.PaperDataSource.Companion.KEY_EXPIRY
import app.robholmes.resume.data.sources.PaperDataSource.Companion.KEY_RESUME
import app.robholmes.resume.data.model.Resume
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.unmockkStatic
import io.mockk.verifySequence
import io.paperdb.Book
import io.paperdb.Paper
import io.paperdb.PaperDbException
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class PaperDataSourceTest {

    @RelaxedMockK
    private lateinit var expiryProvider: ExpiryProvider
    @RelaxedMockK
    private lateinit var book: Book
    @MockK
    private lateinit var resume: Resume

    @InjectMockKs
    private lateinit var dataSource: PaperDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(Paper::class)

        every { Paper.book() } returns book
    }

    @After
    fun tearDown() {
        unmockkStatic(Paper::class)
    }

    @Test
    fun `get should return resume from database when not expired`() {
        every { expiryProvider.hasExpired(any()) } returns false
        every { book.read<String?>(KEY_EXPIRY, null) } returns null
        every { book.read<Resume?>(KEY_RESUME, null) } returns resume

        val result = runBlocking { dataSource.get() }

        assertNotNull(result)
        assertSame(resume, result)

        verifySequence {
            book.read(KEY_EXPIRY, null)
            book.read(KEY_RESUME, null)
        }
    }

    @Test
    fun `get should return null and delete entry from database when invalid`() {
        every { expiryProvider.hasExpired(any()) } returns false
        every { book.read<String?>(KEY_EXPIRY, null) } returns null
        every { book.read<Resume?>(KEY_RESUME, null) } throws mockk<PaperDbException>()
        every { book.delete(KEY_RESUME) } just runs

        val result = runBlocking { dataSource.get() }

        assertNull(result)

        verifySequence {
            book.read(KEY_EXPIRY, null)
            book.read(KEY_RESUME, null)
            book.delete(KEY_RESUME)
        }
    }

    @Test
    fun `get should return null and delete entry from database when expired`() {
        every { expiryProvider.hasExpired(any()) } returns true
        every { book.read<String?>(KEY_EXPIRY, null) } returns null
        every { book.delete(KEY_RESUME) } just runs

        val result = runBlocking { dataSource.get() }

        assertNull(result)

        verifySequence {
            book.read(KEY_EXPIRY, null)
            book.delete(KEY_RESUME)
        }
    }

    @Test
    fun `save should write expiry time and resume to database`() {
        every { expiryProvider.newExpiry() } returns "a future date"
        every { book.write<String>(KEY_EXPIRY, any()) } returns book
        every { book.write<Resume>(KEY_RESUME, any()) } returns book

        runBlocking { dataSource.save(resume) }

        verifySequence {
            book.write(KEY_EXPIRY, "a future date")
            book.write(KEY_RESUME, resume)
        }
    }
}