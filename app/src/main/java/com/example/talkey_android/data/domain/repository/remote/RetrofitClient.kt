package com.example.talkey_android.data.domain.repository.remote

import com.caverock.androidsvg.BuildConfig
import com.example.talkey_android.data.constants.Constants.BASE_URL
import com.example.talkey_android.data.constants.Constants.RETROFIT_TIMEOUT_IN_SECOND
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val retrofit: Retrofit
    private val remoteApiService: RemoteApiService

    init {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        httpClient
            .connectTimeout(RETROFIT_TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
            .readTimeout(RETROFIT_TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
            .writeTimeout(RETROFIT_TIMEOUT_IN_SECOND, TimeUnit.SECONDS)

        httpClient.interceptors().clear()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()

            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(logging)
        }

        val gson = GsonBuilder().setLenient().create()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .build()
        remoteApiService = retrofit.create(RemoteApiService::class.java)
    }

    fun getApiServices(): RemoteApiService {
        return remoteApiService
    }
}