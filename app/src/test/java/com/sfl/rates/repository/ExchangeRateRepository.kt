package com.sfl.rates.repository

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.sfl.rates.*
import com.sfl.rates.enums.CurrencyEnum
import com.sfl.rates.models.BankDetails
import com.sfl.rates.repositories.ExchangeRateRepository
import com.sfl.rates.services.RateApiService
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.Mockito

class ExchangeRateRepositoryTest : KoinTest {

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

    val exchangeRateRepository: ExchangeRateRepository by inject()

    val dummyStr = "dymmy_str"

    @Test
    fun `check getRates with success`() = runBlocking {

        declareMock<SharedPreferences> {
            Mockito.`when`(getString("currency", "")).thenReturn(dummyStr)
        }

        declareMock<Gson> {
            Mockito.`when`(fromJson(dummyStr, CurrencyEnum::class.java))
                .thenReturn(CurrencyEnum.AUD)
        }

        val mockedService = declareMock<RateApiService> {
            Mockito.`when`(getExchangePoints()).thenReturn(mapOf())
        }

        val value = exchangeRateRepository.getRates().getOrAwaitValue()

        Mockito.verify(mockedService, Mockito.times(1)).getExchangePoints()
        Assert.assertEquals(true, value.isSuccess)
        Assert.assertEquals(true, value.getOrNull() != null)
    }

    @Test
    fun `check getRates with error`() = runBlocking {

        declareMock<SharedPreferences> {
            Mockito.`when`(getString("currency", "")).thenReturn(dummyStr)
        }

        declareMock<Gson> {
            Mockito.`when`(fromJson(dummyStr, CurrencyEnum::class.java))
                .thenReturn(CurrencyEnum.AUD)
        }

        val mockedService = declareMock<RateApiService> {
            Mockito.`when`(getExchangePoints())
                .thenThrow(RuntimeException()) // currently mockito does not support checked exceptions in kotlin :(
        }

        val value = exchangeRateRepository.getRates().getOrAwaitValue()

        Mockito.verify(mockedService, Mockito.times(1)).getExchangePoints()
        Assert.assertEquals(true, value.isFailure)
        Assert.assertEquals(true, value.getOrNull() == null)
    }

    @Test
    fun `check getBankDetails with success`() = runBlocking {

        val mockedService = declareMock<RateApiService> {
            Mockito.`when`(getBankBranches(dummyStr)).thenReturn(BankDetails())
        }

        val value = exchangeRateRepository.getBankDetails(dummyStr).getOrAwaitValue()
        Mockito.verify(mockedService, Mockito.times(1)).getBankBranches(dummyStr)

        Assert.assertEquals(true, value.isSuccess)
        Assert.assertEquals(true, value.getOrNull() != null)
    }

    @Test
    fun `check getBankDetails with error`() = runBlocking {

        val mockedService = declareMock<RateApiService> {
            Mockito.`when`(getBankBranches(dummyStr))
                .thenThrow(RuntimeException()) // currently mockito does not support checked exceptions in kotlin :(
        }

        val value = exchangeRateRepository.getBankDetails(dummyStr).getOrAwaitValue()

        Mockito.verify(mockedService, Mockito.times(1)).getBankBranches(dummyStr)
        Assert.assertEquals(true, value.isFailure)
        Assert.assertEquals(true, value.getOrNull() == null)
    }


}