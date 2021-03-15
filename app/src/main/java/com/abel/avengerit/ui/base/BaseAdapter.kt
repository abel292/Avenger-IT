package com.abel.avengerit.ui.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abel.avengerit.utils.OnLoadMoreListener

abstract class BaseAdapter(recyclerView: RecyclerView) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    val VIEW_ITEM = 1
    val VIEW_PROG = 0
    // La cantidad mínima de elementos que debe tener debajo de su posición de desplazamiento actual antes de cargar mas
    val visibleThreshold = 5
    var lastVisibleItem = 0
    var totalItemCount = 0
    var loading = false
    var onLoadMoreListener: OnLoadMoreListener? = null

    init {

        if (recyclerView.layoutManager is LinearLayoutManager) {
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int, ) {
                    super.onScrolled(recyclerView, dx, dy)
                    totalItemCount = linearLayoutManager!!.itemCount
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()

                    if (!loading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                        onLoadMoreListener?.onLoadMore()
                        loading = true
                    }
                }
            })
        }
    }

    @JvmName("setOnLoadMoreListener1")
    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    fun setLoaded() {
        loading = false
    }
}