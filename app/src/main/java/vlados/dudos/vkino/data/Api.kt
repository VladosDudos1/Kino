package vlados.dudos.vkino.data

import com.google.gson.GsonBuilder
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import vlados.dudos.vkino.models.*


interface Api {

    @GET("genre/movie/list?api_key=f4247e4401183f4fd54b6f52c8e0b48c&language=ru-RU")
    fun getGenre(): Observable<GenreModel>

    @GET("movie/now_playing?api_key=f4247e4401183f4fd54b6f52c8e0b48c&language=ru-RU&region=RU")
    fun getNowPlaing(): Observable<MovieModel>

    @POST("movie/{movie_id}/rating?api_key=f4247e4401183f4fd54b6f52c8e0b48c")
    fun postValue(@Path("movie_id") movie_id: String, @Query("guest_session_id") guest_session_id: String, @Body rateBodyModel: RateBodyModel): Observable<RateResponseModel>

    @GET("movie/popular?api_key=f4247e4401183f4fd54b6f52c8e0b48c&language=ru-RU&region=RU")
    fun getPopular(): Observable<MovieModel>

    @GET("authentication/guest_session/new?api_key=f4247e4401183f4fd54b6f52c8e0b48c")
    fun guestSession(): Observable<GuestModel>

//    @GET("movie/popular?api_key=f4247e4401183f4fd54b6f52c8e0b48c&language=ru-RU")
//    fun getPopular(@Query("page")page: Int): Observable<MovieModel>

    @GET("movie/{movie_id}/videos?language=ru-RU&api_key=f4247e4401183f4fd54b6f52c8e0b48c")
    fun getTrailers(@Path("movie_id") movieId: Int): Observable<TrailerModel>

    @GET("movie/{movie_id}?api_key=f4247e4401183f4fd54b6f52c8e0b48c&language=ru-RU")
    fun getItem(@Path("movie_id") movieId: Int): Observable<ItemModel>

    @GET("search/movie?api_key=f4247e4401183f4fd54b6f52c8e0b48c&language=ru-Ru&page=1&include_adult=true")
    fun searchAdult(@Query("query") query: String): Observable<SearchModel>

    @GET("movie/{movie_id}/images?api_key=f4247e4401183f4fd54b6f52c8e0b48c")
    fun getImages(@Path("movie_id") movieId: Int): Observable<ImageModel>

    @GET("movie/{movie_id}/credits?api_key=f4247e4401183f4fd54b6f52c8e0b48c&language=ru-RU")
    fun getActors(@Path("movie_id") movie_id: String): Observable<ActorModel>

    @GET("credit/{credit_id}?api_key=f4247e4401183f4fd54b6f52c8e0b48c")
    fun getCredit(@Path("credit_id") credit_id: String) : Observable<CastModel>

    companion object {

        fun createApi(): Api {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(Api::class.java)
        }
    }
}