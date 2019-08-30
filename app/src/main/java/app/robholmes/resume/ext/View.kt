/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.ext

import app.robholmes.resume.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

/**
 * Shows a placeholder shape, for use while data is being fetched from a network for example.
 */
fun MaterialTextView.showPlaceholder(lines: Int = 1) {
    text = "\n".repeat(lines - 1).takeIf { it.isNotEmpty() } ?: " "
    setBackgroundResource(R.drawable.placeholder_rounded_rectangle_bg)
}

fun MaterialButton.showPlaceholder() {
    isEnabled = false
    text = " ".repeat(20)
}

/**
 * Removed placeholder shape
 */
fun MaterialTextView.removePlaceholder(block: MaterialTextView.() -> Unit) {
    background = null
    block()
}

fun MaterialButton.removePlaceholder(block: MaterialButton.() -> Unit) {
    isEnabled = true
    block()
}