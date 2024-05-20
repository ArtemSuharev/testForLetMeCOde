package com.example.applicationletmecode.critics.detail_critics

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.applicationletmecode.R
import com.example.applicationletmecode.critics.model.ResultCritics

class DetailCritics : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var title: TextView
    private lateinit var subtitle: TextView
    private lateinit var titleView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_critics)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val critic = intent.getParcelableExtra<DetailCriticData>("critics")
        if (critic != null) {
            imageView = findViewById(R.id.image)
            title = findViewById(R.id.title)
            subtitle = findViewById(R.id.subtitle)
            titleView = findViewById(R.id.titleView)

            title.text = critic.title
            subtitle.text = critic.subtitle
            titleView.text = critic.critic

            if (critic.url != "") {
                try {
                    val url ="https://www.nytimes.com/${critic.url}"
                    var requestOptions = RequestOptions()
                    requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))

                    imageView.scaleX = 1f
                    imageView.scaleY = 1f

                    Glide
                        .with(this)
                        .load(url)
                        .apply(requestOptions)
                        .into(imageView)

                } catch (_: Exception) {
                    imageView.scaleX = 0.3f
                    imageView.scaleY = 0.3f
                    imageView.setImageResource(R.drawable.image_not_found_icon)
                }
            } else {
                imageView.scaleX = 0.3f
                imageView.scaleY = 0.3f
                imageView.setImageResource(R.drawable.image_not_found_icon)
            }



        }

    }
}