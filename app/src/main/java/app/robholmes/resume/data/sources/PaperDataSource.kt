/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.data.sources

import app.robholmes.resume.data.WritableDataSource
import app.robholmes.resume.data.model.Resume
import io.paperdb.Paper

class PaperDataSource(
    private val expiryProvider: ExpiryProvider
) : WritableDataSource {

    override suspend fun get(): Resume? {
        if (hasExpired()) {
            // resume has expired, clear up after ourselves
            deleteResume()
            return null
        }

        return try {
            getResume()
        } catch (e: Exception) {
            // local resume is invalid, so remove it for good
            deleteResume()
            null
        }
    }

    override suspend fun save(resume: Resume) {
        Paper.book()
            .write(KEY_EXPIRY, expiryProvider.newExpiry())
            .write(KEY_RESUME, resume)
    }

    private fun hasExpired(): Boolean = expiryProvider.hasExpired(getExpiry())

    private fun getExpiry(): String? = Paper.book().read(KEY_EXPIRY, null)

    private fun getResume(): Resume? = Paper.book().read(KEY_RESUME, null)

    private fun deleteResume() = Paper.book().delete(KEY_RESUME)

    companion object {
        const val KEY_RESUME = "resume"
        const val KEY_EXPIRY = "expiry"
    }
}