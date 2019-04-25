package com.ydhnwb.harambe.fragments

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ydhnwb.harambe.R
import com.ydhnwb.harambe.adapters.BugAdapter
import com.ydhnwb.harambe.models.BugModel
import com.ydhnwb.harambe.networks.WrappedListResponse
import com.ydhnwb.harambe.networks.WrappedResponse
import com.ydhnwb.harambe.utilities.ApiUtils
import kotlinx.android.synthetic.main.bottom_bar_search.view.*
import kotlinx.android.synthetic.main.etc_empty_data.view.*
import kotlinx.android.synthetic.main.etc_empty_trouble.view.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentSearch : Fragment() {

    private val bugService = ApiUtils.bugService()
    private var bugList = mutableListOf<BugModel>()
    private val TAG = "FragSearch"
    private var request : Call<WrappedListResponse<BugModel>>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.rvBugSearch.layoutManager = LinearLayoutManager(activity)
        view.et_search.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    if(!view.et_search.text.toString().trim().isEmpty()){
                        runFilter(view.et_search.text.toString().trim())
                    }
                    return true
                }
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        bugList.clear()
        if(!view!!.et_search.text.toString().trim().isEmpty()){
            runFilter(view!!.et_search.text.toString().trim())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(request != null){
            request!!.cancel()
        }
    }

    private fun runFilter(query : String){
        bugList.clear()
        request = bugService.search(query)
        view!!.loading.visibility = View.VISIBLE
        view!!.empty_view_data.visibility = View.INVISIBLE
        view!!.empty_view_trouble.visibility = View.INVISIBLE
        request!!.enqueue(object : Callback<WrappedListResponse<BugModel>>{
            override fun onFailure(call: Call<WrappedListResponse<BugModel>>, t: Throwable) {
                Log.d(TAG, t.message)
                if(view != null){
                    view!!.loading.visibility = View.INVISIBLE
                    view!!.empty_view_trouble.visibility = View.VISIBLE
                    view!!.empty_view_data.visibility = View.INVISIBLE
                }
            }

            override fun onResponse(call: Call<WrappedListResponse<BugModel>>, response: Response<WrappedListResponse<BugModel>>) {
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        if(body.status){
                            view!!.empty_view_data.visibility = View.INVISIBLE
                            view!!.empty_view_trouble.visibility = View.INVISIBLE
                            bugList = body.data as MutableList
                            view!!.rvBugSearch.adapter = BugAdapter(bugList, context!!)
                        }else{
                            if(view!!.rvBugSearch.adapter != null){
                                view!!.rvBugSearch.adapter?.notifyDataSetChanged()
                            }
                            view!!.empty_view_data.visibility = View.VISIBLE
                            view!!.empty_view_trouble.visibility = View.INVISIBLE
                            Toast.makeText(context, body.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    view!!.empty_view_data.visibility = View.INVISIBLE
                    view!!.empty_view_trouble.visibility = View.VISIBLE
                    Log.d(TAG, "Something went wrong")
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
                }
                view!!.loading.visibility = View.INVISIBLE
            }
        })
    }
}