/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.data.sources

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class ExpiryProvider {

    fun hasExpired(dateTimeString: String?): Boolean {
        val expiry = dateTimeString?.let {
            try {
                LocalDateTime.parse(it)
            } catch (e: Exception) {
                null
            }
        }

        return expiry?.isBefore(LocalDateTime.now()) != false
    }

    fun newExpiry(): String = LocalDateTime.now()
        .plusHours(3)
        .format(isoDateTime)

    private companion object {
        private val isoDateTime: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }
}
