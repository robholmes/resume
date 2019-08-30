/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.ui.summary

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import app.robholmes.resume.R
import app.robholmes.resume.ext.removePlaceholder
import app.robholmes.resume.ext.showPlaceholder
import app.robholmes.resume.ui.Error
import app.robholmes.resume.ui.Loading
import app.robholmes.resume.ui.Success
import com.facebook.imagepipeline.request.ImageRequest.fromUri
import kotlinx.android.synthetic.main.summary.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SummaryFragment : Fragment(R.layout.summary) {

    private val viewModel: SummaryViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.data().observe(this, Observer {
            when (it) {
                is Loading -> loading()
                is Error -> error()
                is Success -> success(it.value)
            }
        })

        refresh.setOnRefreshListener(viewModel::refresh)

        viewModel.refresh()
    }

    private fun loading() {
        refresh.isRefreshing = true
        picture.setActualImageResource(0)
        name.showPlaceholder()
        label.showPlaceholder()
        summary.showPlaceholder(lines = 4)
        location.showPlaceholder()
        website.showPlaceholder()
    }

    private fun error() {
        refresh.isRefreshing = false
        // TODO: add error icon in centre of the view, leaving placeholders in place
    }

    private fun success(data: SummaryData) {
        refresh.isRefreshing = false
        picture.setImageRequest(fromUri(data.pictureUrl))
        name.removePlaceholder { text = data.name }
        label.removePlaceholder { text = data.label }
        summary.removePlaceholder { text = data.summary }
        location.removePlaceholder { text = data.location }
        website.removePlaceholder {
            text = data.website
            setOnClickListener { onWebsiteClick(data.website) }
        }
    }

    private fun onWebsiteClick(url: String) {
        try {
            val intent = Intent(ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (e: Exception) {
            Timber.e(e, "Invalid URL")
        }
    }
}