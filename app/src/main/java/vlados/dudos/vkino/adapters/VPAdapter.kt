package vlados.dudos.vkino.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.vp_view.view.*
import vlados.dudos.vkino.Case
import vlados.dudos.vkino.R
import vlados.dudos.vkino.models.Backdrop

class VPAdapter : PagerAdapter {

    private val list: List<Backdrop>
    private val context: Context

    constructor(list: List<Backdrop>, context: Context) {
        this.list = list
        this.context = context
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = list.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view = LayoutInflater.from(context).inflate(R.layout.vp_view, container, false)

        Glide.with(view.img_vp)
            .load("https://image.tmdb.org/t/p/w1280" + list[position].file_path)
            .into(view.img_vp)

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }
}