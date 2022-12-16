package ndtt.myflix.Services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object API {

    val baseUrl = "https://api.themoviedb.org"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}