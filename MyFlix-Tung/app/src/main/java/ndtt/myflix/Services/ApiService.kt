package ndtt.myflix.Services

import ndtt.myflix.models.MovieList
import ndtt.myflix.models.Results
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesApi {

    @GET("/3/movie/top_rated?api_key=005034fdbc49e9c5e99a9ee875047614")
    suspend fun getListMovie() : Response<MovieList>

    @GET("/3/movie/{id}?api_key=005034fdbc49e9c5e99a9ee875047614")
    suspend fun getDetailMovie(@Path("id") id: Int) : Response<Results>

}