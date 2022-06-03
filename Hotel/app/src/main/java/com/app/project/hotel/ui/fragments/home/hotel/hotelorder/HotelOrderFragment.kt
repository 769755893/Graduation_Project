package com.app.project.hotel.ui.fragments.home.hotel.hotelorder

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.project.hotel.R
import com.app.project.hotel.common.OrderListYsAdapter
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.databinding.FragmentHotelOrderBinding
import com.app.project.hotel.common.mwindow
import com.app.project.hotel.ui.fragments.home.hotel.HotelMainPageViewModel
import com.app.project.hotel.ui.view.dialog.showHotelOrderConfirmProgress
import com.app.project.hotel.ui.view.dialog.showHotelOrderDownProgress
import com.example.uitraning.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HotelOrderFragment : BaseFragment<FragmentHotelOrderBinding>() {
    val viewModel by viewModels<HotelOrderViewModel>()
    private val hotelViewModel by activityViewModels<HotelMainPageViewModel>()
    override fun provideLayoutId(): Int {
        return R.layout.fragment_hotel_order
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initCase() {
            viewModel.initOrderListData(
                hotelId = hotelViewModel.data.value?.hotelId,
                showProgressDialog()
            )
            viewModel.initOrderListData(
                hotelId = hotelViewModel.data.value?.hotelId,
                dialog = showProgressDialog()
            )

        viewModel.getOrderLength(hotelViewModel.data.value?.hotelId!!.toInt())

        viewBind.slRefreshLayout.setOnRefreshListener {
            viewModel.initOrderListData(
                hotelId = hotelViewModel.data.value?.hotelId,
                dialog = showProgressDialog()
            )
        }

        val adapter = HotelOrderListAdapter()
        viewBind.rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBind.rv.adapter = adapter

        viewModel.notifyOrderListEmpty = {
            viewBind.slRefreshLayout.isRefreshing = false
            showToast(requireContext(), "没有任何订单数据~", mwindow)
        }
        viewModel.data.observe(this) {
            if (it.size != 0) {
                viewBind.slRefreshLayout.isRefreshing = false
                viewBind.tvNoData.visibility = View.GONE
            }
            adapter.data = it
            adapter.notifyDataSetChanged()
        }

        adapter.callback = object : HotelOrderListener {
            override fun hotelConfirm(orderId: String, position: Int) {
                val dialog = showHotelOrderConfirmProgress()
                viewModel.hotelConfirm(orderId, context, dialog, position)
            }

            override fun hotelCancel(orderId: String, hotelId: Int, position: Int) {
                showRejectDialog(orderId, hotelId, position)
            }

            override fun orderDone(orderId: String, hotelId: Int, position: Int) {
                val dialog = showHotelOrderDownProgress()
                viewModel.hotelOrderDone(orderId, hotelId, context, dialog, position)
            }
        }

        viewModel.refreshUI = { position, state ->
            adapter.notifyItemChanged(position, state)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initCaseSecond() {
        super.initCaseSecond()
        val adapter = OrderListYsAdapter()
        viewBind.rvYs.adapter = adapter
        viewBind.rvYs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewModel.orderLength.observe(this) {
            val data = mutableListOf<Int>()
            if (it >= 5) {
                if (it % 5 == 0)
                    for (i in 0 until it / 5) {
                        data.add(i)
                    }
                else
                    for (i in 0..it / 5) {
                        data.add(i)
                    }
                adapter.data = data
                adapter.notifyDataSetChanged()
            }
        }
        adapter.pageClick = {
            viewModel.initOrderListData(
                hotelId = hotelViewModel.data.value?.hotelId,
                dialog = showProgressDialog(),
                offset = it * 5
            )
        }
    }
}