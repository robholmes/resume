/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import app.robholmes.resume.R.id.navExperience
import app.robholmes.resume.R.id.navInterests
import app.robholmes.resume.R.id.navLeadership
import app.robholmes.resume.R.id.navSummary
import app.robholmes.resume.R.id.navTechnical
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav.setOnNavigationItemSelectedListener { navItemSelected(it.itemId) }
    }

    private fun navItemSelected(@IdRes itemId: Int): Boolean {
        when (itemId) {
            navSummary -> message.setText(R.string.title_summary)
            navTechnical -> message.setText(R.string.title_technical)
            navLeadership -> message.setText(R.string.title_leadership)
            navExperience -> message.setText(R.string.title_experience)
            navInterests -> message.setText(R.string.title_interests)
        }
        return true
    }
}