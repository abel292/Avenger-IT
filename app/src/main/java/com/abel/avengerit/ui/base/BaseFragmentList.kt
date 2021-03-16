package com.abel.avengerit.ui.base

import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.abel.avengerit.ui.main.MainActivity

abstract class BaseFragmentList<T> : Fragment() {

    lateinit var itemLoadeds: ArrayList<T?>
    var mLayoutManager: LinearLayoutManager? = null
    lateinit var allItems: ArrayList<T?>
    var loadedList: Boolean = false
    var handler: Handler? = null
    val cantFirstLoad = 15
    var attempts = 0
    val more = 15

    fun setButtomModeBack(modeBack: Boolean) {
        activity?.let {
            val main = it as MainActivity
            main.setActionButtonToolbar(modeBack)
        }
    }

}