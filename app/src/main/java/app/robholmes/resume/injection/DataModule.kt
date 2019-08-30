/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.injection

import app.robholmes.resume.BuildConfig
import app.robholmes.resume.data.DataSource
import app.robholmes.resume.data.MessagePublisher
import app.robholmes.resume.data.ResumeRepository
import app.robholmes.resume.data.WritableDataSource
import app.robholmes.resume.data.api.ResumeApi
import app.robholmes.resume.data.sources.ExpiryProvider
import app.robholmes.resume.data.sources.PaperDataSource
import app.robholmes.resume.data.sources.RemoteDataSource
import app.robholmes.resume.ext.create
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.dsl.module
import org.koin.experimental.builder.single
import org.koin.experimental.builder.singleBy
import retrofit2.Retrofit
import timber.log.Timber

val dataModule = module {

    singleBy<DataSource, RemoteDataSource>()
    singleBy<WritableDataSource, PaperDataSource>()

    single<ExpiryProvider>()
    single<MessagePublisher>()
    single<ResumeRepository>()

    single<ResumeApi> { get<Retrofit>().create() }

    single<Retrofit> {
        Retrofit.Builder()
            .client(get())
            .baseUrl("https://robholmes-resume.web.app/")
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .build()
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        // create logger using Timber
        HttpLoggingInterceptor { message -> Timber.d(message) }.apply {
            // only log in debug builds
            level = if (BuildConfig.DEBUG) Level.BASIC else Level.NONE
        }
    }
}