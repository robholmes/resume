/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.ui.summary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.robholmes.resume.data.MessagePublisher
import app.robholmes.resume.data.ResumeRepository
import app.robholmes.resume.ui.Error
import app.robholmes.resume.ui.Loading
import app.robholmes.resume.ui.Resource
import app.robholmes.resume.ui.Success
import app.robholmes.resume.utils.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class SummaryViewModel(
    private val resumeRepository: ResumeRepository,
    private val messagePublisher: MessagePublisher,
    private val dataMapper: SummaryDataMapper,
    private val resources: SummaryResourceProvider,
    dispatchersProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(dispatchersProvider.Main + viewModelJob)

    private val data = MutableLiveData<Resource<SummaryData>>()

    fun data(): LiveData<Resource<SummaryData>> = data

    fun refresh() {
        viewModelScope.launch {
            showLoading()
            doRefresh()
        }
    }

    private suspend fun doRefresh() {
        try {
            val resume = resumeRepository.resume() ?: throw Exception()
            data.value = Success(dataMapper.map(resume))
        } catch (e: HttpException) {
            Timber.e(e)
            messagePublisher.show(httpErrorToMessage(e), resources.retry(), ::refresh)
            showError()
        } catch (e: Exception) {
            Timber.e(e)
            messagePublisher.show(resources.unknownError(), resources.retry(), ::refresh)
            showError()
        }
    }

    private fun httpErrorToMessage(httpException: HttpException): String = when {
        httpException.code() >= 500 -> resources.httpError()
        httpException.code() == 404 -> resources.notFoundError()
        else -> resources.unknownError()
    }

    private fun showLoading() {
        data.value = Loading()
    }

    private fun showError() {
        data.value = Error()
    }
}