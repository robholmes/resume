/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.data.sources

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDateTime
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ExpiryProviderTest {

    @RelaxedMockK
    private lateinit var expiry: LocalDateTime
    @RelaxedMockK
    private lateinit var now: LocalDateTime
    @RelaxedMockK
    private lateinit var nowPlusSomeHours: LocalDateTime

    @InjectMockKs
    private lateinit var expiryProvider: ExpiryProvider

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(LocalDateTime::class)
    }

    @After
    fun tearDown() {
        unmockkStatic(LocalDateTime::class)
    }

    @Test
    fun `hasExpired should return false when expiry time is after now`() {
        every { LocalDateTime.now() } returns now
        every { LocalDateTime.parse(any()) } returns expiry
        every { expiry.isBefore(now) } returns false

        val result = expiryProvider.hasExpired("valid time")

        assertFalse(result)
        verify { expiry.isBefore(now) }
    }

    @Test
    fun `hasExpired should return true when expiry time is before now`() {
        every { LocalDateTime.now() } returns now
        every { LocalDateTime.parse(any()) } returns expiry
        every { expiry.isBefore(now) } returns true

        val result = expiryProvider.hasExpired("valid time")

        assertTrue(result)
    }

    @Test
    fun `hasExpired should return true when time string cannot be parsed`() {
        every { LocalDateTime.parse(any()) } throws mockk<Exception>()

        val result = expiryProvider.hasExpired("invalid time")

        assertTrue(result)
        verify(exactly = 1) { LocalDateTime.parse(any()) }
    }

    @Test
    fun `hasExpired should return true when time string is null`() {
        val result = expiryProvider.hasExpired(null)

        assertTrue(result)
    }

    @Test
    fun `newExpiry should return `() {
        every { LocalDateTime.now() } returns now
        every { now.plusHours(any()) } returns nowPlusSomeHours
        every { nowPlusSomeHours.format(any()) } returns "future time"

        val result = expiryProvider.newExpiry()

        assertSame("future time", result)
    }
}