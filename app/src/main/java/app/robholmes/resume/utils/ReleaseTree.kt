/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.utils

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

class ReleaseTree : Timber.Tree() {

    private val loggablePriorities = listOf(Log.ERROR, Log.WARN)

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (priority !in loggablePriorities) return

        if (throwable != null) {
            Crashlytics.logException(throwable)
            return
        }

        Crashlytics.log(priority, tag, message)
    }
}