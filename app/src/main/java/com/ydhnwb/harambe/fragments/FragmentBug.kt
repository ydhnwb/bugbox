package com.ydhnwb.harambe.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.ydhnwb.harambe.R
import com.ydhnwb.harambe.models.BugModel
import com.ydhnwb.harambe.networks.WrappedResponse
import com.ydhnwb.harambe.utilities.ApiUtils
import kotlinx.android.synthetic.main.fragment_new.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.URI


class FragmentBug : Fragment() {
    private val REQ_CODE_IMAGE = 12
    private var bug = BugModel()
    private val TAG = "FragBug"
    private var bugService = ApiUtils.bugService()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = inflater.inflate(R.layout.fragment_new, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.btn_add_photo.setOnClickListener { Pix.start(this@FragmentBug, Options.init().setRequestCode(REQ_CODE_IMAGE)) }

        view.btnSaveBug.setOnClickListener {
            bug.name = view.etTitleBug.text.toString().trim()
            bug.description = view.etDescBug.text.toString().trim()
            if(bug.name != null && bug.description != null && bug.photo != null){
                view.progress_upload.visibility = View.VISIBLE
                view.btnSaveBug.isEnabled = false

                val file = File(bug.photo)
                val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
                val photo : MultipartBody.Part = MultipartBody.Part.createFormData("photo",file.name,requestBodyForFile)
                val name : RequestBody = RequestBody.create(MultipartBody.FORM, bug.name.toString())
                val desc : RequestBody = RequestBody.create(MultipartBody.FORM, bug.description.toString())
                val req = bugService.new(photo, name, desc)
                req.enqueue(object : Callback<WrappedResponse<BugModel>>{
                    override fun onFailure(call: Call<WrappedResponse<BugModel>>, t: Throwable) {
                        view.btnSaveBug.isEnabled = true
                        view.progress_upload.visibility = View.INVISIBLE
                        Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                        Log.d(TAG, "onFailure"+t.message)
                    }

                    override fun onResponse(call: Call<WrappedResponse<BugModel>>, response: Response<WrappedResponse<BugModel>>) {
                        if(response.isSuccessful){
                            val b = response.body()
                            if(b != null){
                                view.etTitleBug.text.clear()
                                view.etDescBug.text.clear()
                                bug.name = null
                                bug.description = null
                                bug.photo = null
                                Glide.with(context!!).load(R.drawable.ic_photo_camera_black_24dp).into(view.image_preview)
                                Toast.makeText(context, "Successfullly uploaded", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show()
                            Log.d(TAG, response.message())
                        }
                        view.btnSaveBug.isEnabled = true
                        view.progress_upload.visibility = View.INVISIBLE
                    }
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQ_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null){
            val returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            bug.photo = returnValue[0]
            Toast.makeText(context!!, bug.photo, Toast.LENGTH_LONG).show()
            println(Log.d(TAG, bug.photo))
            Glide.with(context!!).load(bug.photo).into(view!!.image_preview)
        }
    }
}