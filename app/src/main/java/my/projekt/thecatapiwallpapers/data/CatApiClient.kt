package my.projekt.thecatapiwallpapers.data

import my.projekt.thecatapiwallpapers.data.remove.CatService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object CatApiClient {
    private const val BASE_URL = "https://api.thecatapi.com/v1/"
    private const val API_KEY = "live_Mn7VIpPGWGJCbSyEGwquAllFpaLWfHYZRpBGexVQm2dHjcmUVYydJi7aBsdxiEkQ"

    fun create(): CatService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("x-api-key", API_KEY)
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CatService::class.java)
    }
}
