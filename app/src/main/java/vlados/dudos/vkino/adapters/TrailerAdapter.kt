package vlados.dudos.vkino.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.trailer_view.view.*
import vlados.dudos.vkino.Case
import vlados.dudos.vkino.InfoActivity
import vlados.dudos.vkino.R
import vlados.dudos.vkino.models.ResultX

class TrailerAdapter(
    val list: List<ResultX>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<TrailerAdapter.TrailerView>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerView {
        return TrailerView(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.trailer_view, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: TrailerView, position: Int) {
        Glide.with(holder.itemView.img_trailer)
            .load("https://img.youtube.com/vi/" + list[position].key + "/0.jpg")
            .into(holder.itemView.img_trailer)

        holder.itemView.card_trailer.setOnClickListener {
            Case.key = list[position].key
           onClickListener.click(list[position])
        }
    }

    class TrailerView(view: View) : RecyclerView.ViewHolder(view)

    interface OnClickListener {
        fun click(data: ResultX)
    }
}