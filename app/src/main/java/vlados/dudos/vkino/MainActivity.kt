package vlados.dudos.vkino

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import vlados.dudos.vkino.app.App

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_in.setOnClickListener {
            if (App.dm.isFirstLaunch()) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, ListMovieActivity::class.java))
            }
        }
        ViewAnimator().animateFadeIn(frame, 600, addTranslation = true, translationLeft = true)
    }
}