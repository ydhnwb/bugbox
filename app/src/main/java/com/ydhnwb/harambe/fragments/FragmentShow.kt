package com.ydhnwb.harambe.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ydhnwb.harambe.R
import com.ydhnwb.harambe.adapters.BugAdapter
import com.ydhnwb.harambe.models.BugModel
import com.ydhnwb.harambe.networks.WrappedListResponse
import com.ydhnwb.harambe.utilities.ApiUtils
import kotlinx.android.synthetic.main.etc_empty_data.view.*
import kotlinx.android.synthetic.main.etc_empty_trouble.view.*
import kotlinx.android.synthetic.main.fragment_show.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentShow : Fragment() {

    private var bugService = ApiUtils.bugService()
    private var bugList = mutableListOf<BugModel>()
    private val TAG = "FragShow"
    private var request : Call<WrappedListResponse<BugModel>>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = inflater.inflate(R.layout.fragment_show, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.rvBug.layoutManager = LinearLayoutManager(context)
    }

    override fun onResume() {
        super.onResume()
        bugList.clear()
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(request != null){
            request?.cancel()
        }
    }

    private fun loadData(){
        request = bugService.all()
        view!!.loading.visibility = View.VISIBLE
        view!!.empty_view_trouble.visibility = View.INVISIBLE
        view!!.empty_view_data.visibility = View.INVISIBLE
        request!!.enqueue(object : Callback<WrappedListResponse<BugModel>>{
            override fun onFailure(call: Call<WrappedListResponse<BugModel>>, t: Throwable) {
                Log.d(TAG, t.message)
                if(view != null){
                    view?.loading?.visibility = View.INVISIBLE
                    view!!.empty_view_trouble.visibility = View.VISIBLE
                    view!!.empty_view_data.visibility = View.INVISIBLE
                }
            }

            override fun onResponse(call: Call<WrappedListResponse<BugModel>>, response: Response<WrappedListResponse<BugModel>>) {
                if(response.isSuccessful){
                    val r = response.body()
                    if(r != null){
                        if(r.status){
                            Log.d(TAG, r.message)
                            view!!.empty_view_trouble.visibility = View.INVISIBLE
                            view!!.empty_view_data.visibility = View.INVISIBLE
                            bugList = r.data as MutableList<BugModel>
                            if(bugList.isEmpty()){
                                view!!.empty_view_data.visibility = View.VISIBLE
                                if(view!!.rvBug.adapter != null){
                                    view!!.rvBug.adapter?.notifyDataSetChanged()
                                }
                            }else{ view?.rvBug?.adapter = BugAdapter(bugList, context!!) }
                        }else{
                            if(view!!.rvBug.adapter != null){
                                view!!.rvBug.adapter?.notifyDataSetChanged()
                            }
                            view!!.empty_view_trouble.visibility = View.INVISIBLE
                            view!!.empty_view_data.visibility = View.VISIBLE
                        }
                    }
                }else{
                    view!!.empty_view_trouble.visibility = View.VISIBLE
                    view!!.empty_view_data.visibility = View.INVISIBLE
                    Log.d(TAG, "Response is not success")
                }
                view!!.loading.visibility = View.INVISIBLE
            }
        })
    }
}