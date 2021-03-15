package com.abel.avengerit.ui.detail_character

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abel.avengerit.R
import com.abel.avengerit.model.character.ItemComic
import com.abel.avengerit.ui.base.BaseAdapter
import com.abel.avengerit.utils.OnLoadMoreListener
import com.abel.avengerit.utils.getYearComic
import com.airbnb.lottie.LottieAnimationView

class ComicsAdapter(
    private val list: ArrayList<ItemComic?>,
    recyclerView: RecyclerView
) : BaseAdapter(recyclerView) {

    override fun getItemViewType(position: Int): Int {
        return if (list[position] != null) VIEW_ITEM else VIEW_PROG
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return if (viewType == VIEW_ITEM) {
            val v: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_comics, parent, false
            )
            ComicViewHolder(v)
        } else {
            val v: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_loading_recycler, parent, false
            )
            ProgressViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ComicViewHolder) {
            val itemComic = list[position]
            holder.bind(itemComic)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    //todo Holder
    class ComicViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var textViewTitle: TextView = v.findViewById(R.id.textViewNameComic)
        var textViewDescripcion: TextView = v.findViewById(R.id.textViewDateComic)

        fun bind(
            comics: ItemComic?,
        ) {
            val itemComic = comics?.name?.getYearComic()
            textViewTitle.text = itemComic?.title + itemComic?.number?:""
            textViewDescripcion.text = itemComic?.year
        }
    }

    class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    }

}