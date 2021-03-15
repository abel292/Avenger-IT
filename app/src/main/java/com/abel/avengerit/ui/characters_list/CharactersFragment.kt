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
import com.abel.avengerit.view_models.Resourse.Companion.SUCCESS
import kotlinx.android.synthetic.main.fragment_characters.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersFragment : BaseFragmentList<Result>(), OnLoadMoreListener {

    private val viewModel: MarvelViewModel by viewModel()
    lateinit var mAdapter: CharacterAdapter
    private val itemListener = CharacterAdapter.OnClickListener { character ->
        val direction: NavDirections = CharactersFragmentDirections.actionCharactersFragmentToDetailFragment(character)
        findNavController().navigate(direction)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_characters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservables()
    }

    private fun initObservables() {
        viewModel.resourceCharacterLive.observe(this, {
            when (it.responseAction) {
                SUCCESS -> {
                    it.resourceObject?.forEach { character ->
                        allItems.add(character)
                    }
                    insertMoreCharacters(it.resourceObject)
                }
                BAD -> context?.showToast(getString(R.string.algo_malio_sal))
            }
        })
    }

    private fun init() {
        if (!loadedList) {
            itemLoadeds = ArrayList()
            allItems = ArrayList()
            viewModel.getCharacters(allItems.size)
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
        mAdapter.setOnLoadMoreListener(this)

    }

    override fun onLoadMore() {
        itemLoadeds.add(null)
        mAdapter.notifyItemInserted(itemLoadeds.size - 1)

        //pedimos 15 registros mas a la api
        viewModel.getCharacters(allItems.size)

    }

    private fun insertMoreCharacters(listMore: List<Result>?) {
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
            recyclerViewCharacter.visibility = View.GONE
            textViewWithoutCharacter.visibility = View.VISIBLE

        } else {
            loadedList = true
            recyclerViewCharacter.visibility = View.VISIBLE
            textViewWithoutCharacter.visibility = View.GONE
        }
    }


}