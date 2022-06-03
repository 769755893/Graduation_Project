package com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.comments

import android.annotation.SuppressLint
import android.view.View
import android.widget.AdapterView
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentUserHotelRoomCommentBinding
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.ui.activity.MainActivity
import com.app.project.hotel.ui.fragments.home.user.userhotellist.UserHotelListViewModel
import com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.UserHotelRoomViewModel
import com.example.uitraning.util.Utils
import com.example.uitraning.util.log
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HotelCommentFragment : BaseFragment<FragmentUserHotelRoomCommentBinding>() {
    private val viewModel by activityViewModels<UserHotelRoomViewModel>()
    val hotelListViewModel by activityViewModels<UserHotelListViewModel>()

    override fun provideLayoutId(): Int {
        return R.layout.fragment_user_hotel_room_comment
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initCase() {
        viewBind.commentSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                (viewBind.rvComment.adapter as UserHotelCommentAdapter).sortCommentData(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        viewBind.slRefreshLayout.setOnRefreshListener {
            viewModel.refreshCommentData(hotelListViewModel.roomPageData.value?.hotelId!!)
        }

        hotelListViewModel.roomPageData.observe(this) {
            viewBind.hotelName = it?.hotelName
            viewBind.hotelIcon = Utils.string2Bitmap(it?.hotelIcon)?.toDrawable(resources)
        }


        val adapter = UserHotelCommentAdapter()
        viewBind.rvComment.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBind.rvComment.adapter = adapter
        viewModel.hotelCommentData.observe(this) {
            viewBind.slRefreshLayout.isRefreshing = false
            adapter.data = it
            adapter.sortCommentData(0)
        }

        var clicks = 0
        var f = 0L
        var n = 0L
        adapter.goodClickCall = { position, commentId ->
            if (clicks % 2 == 0) {
                f = System.currentTimeMillis()
            } else {
                n = System.currentTimeMillis()
            }
            clicks++
            log(f - n)
            viewModel.gooClick(commentId)
            adapter.notifyItemChanged(position, "goodClick")
        }

        var lengthChangeGuard = 0
        viewBind.tvSearchContentText.textChanges().skip(2, TimeUnit.SECONDS)
            .subscribe {
                if (it.length > lengthChangeGuard) {
                    lengthChangeGuard = it.length
                } else {
                    lengthChangeGuard = it.length
                    adapter.data = viewModel.hotelCommentData.value!!
                    adapter.notifyDataSetChanged()
                }
                adapter.flushHotelCommentData(it.toString())
            }.bindLife()
        viewBind.ivClearBtn.clicks()
            .subscribe {
                viewBind.tvSearchContentText.text.clear()
            }.bindLife()
    }

    override fun initCaseThird() {
        viewBind.tvSearchContentText.textChanges()
            .subscribe {
                if (it.isEmpty())
                    viewBind.ivClearBtn.visibility = View.GONE
                else {
                    viewBind.ivClearBtn.visibility = View.VISIBLE
                }
            }.bindLife()
    }
}