/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.injection

import app.robholmes.resume.data.DataSource
import app.robholmes.resume.data.ResumeRepository
import app.robholmes.resume.data.WritableDataSource
import app.robholmes.resume.data.sources.ExpiryProvider
import app.robholmes.resume.data.sources.FirebaseDataSource
import app.robholmes.resume.data.sources.PaperDataSource
import org.koin.dsl.module
import org.koin.experimental.builder.single
import org.koin.experimental.builder.singleBy

val dataModule = module {

    singleBy<DataSource, FirebaseDataSource>()
    singleBy<WritableDataSource, PaperDataSource>()

    single<ExpiryProvider>()
    single<ResumeRepository>()
}