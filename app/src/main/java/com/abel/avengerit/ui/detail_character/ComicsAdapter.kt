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
import com.abel.avengerit.utils.OnLoadMoreListener
import com.airbnb.lottie.LottieAnimationView

class ComicsAdapter(
    private val list: ArrayList<ItemComic?>,
    recyclerView: RecyclerView
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0

    // La cantidad mínima de elementos que debe tener debajo de su posición de desplazamiento actual antes de cargar mas
    private val visibleThreshold = 5
    private var lastVisibleItem = 0
    private var totalItemCount = 0
    private var loading = false
    private lateinit var onLoadMoreListener: OnLoadMoreListener

    init {

        if (recyclerView.layoutManager is LinearLayoutManager) {
            val linearLayoutManager = recyclerView
                .layoutManager as LinearLayoutManager?
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int, dy: Int,
                ) {
                    super.onScrolled(recyclerView, dx, dy)
                    totalItemCount = linearLayoutManager!!.itemCount
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                    if (!loading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                        onLoadMoreListener.onLoadMore()
                        loading = true
                    }
                }
            })
        }
    }

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

    fun setLoaded() {
        loading = false
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ComicViewHolder) {
            val itemComic = list[position]
            holder.bind(itemComic)
        }
    }

    //todo Holder
    class ComicViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var textViewTitle: TextView = v.findViewById(R.id.textViewNameComic)
        var textViewDescripcion: TextView = v.findViewById(R.id.textViewDateComic)

        fun bind(
            comics: ItemComic?,
        ) {
            textViewTitle.text = comics?.name
            //textViewDescripcion.text = comics?.resourceURI
        }
    }

    class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var progressBar: LottieAnimationView =
            v.findViewById<View>(R.id.progressBarAnimated) as LottieAnimationView
    }

}