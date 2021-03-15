package com.abel.avengerit.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import com.abel.avengerit.R
import com.abel.avengerit.model.event.Event
import com.abel.avengerit.ui.base.BaseFragmentList
import com.abel.avengerit.utils.OnLoadMoreListener
import com.abel.avengerit.utils.showToast
import com.abel.avengerit.view_models.MarvelViewModel
import com.abel.avengerit.view_models.Resourse
import kotlinx.android.synthetic.main.fragment_events.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventsFragment : BaseFragmentList<Event>(), OnLoadMoreListener {

    private val viewModel: MarvelViewModel by viewModel()
    lateinit var mAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservables()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        if (!loadedList) {
            itemLoadeds = ArrayList()
            viewModel.getEvents(itemLoadeds.size)
            loadRecyclerView()
        } else {
            loadRecyclerView()
        }
    }

    private fun initObservables() {
        viewModel.eventsLive.observe(this, {
            when (it.responseAction) {
                Resourse.SUCCESS -> {
                    insertMoreEvents(it.resourceObject)
                }
                Resourse.BAD -> context?.showToast(getString(R.string.algo_malio_sal))
            }
        })
    }

    private fun loadRecyclerView() {
        recyclerViewEvent.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this.requireContext())
        recyclerViewEvent.layoutManager = mLayoutManager
        mAdapter = EventAdapter(itemLoadeds, recyclerViewEvent)
        recyclerViewEvent.adapter = mAdapter

        // Para cuando el usuario pulsa el botón Atrás
        postponeEnterTransition()
        recyclerViewEvent.doOnPreDraw {
            startPostponedEnterTransition()
        }
        mAdapter.setOnLoadMoreListener(this)
    }

    override fun onLoadMore() {
        itemLoadeds.add(null)
        mAdapter.notifyItemInserted(itemLoadeds.size - 1)

        //pedimos 15 eventos mas a la api
        viewModel.getEvents(itemLoadeds.size)

    }

    private fun insertMoreEvents(listMore: List<Event>?) {
        if (itemLoadeds.isNotEmpty()) {
            itemLoadeds.removeAt(itemLoadeds.size - 1)
        }
        mAdapter.notifyItemRemoved(itemLoadeds.size)

        listMore?.forEach {
            itemLoadeds.add(it)
            mAdapter.notifyItemInserted(itemLoadeds.size)
        }

        if (itemLoadeds.size >= mAdapter.itemCount) {
            mAdapter.setLoaded()
        }

        if (itemLoadeds.isEmpty()) {
            recyclerViewEvent.visibility = View.GONE
        } else {
            loadedList = true
            recyclerViewEvent.visibility = View.VISIBLE
        }
    }


}