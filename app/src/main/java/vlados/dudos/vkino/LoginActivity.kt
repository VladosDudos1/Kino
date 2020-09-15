package vlados.dudos.vkino

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import vlados.dudos.vkino.Case.userToken
import vlados.dudos.vkino.app.App
import vlados.dudos.vkino.models.User


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        google_btn.setOnClickListener {
            autorise()
        }
        mail_btn.setOnClickListener(this::registr)

        ViewAnimator().animateFadeIn(linear, 600, true, true)

        done_btn.setOnClickListener {
            successReg()
        }

        to_Autorisation.setOnClickListener {
            autorise()
        }

        to_Registration.setOnClickListener(this::registr)

        auth_done.setOnClickListener {
            if (mail.text.toString().isEmpty() || !mail.text.toString()
                    .contains("@") || mail.text.toString().length <= 3
            ) {
                Toast.makeText(this, "Неправильно введён email", Toast.LENGTH_SHORT).show()
            } else if (password.text.toString().isEmpty() || password.text.toString().equals(" ")) {
                Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show()
            } else {
                var user = User(mail.text.toString(), "", "", password.text.toString())

                val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                val gson = Gson()
                val json = gson.toJson(user)

                sharedPreferences.edit().putString("user", json).apply()

                val sharedPreferencesToken = getSharedPreferences("token", Context.MODE_PRIVATE)

                val disp = App.dm.apiReg
                    .autorisation(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t ->
                        userToken = t.token.toString()
                        sharedPreferencesToken.edit().putString("token", t.token.toString()).apply()
                    }, {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }, {
                        if (userToken.isEmpty()) {
                            Toast.makeText(
                                this,
                                "Такого пользователя не существует",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            App.dm.endFirstLaunch()
                            startActivity(Intent(this, ListMovieActivity::class.java))
                        }
                    })
            }
        }
    }

    fun autorise() {
        layout.visibility = View.GONE
        layout_registration.visibility = View.GONE
        layout_autorisation.visibility = View.VISIBLE
        done_btn.visibility = View.GONE
    }


    fun registr(view: View) {
        layout.visibility = View.GONE
        layout_registration.visibility = View.VISIBLE
        layout_autorisation.visibility = View.GONE
        done_btn.visibility = View.VISIBLE
    }

    fun successReg() {
        if (edit_name_surname.text.isNullOrEmpty() || edit_mail.text.isNullOrEmpty() || edit_password.text.isNullOrEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        } else if (edit_password.text.toString() != (edit_repeat_password.text.toString())) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
        } else {

            var name = edit_name_surname.text.toString()

            val user = User(edit_mail.text.toString(), name, "", edit_password.text.toString())

            val disp = App.dm.apiReg
                .registration(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                }, {
                    if (it.message!!.contains("465")) {
                        Toast.makeText(
                            this,
                            "Пользователь с таким логином уже существует",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        autorise()
                        Toast.makeText(this, "Успешно", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}