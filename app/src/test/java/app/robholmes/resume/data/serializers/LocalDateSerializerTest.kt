/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.data.serializers

import app.robholmes.resume.data.model.Education
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration.Companion.Stable
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class LocalDateSerializerTest {

    @RelaxedMockK
    private lateinit var decoder: Decoder
    @RelaxedMockK
    private lateinit var encoder: Encoder
    @RelaxedMockK
    private lateinit var localDate: LocalDate

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(LocalDate::class)
    }

    @After
    fun tearDown() {
        unmockkStatic(LocalDate::class)
    }

    @Test
    fun `serialize should encode local date object into date string`() {
        every { localDate.format(any()) } returns "date string"

        LocalDateSerializer.serialize(encoder, localDate)

        verifySequence {
            localDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
            encoder.encodeString("date string")
        }
    }

    @Test
    fun `deserialize should return date string from a local date object`() {
        every { decoder.decodeString() } returns "date string"
        every { LocalDate.parse(any(), any()) } returns localDate

        val result = LocalDateSerializer.deserialize(decoder)

        assertEquals(localDate, result)
        verify { LocalDate.parse("date string", DateTimeFormatter.ISO_LOCAL_DATE) }
    }

    @Test
    fun `deserialize should throw a serialization exception `() {
        every { decoder.decodeString() } returns "invalid date"
        every { LocalDate.parse(any(), any()) } throws mockk<DateTimeParseException>(relaxed = true)

        assertFailsWith(SerializationException::class) {
            LocalDateSerializer.deserialize(decoder)
        }
    }

    @Test
    fun `stringify object with local date`() {
        every { localDate.format(any()) } returns "yyyy-mm-dd"

        val education = Education(
            endDate = localDate,
            startDate = localDate,
            area = "test area",
            studyType = "test study type",
            institution = "test institution"
        )

        val result = Json(Stable).stringify(Education.serializer(), education)

        assertEquals(
            """{"endDate":"yyyy-mm-dd","startDate":"yyyy-mm-dd","area":"test area","studyType":"test study type","institution":"test institution"}""",
            result
        )
    }

    @Test
    fun `parse string with local date`() {
        every { localDate.format(any()) } returns "yyyy-mm-dd"
        every { LocalDate.parse(any(), any()) } returns localDate

        val json =
            """{"endDate":"yyyy-mm-dd","startDate":"yyyy-mm-dd","area":"test area","studyType":"test study type","institution":"test institution"}"""

        val result = Json(Stable).parse(Education.serializer(), json)

        assertNotNull(result.endDate)
        assertSame(localDate, result.endDate)
        assertNotNull(result.startDate)
        assertSame(localDate, result.startDate)
        assertEquals("test area", result.area)
        assertEquals("test study type", result.studyType)
        assertEquals("test institution", result.institution)
    }
}