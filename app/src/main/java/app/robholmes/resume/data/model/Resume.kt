/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Resume(
    val basics: Basics,
    val education: List<Education>,
    val references: List<References>,
    val skills: List<Skills>,
    val work: List<Work>,
    val volunteer: List<Volunteer>,
    val awards: List<Award>,
    val publications: List<Publication>,
    val languages: List<Language>,
    val interests: List<Interest>,
    val meta: Meta
)