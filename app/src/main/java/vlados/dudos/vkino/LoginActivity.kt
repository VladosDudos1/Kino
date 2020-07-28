package vlados.dudos.vkino

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreferences = getSharedPreferences("autorisation", Context.MODE_PRIVATE)

        google_btn.setOnClickListener(this::genreopen)
        mail_btn.setOnClickListener(this::genreopen)

        ViewAnimator().animateFadeIn(linear, 600, true, true)

        done_btn.setOnClickListener {
            if ((edit_name.text.contains(" ") || edit_surname.text.contains(" "))) {
                Toast.makeText(this, "Пожалуйста, уберите пробелы", Toast.LENGTH_SHORT).show()
            } else {
                if (edit_name.text.isNotEmpty() && edit_surname.text.isNotEmpty()) {
                    sharedPreferences.edit().putString("name", edit_name.text.toString()).apply()
                    sharedPreferences.edit().putString("surname", edit_surname.text.toString())
                        .apply()
                    startActivity(Intent(this, GenreActivity::class.java))
                } else Toast.makeText(this, "Пожалуйста, укажите свои имя и фамилию", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun genreopen(view: View) {
        layout_firstLaunch.visibility = View.GONE
        layout_autorisation.visibility = View.VISIBLE
        done_btn.visibility = View.VISIBLE
    }
}
