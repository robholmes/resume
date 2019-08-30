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

class SummaryDataMapper {

    fun map(resume: Resume) = SummaryData(
        pictureUrl = resume.basics.picture,
        name = resume.basics.name,
        label = resume.basics.label,
        website = resume.basics.website,
        summary = resume.basics.summary,
        location = location(resume.basics.location)
    )

    private fun location(location: Location) = listOf(
        location.city,
        location.region,
        location.countryCode
    ).joinToString(separator = ", ")
}