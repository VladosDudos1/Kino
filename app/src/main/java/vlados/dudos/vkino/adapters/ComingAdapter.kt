package vlados.dudos.vkino.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.comig_view.view.*
import vlados.dudos.vkino.Case
import vlados.dudos.vkino.InfoActivity
import vlados.dudos.vkino.R
import vlados.dudos.vkino.models.KnownFor
import vlados.dudos.vkino.models.Result

class ComingAdapter(
    var context: Context,
    var list: List<KnownFor>,
    val onClickListener: OnClickListener
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(context).inflate(R.layout.comig_view, container, false)
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w1280" + list[position].poster_path)
            .into(view.img_coming)
        view.movie_name.text = list[position].title
        view.rate_text.text = list[position].vote_average.toString()
        view.f_tC.setOnClickListener { onClickListener.clickK(list[position]) }

        Case.item = Result(
            false,
            list[position].backdrop_path,
            list[position].genre_ids,
            list[position].id,
            list[position].original_language,
            list[position].original_title,
            list[position].overview,
            list[position].popularity,
            list[position].poster_path,
            list[position].release_date,
            list[position].title,
            list[position].video,
            list[position].vote_average,
            list[position].vote_count,
            false
        )
        container.addView(view)
        return view
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as FrameLayout)
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object`
    }

    interface OnClickListener {
        fun clickK(data: KnownFor)
    }
}