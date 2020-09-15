package vlados.dudos.vkino.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.search_view.view.*
import vlados.dudos.vkino.Case
import vlados.dudos.vkino.R
import vlados.dudos.vkino.models.Result

class SearchAdapter(val list: List<Result>, val onClickListener: MovieAdapter.OnClickListener): RecyclerView.Adapter<SearchAdapter.SearchView>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchView {
        return SearchView(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.search_view, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SearchView, position: Int) {
        Glide.with(holder.itemView.img_movie)
            .load("https://image.tmdb.org/t/p/w1280"+ list[position].poster_path)
            .error(R.drawable.noimage)
            .into(holder.itemView.img_movie)

        holder.itemView.movie_card.setOnClickListener {
            onClickListener.clickM(list[position])
            Case.item = list[position]
            Case.id = list[position].id
        }
        holder.itemView.name_movie.text = list[position].title
        holder.itemView.rate_search.text = list[position].vote_average.toString()
    }

    class SearchView(view: View): RecyclerView.ViewHolder(view)
}
