/*
 * Copyright (c) 2019. Rob Holmes. This file is part of the Resume app of Rob Holmes, an Android
 * Developer from Hampshire, United Kingdom, and is intended to demonstrate Rob's skills in an open
 * manner. It therefore should not be used for any other purpose, or any other person's Resume, as
 * this would be deceptive and misleading.
 * If you find this code anywhere other than https://github.com/robholmes/Resume then it's probably
 * a sign someone has copied it.
 */

package app.robholmes.resume.ui.summary

import androidx.lifecycle.Observer
import app.robholmes.resume.BaseTest
import app.robholmes.resume.data.MessagePublisher
import app.robholmes.resume.data.ResumeRepository
import app.robholmes.resume.data.model.Resume
import app.robholmes.resume.ui.Error
import app.robholmes.resume.ui.Loading
import app.robholmes.resume.ui.Resource
import app.robholmes.resume.ui.Success
import app.robholmes.resume.utils.CoroutineDispatcherProviderUnconfined
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class SummaryViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var resumeRepository: ResumeRepository
    @RelaxedMockK
    private lateinit var messagePublisher: MessagePublisher
    @RelaxedMockK
    private lateinit var dataMapper: SummaryDataMapper
    @RelaxedMockK
    private lateinit var resources: SummaryResourceProvider
    @RelaxedMockK
    private lateinit var dataObserver: Observer<Resource<SummaryData>>
    @RelaxedMockK
    private lateinit var resume: Resume

    @InjectMockKs
    private lateinit var viewModel: SummaryViewModel

    private val dispatchersProvider = CoroutineDispatcherProviderUnconfined()

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `refresh should fetch resume from repository`() {
        coEvery { resumeRepository.resume() } returns resume

        viewModel.data().observeForever(dataObserver)

        viewModel.refresh()

        coVerifySequence {
            dataObserver.onChanged(ofType<Loading<SummaryData>>())
            resumeRepository.resume()
            dataMapper.map(resume)
            dataObserver.onChanged(ofType<Success<SummaryData>>())
        }
    }

    @Test
    fun `refresh should trigger http error when http 500 exception is thrown`() {
        val exception = mockk<HttpException>()
        every { exception.code() } returns 500
        coEvery { resumeRepository.resume() } throws exception

        viewModel.data().observeForever(dataObserver)

        viewModel.refresh()

        coVerifyOrder {
            dataObserver.onChanged(ofType<Loading<SummaryData>>())
            resumeRepository.resume()
            resources.httpError()
            messagePublisher.show(any(), any(), viewModel::refresh)
            dataObserver.onChanged(ofType<Error<SummaryData>>())
        }
    }

    @Test
    fun `refresh should trigger not found error when http 404 exception is thrown`() {
        val exception = mockk<HttpException>()
        every { exception.code() } returns 404
        coEvery { resumeRepository.resume() } throws exception

        viewModel.data().observeForever(dataObserver)

        viewModel.refresh()

        coVerifyOrder {
            dataObserver.onChanged(ofType<Loading<SummaryData>>())
            resumeRepository.resume()
            resources.notFoundError()
            messagePublisher.show(any(), any(), viewModel::refresh)
            dataObserver.onChanged(ofType<Error<SummaryData>>())
        }
    }

    @Test
    fun `refresh should trigger unknown error when runtime exception is thrown`() {
        val exception = mockk<RuntimeException>()
        coEvery { resumeRepository.resume() } throws exception

        viewModel.data().observeForever(dataObserver)

        viewModel.refresh()

        coVerifyOrder {
            dataObserver.onChanged(ofType<Loading<SummaryData>>())
            resumeRepository.resume()
            resources.unknownError()
            messagePublisher.show(any(), any(), viewModel::refresh)
            dataObserver.onChanged(ofType<Error<SummaryData>>())
        }
    }
}