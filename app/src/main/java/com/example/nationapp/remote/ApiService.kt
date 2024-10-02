package com.example.nationapp.remote

import android.content.Context
import com.example.nationapp.remote.CountryServiceImpl.Companion.CACHE_CONTROL
import com.example.nationapp.remote.CountryServiceImpl.Companion.TIME_CACHE_OFFLINE
import com.example.nationapp.remote.CountryServiceImpl.Companion.TIME_CACHE_ONLINE
import com.example.nationapp.remote.model.Country
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.File
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("v3.1/all?fields=name,flags")
    suspend fun getAllCountries(): List<Country>
}

class CountryServiceImpl(val context: Context) : CountryService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://restcountries.com/")
        .client(provideOkHttpClient(context))
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    override suspend fun fetchCountries(): List<Country> {
        return apiService.getAllCountries()
    }

    override suspend fun isCacheEmpty(): Boolean {
        val cache = provideOkHttpClient(context).cache()
        return cache?.size() == 0L || cache?.directory()?.listFiles()?.isEmpty() == true
    }


    private fun provideOkHttpClient(context: Context): OkHttpClient {
        // Set cache size (e.g., 10 MB)
        val cacheDirectory = File(context.cacheDir, "http_cache")

        val cache = Cache(cacheDirectory, CACHE_SIZE)

        return OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .cache(cache) // Enable caching
            .addInterceptor(CachingInterceptor(context))
            .build()
    }

    companion object {
        const val CACHE_SIZE = (10 * 1024 * 1024).toLong()
        const val READ_TIMEOUT = 50L
        const val WRITE_TIMEOUT = 50L
        const val CONNECT_TIMEOUT = 50L
        const val CACHE_CONTROL = "Cache-Control"
        const val TIME_CACHE_ONLINE = "public, max-age = 60" // 1 minute
        const val TIME_CACHE_OFFLINE = "public, only-if-cached, max-stale = 60" //1 minute
    }
}

class CachingInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        request = if (Constant.isNetworkAvailable(context)) {
            request.newBuilder().header(CACHE_CONTROL, TIME_CACHE_ONLINE).build()
        } else request.newBuilder().header(CACHE_CONTROL, TIME_CACHE_OFFLINE).build()

        val response = chain.proceed(request)

        // Customize caching headers if needed
        return response
    }

}
