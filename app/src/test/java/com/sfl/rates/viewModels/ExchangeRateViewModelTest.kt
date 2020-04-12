package com.sfl.rates.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import com.sfl.rates.*
import com.sfl.rates.models.BankDetails
import com.sfl.rates.models.ExchangePointModel
import com.sfl.rates.repositories.ExchangeRateRepository
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times


class ExchangeRateViewModelTest : KoinTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger(Level.DEBUG)
        modules(listOf(viewModels, appRepositories, services, networkModule))
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val exchangeRateViewModel: ExchangeRateViewModel by inject()

    @Test
    fun `check getRates on success`() {

        val successLiveData = liveData {
            emit(Result.success(listOf<ExchangePointModel>()))
        }

        declareMock<ExchangeRateRepository> {
            `when`(getRates()).thenReturn(successLiveData)
        }

        val value = exchangeRateViewModel.getRates().getOrAwaitValue()

        Assert.assertEquals(true, value.isSuccess)
        Assert.assertEquals(true, value.getOrNull()?.isEmpty())
    }

    @Test
    fun `check getRates on error`() {

        val errorLiveData = liveData<Result<List<ExchangePointModel>>> {
            emit(Result.failure(Exception()))
        }

        declareMock<ExchangeRateRepository> {
            `when`(getRates()).thenReturn(errorLiveData)
        }

        val value = exchangeRateViewModel.getRates().getOrAwaitValue()

        Assert.assertEquals(true, value.isFailure)
        Assert.assertEquals(true, value.exceptionOrNull() != null)
    }

    @Test
    fun `check getRates called once`() {
        val mockRepo = declareMock<ExchangeRateRepository>()
        exchangeRateViewModel.getRates()
        Mockito.verify(mockRepo, times(1)).getRates()
    }

    @Test
    fun `check getBankDetails on onSuccess`() {

        val successLiveData = liveData {
            emit(Result.success(listOf<BankDetails.BankBranchDetail>()))
        }

        declareMock<ExchangeRateRepository> {
            `when`(getBankDetails(ArgumentMatchers.anyString())).thenReturn(successLiveData)
        }

        val value =
            exchangeRateViewModel.getBankDetails(ArgumentMatchers.anyString()).getOrAwaitValue()

        Assert.assertEquals(true, value.isSuccess)
        Assert.assertEquals(true, value.getOrNull()?.isEmpty())
    }

    @Test
    fun `check getBankDetails on onError`() {

        val errorLiveData = liveData<Result<List<BankDetails.BankBranchDetail>>> {
            emit(Result.failure(Exception()))
        }

        declareMock<ExchangeRateRepository> {
            `when`(getBankDetails(ArgumentMatchers.anyString())).thenReturn(errorLiveData)
        }

        val value =
            exchangeRateViewModel.getBankDetails(ArgumentMatchers.anyString()).getOrAwaitValue()

        Assert.assertEquals(true, value.isFailure)
        Assert.assertEquals(true, value.exceptionOrNull() != null)
    }

    @Test
    fun `check getBankDetails same ID passed to repo getBankDetails`() {
        val mockRepo = declareMock<ExchangeRateRepository>()
        exchangeRateViewModel.getBankDetails("bank_id")
        Mockito.verify(mockRepo, times(1)).getBankDetails("bank_id")
    }
}