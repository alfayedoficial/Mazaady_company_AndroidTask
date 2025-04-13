package com.afapps.mazaadyAndroidTask.di


import com.afapps.mazaadyAndroidTask.BuildConfig
import com.afapps.mazaadyAndroidTask.data.remote.api.ApiService
import com.afapps.mazaadyAndroidTask.utilities.AppConstants.ACCESS_TOKEN
import com.afapps.mazaadyAndroidTask.utilities.AppConstants.REQUEST_TIME
import com.afapps.mazaadyAndroidTask.utilities.annotation.BaseURL
import com.afapps.mazaadyAndroidTask.utilities.annotation.ImageBaseURL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHeadersInterceptor() = Interceptor { chain ->
        chain.proceed(
            chain.request().newBuilder()
                .addHeader("Authorization", ACCESS_TOKEN)
                .addHeader("Content-Type", "application/json")
                .build()
        )
    }


    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        headersInterceptor: Interceptor,
        logging: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(REQUEST_TIME, TimeUnit.MINUTES)
            .connectTimeout(REQUEST_TIME, TimeUnit.MINUTES)
            .addInterceptor(headersInterceptor)
            .addNetworkInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun getDynamicRetrofitClient(
        okHttpClient: OkHttpClient,
        @BaseURL baseUrl: String
    ): Retrofit {

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun provideApiServices(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @BaseURL
    @Provides
    @Singleton
    fun provideBaseURL(): String {
        return BuildConfig.SERVER_URL
    }

    @ImageBaseURL
    @Provides
    @Singleton
    fun provideImageBaseURL(): String {
        return BuildConfig.POSTER_URL
    }
}
