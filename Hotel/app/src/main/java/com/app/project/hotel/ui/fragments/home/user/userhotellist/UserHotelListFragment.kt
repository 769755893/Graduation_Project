package com.app.project.hotel.ui.fragments.home.user.userhotellist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentUserHotelListBinding
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.common.OrderListYsAdapter
import com.example.uitraning.util.coroutines.Co
import com.example.uitraning.util.log
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class UserHotelListFragment : BaseFragment<FragmentUserHotelListBinding>() {
    private val viewModel by activityViewModels<UserHotelListViewModel>()
    private val ysAdapter by lazy { OrderListYsAdapter() }
    private val hotelListAdapter by lazy { UserHotelListAdapter() }
    private val HOTEL_CNT = 8
    override fun provideLayoutId(): Int {
        return R.layout.fragment_user_hotel_list
    }

    /*
    This Call back will only init in the first time create view
    */
    @SuppressLint("NotifyDataSetChanged")
    override fun initViewModelData() {
        super.initViewModelData()
        viewModel.getHotelList(0, 0, showProgressDialog = showProgressDialog())
        viewModel.filter = {
            hotelListAdapter.filterText(it)
        }
        viewModel.cancelFilter = {
            hotelListAdapter.data = viewModel.data.value!!
            hotelListAdapter.notifyDataSetChanged()
        }
        viewModel.getHotelCount()

        viewModel.loadFailed = {
            viewBind.slRefreshLayout.isRefreshing = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initCase() {
        viewModel.data.observe(this) {
            if (it.size != 0) {
                viewBind.slRefreshLayout.isRefreshing = false
            }
            hotelListAdapter.data = it
            hotelListAdapter.notifyDataSetChanged()
        }

        viewBind.rv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        viewBind.rv.adapter = hotelListAdapter
        hotelListAdapter.callback = object : UserHotelListListener {
            override fun goHotelRoomPage(hotelId: String) {
                val b = Bundle()
                b.putString("hotelId", hotelId)
                findNavController().navigate(R.id.action_userMainPage_to_userHotelRoomPage, b)
            }
        }
        viewBind.slRefreshLayout.setOnRefreshListener {
            viewModel.getHotelList(0, 0, showProgressDialog = showProgressDialog())
        }
    }

    override fun initCaseSecond() {
        super.initCaseSecond()
        viewBind.srSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (!DENY_FIRST_TIME_CALL_BACK_INIT) {
                    viewModel.getHotelList(sortType = p2, showProgressDialog = showProgressDialog())
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        initSearchView()
    }

    private fun initSearchView() {
        viewBind.ivClearBtn.visibility = View.GONE
        viewBind.etSearchContentText.textChanges()
            .throttleFirst(3, TimeUnit.SECONDS)
            .subscribe {
                if (!DENY_FIRST_TIME_CALL_BACK_INIT && it.isNotEmpty()) {
                    viewModel.getHotelList(0, 0, it.toString(), showProgressDialog())
                }
            }
            .bindLife()

        viewBind.etSearchContentText.textChanges()
            .subscribe {
                if (it.isNotEmpty()) {
                    viewBind.ivClearBtn.visibility = View.VISIBLE
                } else {
                    viewBind.ivClearBtn.visibility = View.GONE
                }
            }
            .bindLife()

        viewBind.ivClearBtn.clicks()
            .subscribe {
                viewBind.etSearchContentText.text.clear()
            }.bindLife()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initCaseThird() {
        super.initCaseThird()
        viewBind.rvYs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewBind.rvYs.adapter = ysAdapter
        ysAdapter.pageClick = {
            if (!DENY_FIRST_TIME_CALL_BACK_INIT) {
                viewModel.getHotelList(it * HOTEL_CNT, 0, showProgressDialog = showProgressDialog())
            }
        }
        viewModel.pageCount.observe(this) {
            val data = mutableListOf<Int>()
            if (it >= HOTEL_CNT) {
                if (it % HOTEL_CNT == 0)
                    for (i in 0 until it / HOTEL_CNT) {
                        data.add(i)
                    } else
                    for (i in 0..it / HOTEL_CNT) {
                        data.add(i)
                    }
                ysAdapter.data = data
                ysAdapter.notifyDataSetChanged()
            }
        }
    }
}