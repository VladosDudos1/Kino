package vlados.dudos.vkino

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cast.*
import vlados.dudos.vkino.Case.castItem
import vlados.dudos.vkino.adapters.ComingAdapter
import vlados.dudos.vkino.app.App
import vlados.dudos.vkino.models.CastModel
import vlados.dudos.vkino.models.KnownFor


class CastActivity : AppCompatActivity(), ComingAdapter.OnClickListener {

    override fun clickK(data: KnownFor) {
        startActivity(Intent(this, InfoActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cast)

        var actor: CastModel? = null

        vp_c_movies.setPageTransformer(true) { page, position ->
            page.alpha = 0.5f + (1 - kotlin.math.abs(position))
            page.rotation = ((position - .1323185) * 5).toFloat()
        }

        val disp = App.dm.api
            .getCredit(castItem!!.credit_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({c ->
                actor = c
                vp_c_movies.adapter = ComingAdapter(this, c.person.known_for, this)
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }, {
                Glide.with(actor_poster)
                    .load("https://image.tmdb.org/t/p/w1280" + actor!!.person.profile_path)
                    .error(R.drawable.noimage)
                    .into(actor_poster)

                cast_name.text = actor!!.person.name

            })
    }
}