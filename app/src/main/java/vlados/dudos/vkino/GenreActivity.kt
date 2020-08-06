package vlados.dudos.vkino

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_genre.*
import vlados.dudos.vkino.adapters.GenreAdapter
import vlados.dudos.vkino.app.App
import vlados.dudos.vkino.models.Genre

class GenreActivity : AppCompatActivity(), GenreAdapter.OnClickListener {
    override fun click(data: Genre) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre)

        podtverdit_btn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        rv.layoutManager = GridLayoutManager(this, 3)
        ViewAnimator().animateFadeIn(sc_view, 600, true, false)

        val disp = App.dm.api
            .getGenre()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({g ->
                pb_g.visibility = View.GONE
                rv.visibility = View.VISIBLE
                rv.adapter =
                    GenreAdapter(g.genres, this)
            },{
                Toast.makeText(this, "You have no internet!", Toast.LENGTH_SHORT).show()
            })
    }
}
