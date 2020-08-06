package vlados.dudos.vkino

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list_movie.*
import vlados.dudos.vkino.Case.filterId
import vlados.dudos.vkino.Case.filterList
import vlados.dudos.vkino.adapters.FilterAdapter
import vlados.dudos.vkino.adapters.MovieAdapter
import vlados.dudos.vkino.adapters.NewsAdapter
import vlados.dudos.vkino.adapters.SearchAdapter
import vlados.dudos.vkino.app.App
import vlados.dudos.vkino.models.Genre
import vlados.dudos.vkino.models.Result
import java.util.*

class ListMovieActivity : AppCompatActivity(), TextWatcher, SearchAdapter.OnClickListener,
    MovieAdapter.OnClickListener, FilterAdapter.OnClickListener, NewsAdapter.OnClickListener {

    override fun clickM(data: Result) {
        startActivity(Intent(this, InfoActivity::class.java))
    }

    override fun click(data: Result) {
        startActivity(Intent(this, InfoActivity::class.java))
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun click(data: Genre) {
        filterList.clear()
        for (i in listM) {
            for (j in i.genre_ids) {
                if (j == filterId) {
                    filterList.add(i)
                }
            }
        }
        rv_movie.adapter = MovieAdapter(filterList, listF, this)
    }


    var listN = listOf<Result>()
    var listF = listOf<Genre>()
    var listM = listOf<Result>()

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val disp = App.dm.api
            .searchAdult(edit_search.text.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ s ->
                rv_search.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                for (i in 0..s.results.size - 1) {
                    for (j in 0..s.results.size - 1) {
                        if (i != j && s.results[i].vote_average > s.results[j].vote_average) {
                            Collections.swap(s.results, i, j)
                        }
                    }
                }
                rv_search.adapter =
                    SearchAdapter(s.results, this)
            }, {
            })
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_movie)

        text_language.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }



        edit_search.setOnTouchListener { view, motionEvent ->
            arrowback.visibility = View.VISIBLE
            card_initials.visibility = View.GONE
            anim_layout.visibility = View.GONE
            rv_search.visibility = View.VISIBLE
            false
        }


        var name = ""
        val sharedPreferencesToken = getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = sharedPreferencesToken.getString("token", "")

        val getUser = App.dm.apiReg
            .getProfile(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ u ->
                name = u.content.nickName
            }, {
                it.message
            }, {
                text_language.text = name[0].toString().capitalize() + name[1].toString()
            })


        ViewAnimator().topAnimate(anim_layout, 600, true, true)
        edit_search.addTextChangedListener(this)

        rv_new.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_filter.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_movie.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val disp = App.dm.api
            .getNowPlaing()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ n ->
                listN = n.results
            }, {
                Toast.makeText(this, "news problem", Toast.LENGTH_SHORT).show()
            }, {
                val dispose = App.dm.api
                    .getPopular()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ p ->
                        listM = p.results
                    }, {
                        Toast.makeText(this, "popular problem", Toast.LENGTH_SHORT).show()
                    }, {
                        val newDisp = App.dm.api
                            .getGenre()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ g ->
                                pb_n.visibility = View.GONE
                                listF = g.genres
                            }, {
                                Toast.makeText(this, "genres problem", Toast.LENGTH_SHORT).show()
                            }, {
                                rv_new.adapter =
                                    NewsAdapter(listN, this)
                                rv_filter.adapter =
                                    FilterAdapter(listF, this)

                                rv_movie.adapter =
                                    MovieAdapter(listM, listF, this)

                                pb_n.visibility = View.GONE
                                rv_new.visibility = View.VISIBLE
                                rv_filter.visibility = View.VISIBLE
                                rv_movie.visibility = View.VISIBLE
                            })
                    })
            })

        arrowback.setOnClickListener {
            edit_search.text.clear()
            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
            arrowback.visibility = View.GONE
            rv_search.visibility = View.GONE
            card_initials.visibility = View.VISIBLE
            anim_layout.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        arrowback.visibility = View.GONE
        rv_search.visibility = View.GONE
        card_initials.visibility = View.VISIBLE
        anim_layout.visibility = View.VISIBLE
    }
}