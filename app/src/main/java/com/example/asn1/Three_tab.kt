package com.example.asn1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator



class Three_tab : AppCompatActivity() {

    private val tabTitles = arrayOf("Start", "History", "Settings")
    private val fragments = listOf(Start(), History(), Settings())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three_tab)

        var pager = findViewById<ViewPager2>(R.id.viewPager)
        var tl = findViewById<TabLayout>(R.id.tabLayout)
//
        pager.adapter = object:FragmentStateAdapter(this){
            override fun getItemCount(): Int = fragments.size
            override fun createFragment(position: Int): Fragment =fragments[position]
        }

        TabLayoutMediator(tl, pager){
            tab, position -> tab.text = tabTitles[position]
        }.attach()

    }
}