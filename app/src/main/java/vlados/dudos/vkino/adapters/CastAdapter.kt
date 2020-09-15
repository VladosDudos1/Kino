package vlados.dudos.vkino.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.cast_view.view.*
import vlados.dudos.vkino.Case.castItem
import vlados.dudos.vkino.Case.showShimmer
import vlados.dudos.vkino.R
import vlados.dudos.vkino.models.Cast

class CastAdapter(val list: List<Cast>, val onClickListener: OnClickListener) : RecyclerView.Adapter<CastAdapter.CastView>() {

    val shimmerCount: Int = 5

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastView {
        return CastView(
            LayoutInflater.from(parent.context).inflate(R.layout.cast_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        if (showShimmer){
            return shimmerCount
        }
        return list.size
    }

    override fun onBindViewHolder(holder: CastView, position: Int) {
        Glide.with(holder.itemView.cast_img)
            .load("https://image.tmdb.org/t/p/w1280" + list[position].profile_path)
            .error(R.drawable.noimage)
            .into(holder.itemView.cast_img)

        holder.itemView.cast_name.text = list[position].name

        holder.itemView.card_cast.setOnClickListener {
            castItem = list[position]

            onClickListener.clickA(list[position])
        }
    }

    class CastView(view: View) : RecyclerView.ViewHolder(view)


    interface OnClickListener{
        fun clickA(data: Cast)
    }
}