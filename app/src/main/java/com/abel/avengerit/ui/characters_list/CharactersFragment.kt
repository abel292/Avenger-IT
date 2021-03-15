package com.abel.avengerit.ui.characters_list

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abel.avengerit.R
import com.abel.avengerit.model.character.Result
import com.abel.avengerit.ui.base.BaseFragmentList
import com.abel.avengerit.utils.OnLoadMoreListener
import com.abel.avengerit.utils.showToast
import com.abel.avengerit.view_models.MarvelViewModel
import com.abel.avengerit.view_models.Resourse.Companion.BAD
import com.abel.avengerit.view_models.Resourse.Companion.LOGIN_SUCCESS
import kotlinx.android.synthetic.main.fragment_characters.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersFragment : BaseFragmentList<Result>() {

    private val viewModel: MarvelViewModel by viewModel()
    lateinit var mAdapter: CharacterAdapter
    private val itemListener = CharacterAdapter.OnClickListener { character ->
        val direction: NavDirections =
            CharactersFragmentDirections.actionCharactersFragmentToDetailFragment(character)
        findNavController().navigate(direction)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_characters, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservables()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCharacters()

    }

    private fun initObservables() {
        viewModel.resourceCharacterLive.observe(this, {
            when (it.responseAction) {
                LOGIN_SUCCESS -> init(it.user)
                BAD -> context?.showToast("Algo salio mal")
            }
            if (it.loading) {
                progressBarAnimated.visibility = View.VISIBLE
            } else {
                progressBarAnimated.visibility = View.GONE
            }
        })
    }

    private fun init(list: List<Result>?) {
        if (!loadedList) {
            itemLoadeds = ArrayList()
            allItems = ArrayList()
            list?.forEach { character ->
                allItems.add(character)
            }
            handler = Handler()
            loadDataFirst()
            loadRecyclerView()

        } else {
            loadRecyclerView()
        }
    }

    private fun loadDataFirst() {
        //Cargamos los primero remitos y si hay menos que "cantFirstLoad" cargamos todos
        if (allItems.size <= cantFirstLoad) {
            itemLoadeds.addAll(allItems)
        } else {
            for (i in 0..cantFirstLoad) {
                itemLoadeds.add(allItems[i])
            }
        }
    }


    private fun loadRecyclerView() {
        recyclerViewCharacter.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this.requireContext())
        recyclerViewCharacter.layoutManager = mLayoutManager
        mAdapter = CharacterAdapter(itemLoadeds, recyclerViewCharacter, itemListener)
        recyclerViewCharacter.adapter = mAdapter

        // Cuando el usuario pulsa el botón Atrás, la transición se realiza hacia atrás.
        postponeEnterTransition()
        recyclerViewCharacter.doOnPreDraw {
            startPostponedEnterTransition()
        }
        if (itemLoadeds.isEmpty()) {
            recyclerViewCharacter.visibility = View.GONE
            textViewWithoutCharacter.visibility = View.VISIBLE

            //probamos nuevamente
            if (attempts < 2) {
                Handler().postDelayed({ viewModel.getCharacters() }, 560L)
                attempts++
            }
        } else {
            loadedList = true
            recyclerViewCharacter.visibility = View.VISIBLE
            textViewWithoutCharacter.visibility = View.GONE
        }
        setListenerAdapter()
    }

    private fun setListenerAdapter() {
        mAdapter.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                itemLoadeds.add(null)
                mAdapter.notifyItemInserted(itemLoadeds.size - 1)
                handler?.postDelayed(Runnable {
                    //   remove progress item
                    itemLoadeds.removeAt(itemLoadeds.size - 1)
                    mAdapter.notifyItemRemoved(itemLoadeds.size)

                    //add items one by one
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
                    //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                }, 2000)
            }
        })
    }


}