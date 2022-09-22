package ru.stersh.apisonic.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor(
    private val userName: String,
    private val password: String,
    private val apiVersion: String,
    private val clientId: String
) : Interceptor {

    val joinedQueries = "u=$userName&p=$password&v=$apiVersion&c=$clientId&f=json"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter("u", userName)
            .addQueryParameter("p", password)
            .addQueryParameter("v", apiVersion)
            .addQueryParameter("c", clientId)
            .addQueryParameter("f", "json")
            .build()
        return chain.proceed(request.newBuilder().url(url).build())
    }

}