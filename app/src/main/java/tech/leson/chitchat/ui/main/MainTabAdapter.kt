package tech.leson.chitchat.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import tech.leson.chitchat.ui.main.MainActivity

class MainTabAdapter(activity: MainActivity) : FragmentStateAdapter(activity) {

    lateinit var mTabs: ArrayList<Fragment>

    override fun getItemCount() = mTabs.size

    override fun createFragment(position: Int) = mTabs[position]
}
