package com.ydhnwb.harambe.adapters

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ydhnwb.harambe.DetailActivity
import com.ydhnwb.harambe.R
import com.ydhnwb.harambe.models.BugModel
import kotlinx.android.synthetic.main.list_item_main.view.*

class BugAdapter(private val mList : MutableList<BugModel>, private val mContext : Context) : RecyclerView.Adapter<BugAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) = ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_item_main, p0, false))

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) = p0.bindData(mList[p1], mContext)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bindData(model : BugModel, context: Context){
            itemView.setOnClickListener {
                val i = Intent(context, DetailActivity::class.java)
                i.putExtra("ID", model.id)
                i.putExtra("NAME", model.name)
                i.putExtra("DESC", model.description)
                i.putExtra("PHOTO", model.photo)
                context.startActivity(i)
            }
            itemView.descBug.text = model.description
            itemView.titleBug.text = model.name
            Glide.with(context).load("https://bugscollector.herokuapp.com/"+model.photo).into(itemView.imageBug)
        }
    }
}