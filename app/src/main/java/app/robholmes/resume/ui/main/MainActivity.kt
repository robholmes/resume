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
import app.robholmes.resume.data.model.ActionMessage
import app.robholmes.resume.data.model.Message
import app.robholmes.resume.data.model.SimpleMessage
import app.robholmes.resume.ui.experience.ExperienceFragment
import app.robholmes.resume.ui.interests.InterestsFragment
import app.robholmes.resume.ui.leadership.LeadershipFragment
import app.robholmes.resume.ui.summary.SummaryFragment
import app.robholmes.resume.ui.technical.TechnicalFragment
import app.robholmes.resume.utils.CoroutineDispatcherProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : FragmentActivity(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModel()
    private val dispatchersProvider: CoroutineDispatcherProvider by inject()

    private val backgroundJob = Job()
    private val backgroundScope = CoroutineScope(dispatchersProvider.Main + backgroundJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomNav()
        initSnackbarObserver()
        showSummary()
    }

    private fun initBottomNav() {
        bottomNav.setOnNavigationItemSelectedListener(::onBottomNavItemSelected)
    }

    private fun initSnackbarObserver() {
        backgroundScope.launch {
            viewModel.snackbarMessages().consumeEach { message ->
                withContext(dispatchersProvider.Main) {
                    onSnackbarMessage(message)
                }
            }
        }
    }

    private fun onSnackbarMessage(message: Message) {
        when (message) {
            is SimpleMessage -> showSimpleMessage(message)
            is ActionMessage -> showActionMessage(message)
        }
    }

    private fun showSimpleMessage(message: SimpleMessage) {
        createSnackbar(message).show()
    }

    private fun showActionMessage(message: ActionMessage) {
        createSnackbar(message)
            .setAction(message.actionTitle) { message.actionCallback.invoke() }
            .show()
    }

    private fun createSnackbar(message: Message) = Snackbar
        .make(container, message.message, LENGTH_LONG)
        .setAnchorView(bottomNav)

    private fun onBottomNavItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            navSummary -> showSummary()
            navTechnical -> showTechnical()
            navLeadership -> showLeadership()
            navExperience -> showExperience()
            navInterests -> showInterests()
        }
        Timber.d("Nav item shown '${item.title}'")
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