package my.projekt.thecatapiwallpapers.data.remove

import my.projekt.thecatapiwallpapers.domain.model.CatResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CatService {
    @GET("images/search?limit=1")
    fun getRandomCat(@Header("x-api-key") apiKey: String): Call<List<CatResponse>>
}
