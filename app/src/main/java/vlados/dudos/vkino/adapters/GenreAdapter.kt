package vlados.dudos.vkino.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.florent37.viewanimator.ViewAnimator
import kotlinx.android.synthetic.main.genre_view.view.*
import vlados.dudos.vkino.R
import vlados.dudos.vkino.models.Genre

class GenreAdapter(val list: List<Genre>, val onClickListener: OnClickListener) :
    RecyclerView.Adapter<GenreAdapter.GenreView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreView {
        return GenreView(
            LayoutInflater.from(parent.context).inflate(
                R.layout.genre_view,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: GenreView, position: Int) {
        holder.itemView.circle_view.text = list[position].name

        holder.itemView.circle_view.setOnClickListener {
            if (list[position].isClicked) {
                onClickListener.click(list[position])
                list[position].isClicked = false
                ViewAnimator
                    .animate(holder.itemView.circle_view)
                    .scale(1.15F, 1F)
                    .duration(500)
                    .start()
            } else {
                onClickListener.click(list[position])
                list[position].isClicked = true
                ViewAnimator
                    .animate(holder.itemView.circle_view)
                    .scale(0.9F, 1.15F)
                    .duration(500)
                    .start()
            }
        }

    }

    class GenreView(view: View) : RecyclerView.ViewHolder(view)

    interface OnClickListener {
        fun click(data: Genre)
    }
}