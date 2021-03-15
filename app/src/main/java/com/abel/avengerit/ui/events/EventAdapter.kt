package com.abel.avengerit.ui.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abel.avengerit.R
import com.abel.avengerit.model.character.ItemComic
import com.abel.avengerit.model.event.Event
import com.abel.avengerit.ui.base.BaseAdapter
import com.abel.avengerit.ui.detail_character.ComicsAdapter
import com.abel.avengerit.utils.OnClickItemListener
import com.abel.avengerit.utils.changeFormatDate
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide


class EventAdapter(
    private val list: ArrayList<Event?>,
    recyclerView: RecyclerView,
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
                R.layout.item_event, parent, false
            )
            EventViewHolder(v)
        } else {
            val v: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_loading_recycler, parent, false
            )
            ProgressViewHolder(v)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EventViewHolder) {
            val character = list[position]
            holder.bind(character)
        }
    }

    //todo Holder
    class EventViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var textViewTitleEventItem: TextView = view.findViewById(R.id.textViewTitleEventItem)
        var textViewDate1: TextView = view.findViewById(R.id.textViewDate1)
        var textViewDate2: TextView = view.findViewById(R.id.textViewDate2)
        var textViewTitleComicDisc: TextView = view.findViewById(R.id.textViewTitleComicDisc)
        var recyclerViewComicsEvent: RecyclerView = view.findViewById(R.id.recyclerViewComicsEvent)
        var imageViewExpandle: ImageView = view.findViewById(R.id.imageViewExpandle)
        var imageViewPortEvent: ImageView = view.findViewById(R.id.imageViewPortEvent)

        fun bind(
            event: Event?
        ) {
            textViewTitleEventItem.text = event?.title
            textViewDate1.text = event?.start?.let { changeFormatDate(it) } ?: ""
            textViewDate2.text = event?.end?.let { changeFormatDate(it) } ?: ""

            Glide.with(itemView.context)
                .load("${event?.thumbnail?.path}.${event?.thumbnail?.extension}")
                .placeholder(R.drawable.ic_marvel)
                .error(R.drawable.ic_marvel)
                .override(200, 200)
                .centerCrop()
                .into(imageViewPortEvent)

            loadListComics(event)
            view.setOnClickListener {
                modeExpandle(recyclerViewComicsEvent.visibility != View.VISIBLE)
            }

        }

        private fun modeExpandle(boolean: Boolean) {
            if (boolean) {
                textViewTitleComicDisc.visibility = View.VISIBLE
                recyclerViewComicsEvent.visibility = View.VISIBLE
                imageViewExpandle.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_flecha_up))

            } else {
                imageViewExpandle.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_flecha_down))
                textViewTitleComicDisc.visibility = View.GONE
                recyclerViewComicsEvent.visibility = View.GONE

            }
        }

        private fun loadListComics(event: Event?) {
            recyclerViewComicsEvent.setHasFixedSize(true)
            val mLayoutManager = LinearLayoutManager(itemView.context)
            recyclerViewComicsEvent.layoutManager = mLayoutManager
            val listComic = ArrayList<ItemComic?>()
            if (event != null) {
                listComic.addAll(event.comics.items)
            }
            val mAdapter = ComicsAdapter(listComic, recyclerViewComicsEvent)
            recyclerViewComicsEvent.adapter = mAdapter
        }

    }

    class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var progressBar: LottieAnimationView =
            v.findViewById<View>(R.id.progressBarAnimated) as LottieAnimationView
    }

    class OnClickListener(val clickListener: (Event) -> Unit) {
        fun onClick(
            character: Event,
        ) = clickListener(character)
    }
}