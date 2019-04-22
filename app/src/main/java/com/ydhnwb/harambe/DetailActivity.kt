package com.ydhnwb.harambe

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ydhnwb.harambe.models.BugModel
import com.ydhnwb.harambe.networks.WrappedResponse
import com.ydhnwb.harambe.utilities.ApiUtils
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private var bug = BugModel()
    private var bugService = ApiUtils.bugService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { finish() }
        getIntentData()
        fetchData()
    }

    private fun getIntentData(){
        bug.id = intent.getIntExtra("ID", 0)
        bug.name = intent.getStringExtra("NAME")
        bug.description = intent.getStringExtra("DESC")
        bug.photo = intent.getStringExtra("PHOTO")
    }

    private fun fetchData(){
        detail_name.text = bug.name
        detail_desc.text = bug.description
        Glide.with(applicationContext).load("https://bugscollector.herokuapp.com/${bug.photo}").into(detail_image)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId){
            R.id.action_delete -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete")
                builder.setMessage("Are you sure to delete this item?")
                builder.setPositiveButton("DELETE") { dialog, which ->
                    val req = bugService.delete(bug.id.toString())
                    req.enqueue(object : Callback<WrappedResponse<BugModel>>{
                        override fun onFailure(call: Call<WrappedResponse<BugModel>>, t: Throwable) { Toast.makeText(this@DetailActivity, t.message, Toast.LENGTH_LONG).show() }
                        override fun onResponse(call: Call<WrappedResponse<BugModel>>, response: Response<WrappedResponse<BugModel>>) {
                            if(response.isSuccessful){
                                val b = response.body()
                                if(b != null && b.status){
                                    Toast.makeText(this@DetailActivity, "Successfully deleted", Toast.LENGTH_LONG).show()
                                    finish()
                                }
                            }else{
                                Toast.makeText(this@DetailActivity, "Something went wrong", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                    finish()
                }
                builder.setNegativeButton("CANCEL", { dialog, which -> dialog.cancel() })
                builder.setCancelable(false)
                builder.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}