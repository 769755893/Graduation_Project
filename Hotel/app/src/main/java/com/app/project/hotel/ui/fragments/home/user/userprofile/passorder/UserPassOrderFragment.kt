package com.app.project.hotel.ui.fragments.home.user.userprofile.passorder

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.project.hotel.R
import com.app.project.hotel.base.responsmodel.UserOrderListDataModel
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.common.OrderListYsAdapter
import com.app.project.hotel.databinding.FragmentUserPassOrderBinding
import com.app.project.hotel.ui.fragments.home.user.userorderlist.UserOrderListAdapter
import com.app.project.hotel.ui.fragments.home.user.userorderlist.UserOrderListListener
import com.app.project.hotel.ui.fragments.home.user.userprofile.passorder.commentdialog.showPassOrderCommentDialog
import com.app.project.hotel.ui.fragments.home.user.userorderlist.showProgress
import com.app.project.hotel.ui.fragments.home.user.userprofile.ProFileViewModel
import com.app.project.hotel.ui.fragments.home.user.userprofile.showUserPassOrderTimePicker
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.visibility
import com.jakewharton.rxbinding3.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class UserPassOrderFragment : BaseFragment<FragmentUserPassOrderBinding>() {
    val viewModel by viewModels<PassOrderViewModel>()
    private val userViewModel by activityViewModels<ProFileViewModel>()
    override fun provideLayoutId(): Int {
        return R.layout.fragment_user_pass_order
    }

    override fun initViewModelData() {
        super.initViewModelData()
        viewModel.initOrderListData(
            userId = userViewModel.data.value?.userId,
            dialog = showProgress(),
            orderTimeType = -1
        )
        viewModel.getOrderLength(userViewModel.data.value?.userId!!)
    }

    @SuppressLint("NotifyDataSetChanged", "CheckResult")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun initCase() {
        val adapter = UserOrderListAdapter()
        viewBind.rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBind.rv.adapter = adapter

        viewModel.data.observe(this) {
            if (it.size != 0) {
                viewBind.tvNoData.visibility = View.GONE
            }
            viewBind.slRefreshLayout.isRefreshing = false
            adapter.data = it
            adapter.notifyDataSetChanged()
        }

        adapter.callback = object : UserOrderListListener {
            override fun orderCancel(userId: String?, orderId: String?, position: Int) {
            }

            override fun commentClick(data: UserOrderListDataModel.Data, position: Int) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    showPassOrderCommentDialog(data, position)
                }
            }
        }

        viewBind.btnTvStartTime.clicks()
            .subscribe {
                showUserPassOrderTimePicker(requireActivity(), viewBind, 1, adapter)
            }.bindLife()

        viewBind.ivClearBtn.clicks()
            .subscribe {
                adapter.data = viewModel.data.value!!
                adapter.notifyDataSetChanged()
                viewBind.st = ""
            }.bindLife()

        viewModel.refreshUI = { position, state ->
            adapter.notifyItemChanged(position, state)
        }
        var etLength = 0
        viewBind.etSearchContentText.textChanges()
            .skip(2, TimeUnit.SECONDS)
            .subscribe {
                if (it.length > etLength) {
                    etLength = it.length
                    adapter.flushHotelName(it.toString())
                } else {
                    etLength = it.length
                    adapter.data = viewModel.data.value!!
                    adapter.flushHotelName(it.toString())
                }
            }.bindLife()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initCaseSecond() {
        val adapter = OrderListYsAdapter()
        viewBind.rvYs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewBind.rvYs.adapter = adapter
        adapter.pageClick = {
            viewModel.initOrderListData(
                userId = userViewModel.data.value?.userId,
                dialog = showProgress(),
                offset = it * 5,
                orderTimeType = -1
            )
        }
        viewModel.pageCount.observe(this) {
            val data = mutableListOf<Int>()
            if (it >= 5) {
                if (it % 5 == 0)
                    for (i in 0 until it / 5) {
                        data.add(i)
                    } else
                    for (i in 0..it / 5)
                        data.add(i)
                adapter.data = data
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun initCaseThird() {
        super.initCaseThird()
        viewBind.slRefreshLayout.setOnRefreshListener {
            viewModel.initOrderListData(
                userId = userViewModel.data.value?.userId,
                dialog = showProgress(),
                orderTimeType = -1
            )
        }
    }
}