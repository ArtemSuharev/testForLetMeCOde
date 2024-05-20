package com.example.applicationletmecode.reviewes

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.applicationletmecode.R
import com.example.applicationletmecode.reviewes.model.ResultReviewes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class ReviewesAdapter(private val reviewesList: ArrayList<ResultReviewes>) : RecyclerView.Adapter<ReviewesAdapter.ReviewesViewHolder>() {

    val initialReviewesList = ArrayList<ResultReviewes>().apply {
        addAll(reviewesList)
    }

    class ReviewesViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView = itemView.findViewById(R.id.image)
        val title : TextView = itemView.findViewById(R.id.title)
        val subtitle : TextView = itemView.findViewById(R.id.subtitle)
        val name : TextView = itemView.findViewById(R.id.name)
        val date : TextView = itemView.findViewById(R.id.date)
        val time : TextView = itemView.findViewById(R.id.time)

        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind (resultReviewes: ResultReviewes) {
            title.text = resultReviewes.title
            subtitle.text = resultReviewes.abstract
            name.text = resultReviewes.byline

            try {
                val apiDate = resultReviewes.published_date

                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK)

                val dateFormatted = format.parse(apiDate)
                println("dateFormated = $dateFormatted")

                val dateFormat = getDate(dateFormatted?.time ?: 0L, "yyyy/MM/dd HH:mm:ss").split(" ")
                date.text = dateFormat[0]
                time.text = dateFormat[1]

            } catch (e: Exception) {
                date.text = ""
                time.text = ""
            }

            try {
                val url = resultReviewes.multimedia.first().url
                var requestOptions = RequestOptions()
                requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))

                imageView.scaleX = 1f
                imageView.scaleY = 1f

                Glide
                    .with(itemView.context)
                    .load(url)
                    .apply(requestOptions)
                    .into(imageView)

            } catch (_ : Exception) {
                imageView.scaleX = 0.3f
                imageView.scaleY = 0.3f
                imageView.setImageResource(R.drawable.image_not_found_icon)
            }

        }

        fun getDate(milliSeconds: Long, dateFormat: String?): String {
            // Create a DateFormatter object for displaying date in specified format.
            val formatter = SimpleDateFormat(dateFormat)

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds
            return formatter.format(calendar.time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reviewes_item, parent, false)
        return ReviewesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reviewesList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ReviewesViewHolder, position: Int) {
        holder.bind(reviewesList[position])
    }

    fun getFilter(): Filter {
        return reviewesFilter
    }

    private val reviewesFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<ResultReviewes> = ArrayList()
            if (constraint.isNullOrEmpty()) {
                initialReviewesList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().lowercase(Locale.ROOT)
                initialReviewesList.forEach {

                    if (it.title.lowercase(Locale.ROOT).contains(query) ||
                        it.abstract.lowercase(Locale.ROOT).contains(query) ||
                        it.des_facet.contains(query)) {
                        filteredList.add(it)
                    }

                }
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is ArrayList<*>) {
                reviewesList.clear()
                reviewesList.addAll(results.values as Collection<ResultReviewes>)
                notifyDataSetChanged()
            }
        }
    }

    fun getFilterByDate(): Filter {
        return reviewesFilterByDate
    }

    private val reviewesFilterByDate = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<ResultReviewes> = ArrayList()
            if (constraint.isNullOrEmpty()) {
                initialReviewesList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().lowercase(Locale.ROOT)
                initialReviewesList.forEach {

                    if (it.published_date.lowercase(Locale.ROOT).contains(query)) {
                        filteredList.add(it)
                    }

                }
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is ArrayList<*>) {
                reviewesList.clear()
                reviewesList.addAll(results.values as Collection<ResultReviewes>)
                notifyDataSetChanged()
            }
        }
    }



}