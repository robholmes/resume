/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

@file:UseSerializers(LocalDateSerializer::class)

package app.robholmes.resume.data.model

import app.robholmes.resume.data.serializers.LocalDateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.threeten.bp.LocalDate

@Serializable
data class Work(
    val company: String,
    val position: String,
    val location: String? = null,
    val website: String? = null,
    val startDate: LocalDate?,
    val endDate: LocalDate? = null,
    val summary: String,
    val highlights: List<String>
)