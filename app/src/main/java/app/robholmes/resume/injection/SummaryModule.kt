/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.injection

import app.robholmes.resume.ui.summary.SummaryDataMapper
import app.robholmes.resume.ui.summary.SummaryResourceProvider
import app.robholmes.resume.ui.summary.SummaryViewModel
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.dsl.module
import org.koin.experimental.builder.single

val summaryModule = module {

    single<SummaryDataMapper>()
    single<SummaryResourceProvider>()

    viewModel<SummaryViewModel>()
}