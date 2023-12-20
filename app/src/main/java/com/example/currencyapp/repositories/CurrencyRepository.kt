package com.example.currencyapp.repositories

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.Date

data class CurrencyResponse(
    val disclaimer: String,
    val date: Date,
    val base: String,
    @SerializedName("rates")
    val exchangeRates: Map<String, Double>
)

interface CurrencyApiService {
    @GET("/latest.js")
    suspend fun getCurrency(): Response<CurrencyResponse>
}

object RetrofitInstance {
    private const val BASE_URL = "https://www.cbr-xml-daily.ru"

    fun create(): CurrencyApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CurrencyApiService::class.java)
    }
}

class CurrencyRepository {
    private val apiService = RetrofitInstance.create()

    suspend fun fetchCurrencyData(): Result<CurrencyResponse> {
        return try {
            val response = apiService.getCurrency()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.message}"))
        }
    }
}
