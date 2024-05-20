package com.example.applicationletmecode

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.applicationletmecode.reviewes.ReviewesAdapter
import com.example.applicationletmecode.reviewes.apis.ApiServiceReviewes
import com.example.applicationletmecode.reviewes.model.ResultReviewes
import com.example.applicationletmecode.reviewes.model.ReviewesData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class FragmentReviewes : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewesList: ArrayList<ResultReviewes>
    private lateinit var reviewesAdapter: ReviewesAdapter
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchBar: SearchView
    private lateinit var buttonCalendar: TextView
    private var mDate: Date? = null

    companion object {
        fun newInstance(): FragmentReviewes {
            return FragmentReviewes()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_one, container, false)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manager = LinearLayoutManager(context)
        swipeRefreshLayout = view.findViewById(R.id.container)
        getReviewes(view)

        swipeRefreshLayout.setOnRefreshListener {

            searchBar.setQuery("", false);

            buttonCalendar.text = "Поиск по дате публикации"
            mDate = null

            swipeRefreshLayout.isRefreshing = false
            getReviewes(view)
            reviewesAdapter.notifyDataSetChanged()

        }

        searchBar = view.findViewById(R.id.search_bar)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                reviewesAdapter.getFilter().filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                reviewesAdapter.getFilter().filter(newText);
                return true
            }

        })

        buttonCalendar = view.findViewById(R.id.calendar)
        buttonCalendar.setOnClickListener {
            showDatePickerDialog { selectedDate ->

                mDate = selectedDate
                val calendar = Calendar.getInstance()
                calendar.time = mDate!!
                val year = "${calendar.get(Calendar.YEAR)}"
                var month = "${calendar.get(Calendar.MONTH) + 1}"
                if (calendar.get(Calendar.MONTH) + 1 < 10) {
                    month = "0$month"
                }
                var day = "${calendar.get(Calendar.DAY_OF_MONTH)}"
                if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
                    day = "0$day"
                }
                buttonCalendar.text = "$year / $month / $day"
                reviewesAdapter.getFilterByDate().filter("$year-$month-${day}T")

            }
        }

    }

    private fun getReviewes(view: View) {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val catApi: ApiServiceReviewes = retrofit.create(ApiServiceReviewes::class.java)

        catApi.getAllData().enqueue(object: Callback<ReviewesData>{
            override fun onResponse(
                call: Call<ReviewesData>,
                response: Response<ReviewesData>
            ) {
                if(response.isSuccessful){
                    val reviewesListApi : ArrayList<ResultReviewes> = response.body()!!.resultReviewes as ArrayList<ResultReviewes>
                    reviewesList = reviewesListApi.filter { it.item_type == "Article" } as ArrayList<ResultReviewes>
                    recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply{
                        reviewesAdapter = ReviewesAdapter(reviewesList)
                        layoutManager = manager
                        adapter = reviewesAdapter
                    }
                }
            }

            override fun onFailure(call: Call<ReviewesData>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

    private fun showDatePickerDialog(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        if (mDate != null) {
            calendar.time = mDate!!
        }
        val datePickerDialog = context?.let { it ->
            DatePickerDialog(
                it,
                { _, year, monthOfYear, dayOfMonth ->
                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
                    val date = format.parse("$year-${(monthOfYear + 1)}-$dayOfMonth\"")
                    date?.let { onDateSelected(it) }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }

        datePickerDialog?.show()
    }

}
