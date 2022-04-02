package com.example.whiterabbitdemo.utils

import com.example.whiterabbitdemo.model.EmployeeResponseItem
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface EndPoints {

    /*GetList*/
    @GET("/v2/5d565297300000680030a986")
    fun getEmployeeList():
            Observable<List<EmployeeResponseItem>>

    companion object {

        fun create(): EndPoints {

            val logging = HttpLoggingInterceptor()

            // set your desired log level
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging);
            httpClient.connectTimeout(2, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(2, TimeUnit.MINUTES) // write timeout
                .readTimeout(2, TimeUnit.MINUTES); // read timeout

            val retrofit = Retrofit.Builder().addCallAdapterFactory(
                RxJava2CallAdapterFactory.create()
            )
                .addConverterFactory(
                    GsonConverterFactory.create()
                )
                .baseUrl(Constants.BASE_URL)
                .client(httpClient.build())
                .build()

            return retrofit.create(EndPoints::class.java)
        }
    }
}
