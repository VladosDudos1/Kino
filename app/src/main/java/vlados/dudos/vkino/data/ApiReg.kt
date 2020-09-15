package vlados.dudos.vkino.data

import com.google.gson.GsonBuilder
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import vlados.dudos.vkino.models.User
import vlados.dudos.vkino.models.UserProfile

interface ApiReg {

    @GET("user/profile")
    fun getProfile(@Header("Token") accountToken: String?): Observable<UserProfile>

    @POST("users")
    fun registration(@Body user: User): Observable<User>

    @POST("user/login")
    fun autorisation(@Body user: User): Observable<User>

    companion object {
        fun createApi(): ApiReg {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val retrofit = Retrofit.Builder()
                .baseUrl("http://wsk2019.mad.hakta.pro/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(ApiReg::class.java)
        }
    }
}