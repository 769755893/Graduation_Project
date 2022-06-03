package com.app.project.hotel.interceptor

import com.example.uitraning.util.log
import okhttp3.Interceptor
import okhttp3.Response
import okio.BufferedSink
import java.nio.charset.Charset

object PrintUrlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request.url().log()
        return chain.proceed(request)
    }
}