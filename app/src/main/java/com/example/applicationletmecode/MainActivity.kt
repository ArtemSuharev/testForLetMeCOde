package com.example.applicationletmecode

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var mTabs: TabLayout
    private lateinit var mIndicator: View
    private lateinit var mViewPager: ViewPager

    private lateinit var mainTitle: TextView
    private lateinit var mainFrame: FrameLayout

    private var indicatorWidth: Int = 0

    private var castil: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Assign view reference
        mTabs = findViewById(R.id.tab)
        mIndicator = findViewById(R.id.indicator)
        mViewPager = findViewById(R.id.viewPager)

        mainTitle = findViewById(R.id.mainTitle)
        mainFrame = findViewById(R.id.mainFrame)

        // Set up the view pager and fragments
        val adapter = TabFragmentAdapter(supportFragmentManager)
        adapter.addFragment(FragmentReviewes.newInstance(), "Истории")
        adapter.addFragment(FragmentCritics.newInstance(), "Обзоры")
        mViewPager.adapter = adapter
        mTabs.setupWithViewPager(mViewPager)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)


        // Determine indicator width at runtime
        mTabs.post {
            indicatorWidth = mTabs.width / mTabs.tabCount

            // Assign new width
            val indicatorParams = mIndicator.layoutParams as FrameLayout.LayoutParams
            indicatorParams.width = indicatorWidth
            mIndicator.layoutParams = indicatorParams
        }

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            @SuppressLint("ResourceAsColor")
            override fun onPageScrolled(i: Int, positionOffset: Float, positionOffsetPx: Int) {

                val params = mIndicator.layoutParams as FrameLayout.LayoutParams

                // Multiply positionOffset with indicatorWidth to get translation
                val translationOffset = (positionOffset + i) * indicatorWidth
                params.leftMargin = translationOffset.toInt()
                mIndicator.layoutParams = params

                if (castil) {
                    mainTitle.text = "Истории"
                    mainTitle.setBackgroundResource(R.color.colorReviewes)
                    mainFrame.setBackgroundResource(R.color.colorReviewes)
                    castil = false
                    window.statusBarColor = Color.argb(255, 247, 160, 114)
                } else {
                    if (translationOffset < indicatorWidth / 2) {
                        mainTitle.text = "Истории"
                        mainTitle.setBackgroundResource(R.color.colorReviewes)
                        mainFrame.setBackgroundResource(R.color.colorReviewes)
                        window.statusBarColor = Color.argb(255, 247, 160, 114)
                    } else {
                        mainTitle.text = "Обзоры"
                        mainTitle.setBackgroundResource(R.color.colorCritics)
                        mainFrame.setBackgroundResource(R.color.colorCritics)
                        window.statusBarColor = Color.argb(255, 169, 223, 251)
                    }
                }



            }

            override fun onPageSelected(i: Int) {}

            override fun onPageScrollStateChanged(i: Int) {}
        })

    }
}

