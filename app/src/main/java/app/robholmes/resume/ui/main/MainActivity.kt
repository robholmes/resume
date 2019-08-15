/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import app.robholmes.resume.R
import app.robholmes.resume.R.id.navExperience
import app.robholmes.resume.R.id.navInterests
import app.robholmes.resume.R.id.navLeadership
import app.robholmes.resume.R.id.navSummary
import app.robholmes.resume.R.id.navTechnical
import app.robholmes.resume.ui.experience.ExperienceFragment
import app.robholmes.resume.ui.interests.InterestsFragment
import app.robholmes.resume.ui.leadership.LeadershipFragment
import app.robholmes.resume.ui.summary.SummaryFragment
import app.robholmes.resume.ui.technical.TechnicalFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomNav()
        showSummary()
    }

    private fun initBottomNav() {
        bottomNav.setOnNavigationItemSelectedListener(::onBottomNavItemSelected)
    }

    private fun onBottomNavItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            navSummary -> showSummary()
            navTechnical -> showTechnical()
            navLeadership -> showLeadership()
            navExperience -> showExperience()
            navInterests -> showInterests()
        }
        return true
    }

    private fun showSummary() = showFragment(SummaryFragment())

    private fun showTechnical() = showFragment(TechnicalFragment())

    private fun showLeadership() = showFragment(LeadershipFragment())

    private fun showExperience() = showFragment(ExperienceFragment())

    private fun showInterests() = showFragment(InterestsFragment())

    private fun showFragment(fragment: Fragment) =
        supportFragmentManager.commit {
            replace(R.id.container, fragment)
        }
}