package org.classapp.whatsup.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.classapp.whatsup.FeaturedFragment
import org.classapp.whatsup.MyEventFragment
import org.classapp.whatsup.NearMeFragment
import org.classapp.whatsup.R

private val WHATSUP_TITLE = arrayOf("Featured","Near Me","My Event")

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return FeaturedFragment.newInstance("", "")
        } else if (position == 1) {
            return NearMeFragment.newInstance("", "")
        } else if (position == 2) {
            return MyEventFragment.newInstance("", "")
        } else {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return WHATSUP_TITLE[position]
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 3
    }
}