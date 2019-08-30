/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.ui.summary

import android.content.res.Resources
import app.robholmes.resume.R

class SummaryResourceProvider(
    private val resources: Resources
) {

    fun retry(): String = resources.getString(R.string.retry)
    fun notFoundError(): String = resources.getString(R.string.not_found_error)
    fun httpError(): String = resources.getString(R.string.http_error)
    fun unknownError(): String = resources.getString(R.string.unknown_error)
}