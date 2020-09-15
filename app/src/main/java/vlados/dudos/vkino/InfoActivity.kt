package vlados.dudos.vkino

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_info.*
import vlados.dudos.vkino.Case.bodyElement
import vlados.dudos.vkino.Case.id
import vlados.dudos.vkino.Case.item
import vlados.dudos.vkino.Case.itemFull
import vlados.dudos.vkino.Case.key
import vlados.dudos.vkino.Case.rate_item
import vlados.dudos.vkino.Case.showShimmer
import vlados.dudos.vkino.adapters.TrailerAdapter
import vlados.dudos.vkino.adapters.VPAdapter
import vlados.dudos.vkino.adapters.VpInfoAdapter
import vlados.dudos.vkino.app.App
import vlados.dudos.vkino.models.RateBodyModel
import vlados.dudos.vkino.models.ResultX


class InfoActivity : AppCompatActivity(), TrailerAdapter.OnClickListener {

    override fun click(data: ResultX) {
        startActivity(Intent(this, TrailerActivity::class.java))
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        toActor_btn.setOnClickListener {
            vp_cast.currentItem++
        }

        toPlot_btn.setOnClickListener {
            vp_cast.currentItem--
        }

        rate.setOnClickListener {
            info_layout.visibility = View.VISIBLE
        }

        scroll_main.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            info_layout.visibility = View.GONE
        }

        card_rate.setOnClickListener {


            MaterialDialog(this)
                .noAutoDismiss()
                .title(text = "Выберите оценку")
                .listItemsSingleChoice(
                    items = listOf(
                        "1",
                        "2",
                        "3",
                        "4",
                        "5",
                        "6",
                        "7",
                        "8",
                        "9",
                        "10"
                    ), selection = { dialog, i, text ->
                        bodyElement = when (i) {
                            0 -> RateBodyModel(1.0)
                            1 -> RateBodyModel(2.0)
                            2 -> RateBodyModel(3.0)
                            3 -> RateBodyModel(4.0)
                            4 -> RateBodyModel(5.0)
                            5 -> RateBodyModel(6.0)
                            6 -> RateBodyModel(7.0)
                            7 -> RateBodyModel(8.0)
                            8 -> RateBodyModel(9.0)
                            else -> RateBodyModel(10.0)
                        }
                        rate_item = item
                        dialog.cancel()
                        postReq()
                    }).show { }
        }



        vp_cast.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    toActor_btn.visibility = View.VISIBLE
                    toPlot_btn.visibility = View.GONE
                }
                if (position == 1) {
                    toActor_btn.visibility = View.GONE
                    toPlot_btn.visibility = View.VISIBLE
                }
            }
        })

        rate_text.text = "Rate:\n" + item!!.vote_average + "/10"

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
                if (i.backdrops.isNullOrEmpty()) {
                    vp_info.visibility = View.GONE
                    card_next.visibility = View.GONE
                    img_vp_error.visibility = View.VISIBLE
                }
            }, {
                println(it.message)
                Toast.makeText(this, "sorry, переработаю", Toast.LENGTH_SHORT).show()
            })

        val actor = App.dm.api
            .getActors(id.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ a ->
                vp_cast.adapter = VpInfoAdapter(a.cast, this, item!!.overview)
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }, {
                showShimmer = false
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

    fun postReq() {

        var guest = ""

        val getGuest = App.dm.api
            .guestSession()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ i ->
                guest = i.guest_session_id
            }, {
                Log.d("", "")
            }, {
                val disp = App.dm.api
                    .postValue(item!!.id.toString(), guest, bodyElement)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ p ->
                        item = rate_item
                        val CHANNEL_ID = "favoriteUpdate"

                        val notificationManager =
                            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) notificationManager.createNotificationChannel(
                            NotificationChannel(
                                CHANNEL_ID,
                                "favoriteChannel",
                                NotificationManager.IMPORTANCE_DEFAULT
                            )
                        )
                        notificationManager.notify(
                            0, NotificationCompat.Builder(this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.sistem)
                                .setContentTitle(item!!.title)
                                .setContentText("Спасибо за вашу оценку!")
                                .setAutoCancel(true)
//                                .setContentIntent(
//                                    PendingIntent.getActivity(
//                                        this,
//                                        0,
//                                        Intent(this, InfoActivity::class.java),
//                                        PendingIntent.FLAG_UPDATE_CURRENT
//                                    ))
                                .build()
                        )
                    }, {
                        Log.d("", "")
                    })
            })
    }
}
