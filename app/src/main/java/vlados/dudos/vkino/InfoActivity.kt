package vlados.dudos.vkino

import android.R.attr.left
import android.R.attr.right
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_info.*
import vlados.dudos.vkino.Case.item
import vlados.dudos.vkino.Case.itemFull
import vlados.dudos.vkino.Case.key
import vlados.dudos.vkino.adapters.TrailerAdapter
import vlados.dudos.vkino.adapters.VPAdapter
import vlados.dudos.vkino.app.App
import vlados.dudos.vkino.models.ResultX


class InfoActivity : AppCompatActivity(), TrailerAdapter.OnClickListener {

    override fun click(data: ResultX) {
        startActivity(Intent(this, TrailerActivity::class.java))
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)


        card_back.setOnClickListener {
            vp_info.currentItem -= 1
        }
        card_next.setOnClickListener {
            vp_info.currentItem += 1
        }

        vp_info.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == vp_info.adapter!!.count - 1) {
                    card_next.visibility = View.GONE
                } else {
                    card_next.visibility = View.VISIBLE
                }
                if (position == 0) {
                    card_back.visibility = View.GONE
                } else {
                    card_back.visibility = View.VISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        val disposable = App.dm.api
            .getImages(item!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ i ->
                vp_info.adapter = VPAdapter(i.backdrops, this)
                if (i.backdrops.isNullOrEmpty()){
                    vp_info.visibility = View.GONE
                    card_next.visibility = View.GONE
                    img_vp_error.visibility = View.VISIBLE
                }
            }, {
                println(it.message)
                Toast.makeText(this, "sorry, переработаю", Toast.LENGTH_SHORT).show()
            })

        val dispose = App.dm.api
            .getItem(item!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ i ->
                itemFull = i
                genre.text = ""

                for (g in itemFull!!.genres) {
                    genre.text = genre.text.toString() + g.name + ", "
                }
                genre.text = genre.text.removeRange(genre.text.length - 2..genre.text.length - 1)
                var min = (itemFull!!.runtime % 60).toString()
                if (min.length == 1) {
                    min = "0" + min
                }
                time.text =
                    itemFull!!.runtime.toString() + " мин. " + "(0" + (itemFull!!.runtime / 60).toString() + ":" + min + ")"
            }, {
                time.text = "(00:00)"
            })

        name.text = item!!.title
        var list = listOf<ResultX>()

        if (item!!.adult) {
            adult.text = "18+ / "
        } else adult.text = "Family content / "

        plot.text = item!!.overview

        val disp = App.dm.api
            .getTrailers(item!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t ->
                rv_trailer.visibility = View.VISIBLE
                no_internet_img.visibility = View.GONE
                no_trailer_img.visibility = View.GONE
                rv_trailer.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                list = t.results
                rv_trailer.adapter =
                    TrailerAdapter(list, this)
                if (t.results.isEmpty()) {
                    rv_trailer.visibility = View.GONE
                    no_internet_img.visibility = View.GONE
                    no_trailer_img.visibility = View.VISIBLE
                    mbtn_info.visibility = View.GONE
                }
            }, {
                text_trailer.visibility = View.GONE
                rv_trailer.visibility = View.GONE
                no_internet_img.visibility = View.VISIBLE
                no_trailer_img.visibility = View.GONE
            })

        arrow.setOnClickListener {
            super.onBackPressed()
        }

        mbtn_info.setOnClickListener {
            key = list[0].key
            click(list[0])
        }
    }
}
