package com.app.project.hotel.base

import com.app.project.hotel.api.HotelApi
import com.app.project.hotel.api.LoginApi
import com.app.project.hotel.api.SuperApi
import com.app.project.hotel.api.UserApi
import com.app.project.hotel.interceptor.PrintUrlInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideLoginApiService(): LoginApi {
        val http = OkHttpClient.Builder()
            .addInterceptor(PrintUrlInterceptor)
            .connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://124.223.190.160:8080/hotel/login/")
            .client(http)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(LoginApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserApiService(): UserApi {
        val http = OkHttpClient.Builder()
            .addInterceptor(PrintUrlInterceptor)
            .connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://124.223.190.160:8080/hotel/user/")
            .client(http)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSuperApiService(): SuperApi {
        val http = OkHttpClient.Builder()
            .connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .addInterceptor(PrintUrlInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://124.223.190.160:8080/hotel/super/")
            .client(http)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(SuperApi::class.java)
    }

    @Singleton
    @Provides
    fun provideHotelApiService(): HotelApi {
        val http = OkHttpClient.Builder()
            .connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .addInterceptor(PrintUrlInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://124.223.190.160:8080/hotel/hotel/")
            .client(http)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(HotelApi::class.java)
    }
}