package com.sfl.rates

import androidx.preference.PreferenceManager
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sfl.rates.repositories.ExchangeRateRepository
import com.sfl.rates.services.RateApiService
import com.sfl.rates.viewModels.ExchangeRateViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.experimental.builder.single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appRepositories: Module = module {
    single<ExchangeRateRepository>()
}

val viewModels: Module = module {
    viewModel<ExchangeRateViewModel>()
}

val networkModule: Module = module {

    // Gson
    single {
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .setPrettyPrinting()
            .create()
    }

    // OkHttpClient
    single {
        val okHttpBuilder = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }
        // Also you can add your own interceptor
        okHttpBuilder
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(
                10,
                TimeUnit.MINUTES
            ) // fix exception when uploading files with slow internet
            .readTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    // Retrofit
    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
            .baseUrl(BuildConfig.BASE_RATE_URL)
            .client(get<OkHttpClient>())
            .build()
    }
}

val services: Module = module {
    single { get<Retrofit>().create(RateApiService::class.java) }
    single { PreferenceManager.getDefaultSharedPreferences(get()) }
}