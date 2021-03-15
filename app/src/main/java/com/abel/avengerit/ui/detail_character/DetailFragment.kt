package com.abel.avengerit.ui.detail_character

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import com.abel.avengerit.R
import com.abel.avengerit.model.character.ItemComic
import com.abel.avengerit.model.character.Result
import com.abel.avengerit.ui.base.BaseFragmentList
import com.abel.avengerit.utils.OnLoadMoreListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : BaseFragmentList<ItemComic>() {

    lateinit var comic: Result
    lateinit var mAdapter: ComicsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = requireArguments()
        comic = DetailFragmentArgs.fromBundle(args).argCharacter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDataFirst()
        populateView()
    }

    private fun populateView() {
        showButtonExit()
        Glide.with(imageViewAvatarCharacterDetail.context)
            .load("${comic.thumbnail.path}.${comic.thumbnail.extension}")
            .placeholder(R.drawable.ic_marvel)
            .error(R.drawable.ic_marvel)
            .override(700, 700)
            .centerCrop()
            .into(imageViewAvatarCharacterDetail)
        val description = if (comic.description.isEmpty())
            getString(R.string.sin_descripcion) else comic.description
        textViewDescriptionDetail.text = description

        loadRecyclerView()

        imageViewAvatarCharacterDetail.requestFocus()
    }

    private fun loadRecyclerView() {
        recyclerViewComics.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this.requireContext())
        recyclerViewComics.layoutManager = mLayoutManager
        mAdapter = ComicsAdapter(itemLoadeds, recyclerViewComics)
        recyclerViewComics.adapter = mAdapter

        // Cuando el usuario pulsa el botón Atrás, la transición se realiza hacia atrás.
        postponeEnterTransition()
        recyclerViewComics.doOnPreDraw {
            startPostponedEnterTransition()
        }
        if (itemLoadeds.isEmpty()) {
            recyclerViewComics.visibility = View.GONE
            textViewWithoutComic.visibility = View.VISIBLE

        } else {
            loadedList = true
            recyclerViewComics.visibility = View.VISIBLE
            textViewWithoutComic.visibility = View.GONE
        }
        setListenerAdapter()
    }

    private fun setListenerAdapter() {
        mAdapter.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                itemLoadeds.add(null)
                mAdapter.notifyItemInserted(itemLoadeds.size - 1)
                handler?.postDelayed(Runnable {
                    itemLoadeds.removeAt(itemLoadeds.size - 1)
                    mAdapter.notifyItemRemoved(itemLoadeds.size)

                    val start: Int = itemLoadeds.size
                    var end = start + more

                    end = if (end > allItems.size) {
                        allItems.size - 1
                    } else {
                        start + more
                    }
                    for (i in start + 1..end) {
                        if (allItems.size > i) {
                            itemLoadeds.add(allItems[i])
                            mAdapter.notifyItemInserted(itemLoadeds.size)
                        }
                    }
                    if (allItems.size > mAdapter.itemCount) {
                        mAdapter.setLoaded()
                    }
                }, 2000)
            }
        })
    }

    private fun loadDataFirst() {
        itemLoadeds = ArrayList()
        allItems = ArrayList()
        comic.comics.items.forEach { character ->
            allItems.add(character)
        }
        handler = Handler()
        //Cargamos los primero remitos y si hay menos que "cantFirstLoad" cargamos todos
        if (allItems.size <= cantFirstLoad) {
            itemLoadeds.addAll(allItems)
        } else {
            for (i in 0..cantFirstLoad) {
                itemLoadeds.add(allItems[i])
            }
        }
    }

    private fun checkLoadignActive() {
        mAdapter.setLoaded()
    }
}