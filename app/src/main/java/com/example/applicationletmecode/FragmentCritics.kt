package com.example.applicationletmecode

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.applicationletmecode.critics.CriticsAdapter
import com.example.applicationletmecode.critics.detail_critics.DetailCritics
import com.example.applicationletmecode.critics.apis.ApiServiceCritics
import com.example.applicationletmecode.critics.model.CriticsData
import com.example.applicationletmecode.critics.model.ResultCritics
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentCritics : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var criticsList: ArrayList<ResultCritics>
    private lateinit var criticsAdapter: CriticsAdapter
    private lateinit var manager: GridLayoutManager
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchBar: SearchView

    companion object {
        fun newInstance(): FragmentCritics {
            return FragmentCritics()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_two, container, false)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manager = GridLayoutManager(context, 2)
        swipeRefreshLayout = view.findViewById(R.id.container)
        getCritics(view)

        swipeRefreshLayout.setOnRefreshListener {

            searchBar.setQuery("", false);

            swipeRefreshLayout.isRefreshing = false
            getCritics(view)
            criticsAdapter.notifyDataSetChanged()

        }

        searchBar = view.findViewById(R.id.search_bar)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                criticsAdapter.getFilter().filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                criticsAdapter.getFilter().filter(newText);
                return true
            }

        })

    }

    private fun getCritics(view: View) {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val catApi: ApiServiceCritics = retrofit.create(ApiServiceCritics::class.java)

        catApi.getAllData().enqueue(object: Callback<CriticsData> {
            override fun onResponse(
                call: Call<CriticsData>,
                response: Response<CriticsData>
            ) {
                if(response.isSuccessful){
                    val criticsListApi : ArrayList<ResultCritics> = response.body()!!.response.docs as ArrayList<ResultCritics>
                    criticsList = criticsListApi
                    recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCritics).apply{
                        criticsAdapter = CriticsAdapter(criticsList)
                        layoutManager = manager
                        adapter = criticsAdapter
                    }
                    criticsAdapter.onItemClick = {
                        val intent = Intent(context, DetailCritics::class.java)
                        intent.putExtra("critics", it)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<CriticsData>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}

