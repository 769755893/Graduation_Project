package com.app.project.hotel.ui.fragments.home.user.userorderlist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.project.hotel.R
import com.app.project.hotel.base.responsmodel.UserOrderListDataModel
import com.app.project.hotel.databinding.FragmentUserOrderListBinding
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.common.OrderListYsAdapter
import com.app.project.hotel.ui.fragments.home.user.userorderlist.commentdialog.showCommentDialog
import com.app.project.hotel.ui.fragments.home.user.userprofile.ProFileViewModel
import com.app.project.hotel.ui.view.dialog.showOrderCancelProgress
import com.example.uitraning.util.log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserOrderListFragment : BaseFragment<FragmentUserOrderListBinding>() {
    val viewModel by activityViewModels<UserOrderListViewModel>()
    private val userViewModel by activityViewModels<ProFileViewModel>()
    private val ORDER_CNT_PER_PAGE = 5
    override fun provideLayoutId(): Int {
        return R.layout.fragment_user_order_list
    }

    override fun initViewModelData() {
        super.initViewModelData()
        viewModel.initOrderListData(
            userId = userViewModel.data.value?.userId,
            dialog = showProgressDialog(),
            orderTimeType = 1
        )
        viewModel.notifyOrderListEmpty = {
            if (viewBind.slRefreshLayout.isRefreshing) {
                viewBind.slRefreshLayout.isRefreshing = false
            }
        }
        viewModel.getOrderLength(userViewModel.data.value?.userId)

        viewModel.loadFailed = {
            viewBind.slRefreshLayout.isRefreshing = false
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun initCase() {
        val adapter = UserOrderListAdapter()
        viewBind.rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBind.rv.adapter = adapter

        viewModel.data.observe(this) { data ->
            if (viewBind.slRefreshLayout.isRefreshing) {
                viewBind.tvNoData.visibility = View.GONE
                viewBind.slRefreshLayout.isRefreshing = false
            }
            adapter.data = data
            adapter.notifyDataSetChanged()
            data.forEach {
                if (!it?.reason.isNullOrBlank() && it?.userOrderConfirm != "1") {
                    showReasonDialog(it?.orderId, it?.reason, it?.hotelName, it?.hotelPhone)
                }
            }
        }
        viewBind.slRefreshLayout.setOnRefreshListener {
            viewModel.initOrderListData(
                userId = userViewModel.data.value?.userId,
                dialog = showProgressDialog(),
                orderTimeType = 1
            )
            viewModel.getOrderLength(userViewModel.data.value?.userId!!)
        }
        adapter.callback = object : UserOrderListListener {
            override fun orderCancel(userId: String?, orderId: String?, position: Int) {
                val dialog = showOrderCancelProgress()
                dialog.show()
                viewModel.orderCancel(userId, orderId, dialog, position)
            }

            override fun commentClick(data: UserOrderListDataModel.Data, position: Int) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    showCommentDialog(data, position)
                }
            }
        }
        viewModel.showAlertMsg = {
            AlertDialog.Builder(requireContext())
                .setTitle("提示")
                .setIcon(R.drawable.ic_baseline_check_24)
                .setMessage("已成功取消订单")
                .setPositiveButton("Confirm", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0?.dismiss()
                        adapter.notifyItemChanged(it, "cancel")
                    }
                }).show()
        }

        viewModel.refreshUI = { position, state ->
            adapter.notifyItemChanged(position, state)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initCaseSecond() {
        super.initCaseSecond()
        val adapter = OrderListYsAdapter()
        viewBind.rvYs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewBind.rvYs.adapter = adapter
        adapter.pageClick = {
            viewModel.initOrderListData(
                userId = userViewModel.data.value?.userId,
                dialog = showProgressDialog(),
                offset = it * ORDER_CNT_PER_PAGE,
                orderTimeType = 1
            )
        }
        viewModel.pageCount.observe(this) {
            "order Length $it".log()
            val data = mutableListOf<Int>()
            if (it >= ORDER_CNT_PER_PAGE) {
                if (it % ORDER_CNT_PER_PAGE == 0)
                    for (i in 0 until it / ORDER_CNT_PER_PAGE) {
                        data.add(i)
                    } else
                    for (i in 0..it / ORDER_CNT_PER_PAGE) {
                        data.add(i)
                    }
                adapter.data = data
                adapter.notifyDataSetChanged()
            }
        }
    }
}