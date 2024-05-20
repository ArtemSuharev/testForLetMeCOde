package com.example.applicationletmecode.critics

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.applicationletmecode.R
import com.example.applicationletmecode.critics.detail_critics.DetailCriticData
import com.example.applicationletmecode.critics.model.ResultCritics
import com.example.applicationletmecode.reviewes.model.ResultReviewes
import java.util.Locale

class CriticsAdapter (private val criticsList: ArrayList<ResultCritics>) : RecyclerView.Adapter<CriticsAdapter.CriticsViewHolder>() {

    val initialCriticsList = ArrayList<ResultCritics>().apply {
        addAll(criticsList)
    }

    var onItemClick: ((DetailCriticData) -> Unit)? = null

    class CriticsViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val movieName : TextView = itemView.findViewById(R.id.movieName)
        val imageCritic : ImageView = itemView.findViewById(R.id.imageCritic)
        val nameCritic : TextView = itemView.findViewById(R.id.nameCritic)

        @SuppressLint("SetTextI18n")
        fun bind (resultCritics: ResultCritics) {

            movieName.text = resultCritics.abstract
            nameCritic.text = resultCritics.byline.original

            try {

                val url ="https://www.nytimes.com/${resultCritics.multimedia.first().url}"
                var requestOptions = RequestOptions()
                requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))

                imageCritic.scaleX = 1f
                imageCritic.scaleY = 1f

                Glide
                    .with(itemView.context)
                    .load(url)
                    .apply(requestOptions)
                    .into(imageCritic)

            } catch (_: Exception) {
                imageCritic.scaleX = 0.3f
                imageCritic.scaleY = 0.3f
                imageCritic.setImageResource(R.drawable.image_not_found_icon)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriticsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.critics_item, parent, false)
        return CriticsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return criticsList.size
    }

    override fun onBindViewHolder(holder: CriticsViewHolder, position: Int) {
        holder.bind(criticsList[position])
        val url: String = try {
            criticsList[position].multimedia.first().url
        } catch (_ : Exception) {
            ""
        }

        holder.itemView.setOnClickListener {
            val data = DetailCriticData(
                criticsList[position].abstract,
                criticsList[position].leadParagraph,
                url,
                criticsList[position].byline.original)
            onItemClick?.invoke(data)
        }
    }

    fun getFilter(): Filter {
        return criticsFilter
    }

    private val criticsFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<ResultCritics> = ArrayList()
            if (constraint.isNullOrEmpty()) {
                initialCriticsList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().lowercase(Locale.ROOT)
                initialCriticsList.forEach {

                    if (it.abstract.lowercase(Locale.ROOT).contains(query) ||
                        it.byline.original.lowercase(Locale.ROOT).contains(query)) {
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
                criticsList.clear()
                criticsList.addAll(results.values as Collection<ResultCritics>)
                notifyDataSetChanged()
            }
        }
    }


}