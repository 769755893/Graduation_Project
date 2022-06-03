package com.app.project.hotel.ui.fragments.home.user.userhotelroomlist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class UserHotelRoomVpAdapter(
    val fragments: List<Fragment>,
    lifecycle: Lifecycle,
    fragmentManager: FragmentManager
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}