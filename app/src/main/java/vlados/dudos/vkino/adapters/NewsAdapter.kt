package vlados.dudos.vkino.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.new_view.view.*
import vlados.dudos.vkino.Case
import vlados.dudos.vkino.Case.item
import vlados.dudos.vkino.R
import vlados.dudos.vkino.models.Result

class NewsAdapter (val list: List<Result>, val onClickListener: MovieAdapter.OnClickListener): RecyclerView.Adapter<NewsAdapter.NewsView>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsView {
        return NewsView(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.new_view, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NewsView, position: Int) {

        Glide.with(holder.itemView.bg_img)
            .load("https://image.tmdb.org/t/p/w1280" + list[position].poster_path)
            .error(R.drawable.noimage)
            .into(holder.itemView.bg_img)

        holder.itemView.date_text.text = list[position].release_date
        holder.itemView.new_name.text = list[position].title

        holder.itemView.card_new.setOnClickListener {
            onClickListener.clickM(list[position])
            Case.id = list[position].id
            item = list[position]
        }
    }
    class NewsView(view: View): RecyclerView.ViewHolder(view)
}