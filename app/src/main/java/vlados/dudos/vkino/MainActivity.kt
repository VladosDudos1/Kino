package vlados.dudos.vkino

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import vlados.dudos.vkino.Case.userToken
import vlados.dudos.vkino.app.App
import vlados.dudos.vkino.models.User
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_in.setOnClickListener {
            if (App.dm.isFirstLaunch()) {
                startActivity(Intent(this, GenreActivity::class.java))

            } else {
                val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                val gson = Gson()
                val json = sharedPreferences.getString("user", null)
                val user = gson.fromJson<User>(json, User::class.java)
                val tokenPref = getSharedPreferences("token", Context.MODE_PRIVATE)
                val jsonToken = tokenPref.getString("token", null)

                val disp = App.dm.apiReg
                    .autorisation(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t ->
                        userToken = jsonToken.toString()
                    }, {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    })


                startActivity(Intent(this, ListMovieActivity::class.java))
            }
        }

        ViewAnimator().animateFadeIn(frame, 600, addTranslation = true, translationLeft = true)
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}