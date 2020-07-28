package vlados.dudos.vkino

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_info.*
import vlados.dudos.vkino.Case.item
import vlados.dudos.vkino.Case.itemFull
import vlados.dudos.vkino.Case.key
import vlados.dudos.vkino.Case.video_key
import vlados.dudos.vkino.adapters.TrailerAdapter
import vlados.dudos.vkino.adapters.VPAdapter
import vlados.dudos.vkino.app.App
import vlados.dudos.vkino.models.ResultX


class InfoActivity : YouTubeBaseActivity(), TrailerAdapter.OnClickListener {

    override fun click(data: ResultX) {
        youtube_view.initialize(
            Case.googleKey,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer,
                    b: Boolean
                ) {
                    youTubePlayer.loadVideo(key)
                    non_video.visibility = View.GONE
                    youtube_frame.visibility = View.VISIBLE
//                    youTubePlayer.release()
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val disposable = App.dm.api
            .getImages(item!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({i ->
                println()
                println(i.backdrops.size)
                println()
                vp_info.adapter = VPAdapter(i.backdrops, this)
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

//        Glide.with(img_info)
//            .load("https://image.tmdb.org/t/p/w1280" + item!!.backdrop_path)
//            .error(R.drawable.noimage)
//            .into(img_info)

        arrow.setOnClickListener {
            super.onBackPressed()
        }

        mbtn_info.setOnClickListener {
//            youtube_frame.visibility = View.VISIBLE
        }
    }
}
