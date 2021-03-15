package com.abel.avengerit.ui.characters_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abel.avengerit.R
import com.abel.avengerit.model.character.Result
import com.abel.avengerit.ui.base.BaseAdapter
import com.abel.avengerit.utils.OnLoadMoreListener
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide

class CharacterAdapter(
    private val list: ArrayList<Result?>,
    recyclerView: RecyclerView,
    private val onClickListener: OnClickListener?,
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
                R.layout.item_character, parent, false
            )
            CharacterViewHolder(v)
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
        if (holder is CharacterViewHolder) {
            val character = list[position]
            holder.bind(character, onClickListener)
        }
    }

    //todo Holder
    class CharacterViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var textViewTitle: TextView = v.findViewById(R.id.textViewNameCharacter)
        var textViewDescripcion: TextView = v.findViewById(R.id.textViewDescripcionCharacter)
        var imageViewCharacter: ImageView = v.findViewById(R.id.imageViewAvatarCharacter)

        fun bind(
            character: Result?,
            onClickListener: OnClickListener?
        ) {
            textViewTitle.text = character?.name
            textViewTitle.transitionName = character?.name
            textViewDescripcion.text = if (character?.description.isNullOrEmpty()) "Sin descripcion" else character?.description

            Glide.with(itemView.context)
                .load("${character?.thumbnail?.path}.${character?.thumbnail?.extension}")
                .placeholder(R.drawable.ic_marvel)
                .error(R.drawable.ic_marvel)
                .override(200, 200)
                .centerCrop()
                .into(imageViewCharacter)

            itemView.setOnClickListener {
                if (character != null) {
                    onClickListener?.onClick(character)
                }
            }
        }

    }

    class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var progressBar: LottieAnimationView =
            v.findViewById<View>(R.id.progressBarAnimated) as LottieAnimationView
    }

    class OnClickListener(val clickListener: (Result) -> Unit) {
        fun onClick(
            character: Result,
        ) = clickListener(character)
    }
}