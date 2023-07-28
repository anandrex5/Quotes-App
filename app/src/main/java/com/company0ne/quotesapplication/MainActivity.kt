package com.company0ne.quotesapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.company0ne.quotesapplication.Api.ApiClient
import com.company0ne.quotesapplication.Api.ApiInterface
import retrofit2.Call
import com.google.gson.JsonArray
import com.squareup.picasso.Picasso
import org.json.JSONArray
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var arrayList: ArrayList<HomeViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arrayList = ArrayList()

        retroFitArray()
    }

    private fun retroFitArray() {
        val apiInterface = ApiClient.client.create(ApiInterface::class.java)
        val call: Call<JsonArray> = apiInterface.getData()
        call.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.isSuccessful) {
                    val array = JSONArray(response.body().toString())
                    Log.d("jsonArrayData", array.toString())
                    //data in the ArrayFormat
                    for (i in 0 until array.length()) {
                        val model = HomeViewModel()
                        val objects = array.getJSONObject(i)

                        model.text = objects.getString("text")
                        model.author = objects.getString("author")
                        arrayList.add(model)
                    }
                    buildRecycler()

                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }

    private fun buildRecycler() {
        val recycler: RecyclerView = findViewById(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = DataAdapter(arrayList)

    }

    private class DataAdapter(var list: ArrayList<HomeViewModel>) :
        RecyclerView.Adapter<DataAdapter.DataViewHolder>() {
        class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


            val textViewAlbum: TextView = itemView.findViewById(R.id.text)
            val textViewAuthor: TextView = itemView.findViewById(R.id.textAuthor)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.data_item, parent, false)
            return DataViewHolder(view)

        }

        override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
            holder.textViewAlbum.text = list[position].text
            holder.textViewAuthor.text = list[position].author
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }
}
