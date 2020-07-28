package vlados.dudos.vkino.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_view.view.*
import vlados.dudos.vkino.Case.item
import vlados.dudos.vkino.R
import vlados.dudos.vkino.models.Genre
import vlados.dudos.vkino.models.Result

class MovieAdapter(val list: List<Result>, val listGenres: List<Genre>, val onClickListener: OnClickListener) :
    RecyclerView.Adapter<MovieAdapter.MovieView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieView {
        return MovieView(
            LayoutInflater.from(parent.context).inflate(
                R.layout.movie_view,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MovieView, position: Int) {
        Glide.with(holder.itemView.movie_img)
            .load("https://image.tmdb.org/t/p/w1280" + list[position].backdrop_path)
            .error(R.drawable.noimage)
            .into(holder.itemView.movie_img)

        holder.itemView.movie_name.text = list[position].title
        holder.itemView.rate_txt.text = (list[position].vote_average.toString().replace(".", ","))

        holder.itemView.genre_text.text = ""

        for (i in 0..listGenres.size - 1){
            if (list[position].genre_ids.contains(listGenres[i].id)) {
                if (holder.itemView.genre_text.text.length == 0)
                    holder.itemView.genre_text.text = String.format("%s", listGenres[i].name)
                else
                    holder.itemView.genre_text.text = String.format("%s, %s", holder.itemView.genre_text.text.toString(), listGenres[i].name)
            }
        }
        holder.itemView.card_m.setOnClickListener {
            onClickListener.clickM(list[position])
            item = list[position]
        }
    }

    class MovieView(view: View) : RecyclerView.ViewHolder(view)

    interface OnClickListener{
        fun clickM(data: Result)
    }
}