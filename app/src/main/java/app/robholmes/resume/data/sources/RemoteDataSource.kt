/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.data.sources

import app.robholmes.resume.data.DataSource
import app.robholmes.resume.data.api.ResumeApi
import app.robholmes.resume.data.model.Resume

class RemoteDataSource(
    private val api: ResumeApi
) : DataSource {

    override suspend fun get(): Resume? {
        return try {
            api.resume()
        } catch (e: Exception) {
            null
        }
    }
}