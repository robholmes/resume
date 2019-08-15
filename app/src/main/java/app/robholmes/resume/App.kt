/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume

import android.app.Application
import app.robholmes.resume.injection.appModules
import app.robholmes.resume.utils.ReleaseTree
import io.paperdb.Paper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initKoin()
        initPaperDatabase()
    }

    private fun initTimber() = Timber.plant(createTree())

    private fun createTree(): Timber.Tree {
        if (BuildConfig.DEBUG) return ReleaseTree()

        return object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement) = String.format(
                "Class: %s, Line: %s, Method: %s",
                super.createStackElementTag(element),
                element.lineNumber,
                element.methodName
            )
        }
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(appModules())
        }
    }

    private fun initPaperDatabase() {
        Paper.init(this)
    }
}