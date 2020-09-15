package vlados.dudos.vkino.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.plot_view.view.*
import vlados.dudos.vkino.CastActivity
import vlados.dudos.vkino.R
import vlados.dudos.vkino.models.Cast

class VpInfoAdapter(val listC: List<Cast>, val context: Context, val overview: String) :
    RecyclerView.Adapter<VpInfoAdapter.InfoView>(), CastAdapter.OnClickListener {

    override fun clickA(data: Cast) {
        context.startActivity(Intent(context, CastActivity::class.java))
    }

    val list = listOf(0, 1)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoView {
        return InfoView(
            LayoutInflater.from(parent.context).inflate(R.layout.plot_view, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: InfoView, position: Int) {
        if (list[position] == 0) {
            holder.itemView.plot_layout.visibility = View.VISIBLE
            holder.itemView.rv_cast.visibility = View.GONE
            holder.itemView.plot.text = overview
        } else if (list[position] == 1) {
            holder.itemView.plot_layout.visibility = View.GONE
            holder.itemView.rv_cast.visibility = View.VISIBLE

            holder.itemView.rv_cast.layoutManager = GridLayoutManager(context, 2)
            holder.itemView.rv_cast.adapter = CastAdapter(listC, this)
        }
    }

    class InfoView(view: View) : RecyclerView.ViewHolder(view)
}