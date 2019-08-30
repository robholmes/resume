/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.ui.summary

import app.robholmes.resume.data.model.Location
import app.robholmes.resume.data.model.Resume
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class SummaryDataMapperTest {

    @RelaxedMockK
    private lateinit var resume: Resume
    @RelaxedMockK
    private lateinit var location: Location

    @InjectMockKs
    private lateinit var dataMapper: SummaryDataMapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { resume.basics.picture } returns "http://test-url/image.jpg"
        every { resume.basics.name } returns "Test Name"
        every { resume.basics.label } returns "Awesome Developer"
        every { resume.basics.website } returns "http://example.com"
        every { resume.basics.summary } returns "Test summary goes here"
        every { resume.basics.location } returns location
        every { location.city } returns "City"
        every { location.region } returns "County"
        every { location.countryCode } returns "GB"
    }

    @Test
    fun `map should return summary data from resume object`() {
        val result = dataMapper.map(resume)

        assertEquals("http://test-url/image.jpg", result.pictureUrl)
        assertEquals("Test Name", result.name)
        assertEquals("Awesome Developer", result.label)
        assertEquals("http://example.com", result.website)
        assertEquals("Test summary goes here", result.summary)
        assertEquals("City, County, GB", result.location)
    }
}