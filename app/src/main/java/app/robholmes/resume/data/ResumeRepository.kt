/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.data

import app.robholmes.resume.data.model.Resume
import app.robholmes.resume.utils.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext

class ResumeRepository(
    private val localDataSource: WritableDataSource,
    private val remoteDataSource: DataSource,
    private val dispatcherProvider: CoroutineDispatcherProvider
) {

    suspend fun resume(): Resume? = withContext(dispatcherProvider.IO) {
        localDataSource.get() ?: remoteDataSource.get()?.also { localDataSource.save(it) }
    }
}