package com.example.openinapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openinapp.dataclass.MainData
import com.example.openinapp.dataclass.TopLink
import com.example.openinapp.getretrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ToplinkFrag : Fragment() {
    private var showFullList = false
    private lateinit var viewMoreButton: TextView
    private val authToken:String="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: RecycleViewAdapterForTopLink
    //listOf() create for immutable list
    private var itemList: List<MainData> = listOf()
    private var toList: List<TopLink> = listOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.toplink_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewMoreButton = view.findViewById(R.id.viewMoreButton)
        recyclerView = view.findViewById(R.id.recycleView_one)
        recyclerView.layoutManager = LinearLayoutManager(context)
        itemAdapter = RecycleViewAdapterForTopLink(toList)
        recyclerView.adapter = itemAdapter
        viewMoreButton.setOnClickListener {
            toggleItemList()
        }
        callApi()
    }

    private fun callApi() {
        val apiService = RetrofitClient.apiService
        apiService.getDetails(authToken).enqueue(object : Callback<MainData> {
            override fun onResponse(call: Call<MainData>, response: Response<MainData>) {
                if (response.isSuccessful) {
                    val allData = response.body()
                    allData?.let {
                        itemList = listOf(it)
                        //itemAdapter.updateData(itemList[0].data.recent_links)
                        updateDisplayedList()
                    }


                }else{
                    Log.e("ToplinkFrag", "Error: ${response.code()} - ${response.message()}")
                    Toast.makeText(context, "Error: ${response.code()} - ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MainData>, t: Throwable) {
                Log.e("ToplinkFrag", "Failure: ${t.message}", t)
                Toast.makeText(context, "Some error happened: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun updateDisplayedList() {
        if (showFullList) {
            itemAdapter.updateData(itemList[0].data.recent_links)
        } else {
            itemAdapter.updateData(itemList[0].data.recent_links.take(2))
        }
    }
    private fun toggleItemList() {
        showFullList = !showFullList
        updateDisplayedList()
        viewMoreButton.text = if (showFullList) "View Less" else "View All Links"
    }

}

