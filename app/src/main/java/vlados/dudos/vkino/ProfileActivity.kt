package vlados.dudos.vkino

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_profile.*
import vlados.dudos.vkino.Case.userToken
import vlados.dudos.vkino.app.App


class ProfileActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        val disp = App.dm.apiReg
            .getProfile(userToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ up ->
                text_mail.text = up.content.email
                text_name.text = up.content.nickName
                text_id.text = up.content.nickName[0].toString()
                    .capitalize() + up.content.nickName[1].toString()
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            })

        logout_btn.setOnClickListener {
            finish()
            App.dm.logout()
            startActivity(Intent(this, MainActivity::class.java))
        }

        themeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

    }
}