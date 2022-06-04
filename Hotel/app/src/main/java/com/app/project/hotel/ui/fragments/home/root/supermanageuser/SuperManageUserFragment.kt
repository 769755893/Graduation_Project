package com.app.project.hotel.ui.fragments.home.root.supermanageuser

import android.annotation.SuppressLint
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentSuperManageUserBinding
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.common.OrderListYsAdapter
import com.app.project.hotel.ui.view.dialog.showDownLoadProcess
import com.app.project.hotel.ui.view.dialog.showDownLoadUserListDiaolog
import com.example.uitraning.util.Utils
import com.example.uitraning.util.log
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SuperManageUserFragment : BaseFragment<FragmentSuperManageUserBinding>() {
    private val viewModel by viewModels<SuperManageUserViewModel>()
    override fun provideLayoutId(): Int {
        return R.layout.fragment_super_manage_user
    }

    override fun initViewModelData() {
        viewModel.getUserLength()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initCase() {
        viewBind.slRefreshLayout.setOnRefreshListener {
            viewModel.flushUserList(dialog = showDownLoadUserListDiaolog(), sortType = 0)
        }
        viewBind.spinner.adapter = ArrayAdapter<String>(
            requireContext(), R.layout.spinner_item, listOf(
                "行为最优", "行为最差"
            )
        )
        viewBind.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.flushUserList(
                    dialog = showDownLoadUserListDiaolog(),
                    sortType = position
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        viewBind.rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = SuperManageUserAdapter()
        viewBind.rv.adapter = adapter

        viewModel.data.observe(this) {
            adapter.data = it
            adapter.notifyDataSetChanged()
            viewBind.slRefreshLayout.isRefreshing = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initCaseThird() {
        super.initCaseThird()
        val adapter = OrderListYsAdapter()
        viewBind.rvYs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewBind.rvYs.adapter = adapter

        viewModel.userLength.observe(this) {
            val data = mutableListOf<Int>()
            if (it >= 10) {
                if (it % 10 == 0)
                    for (i in 0 until it / 10) {
                        data.add(i)
                    }
                else
                    for (i in 0..it / 10) {
                        data.add(i)
                    }
                adapter.data = data
                adapter.notifyDataSetChanged()
            }
        }
        adapter.pageClick = {
            viewModel.flushUserList(offset = it * 10, dialog = showDownLoadProcess(), sortType = 0)
        }
    }

    override fun initCaseSecond() {
        val adapter = viewBind.rv.adapter as SuperManageUserAdapter
        viewBind.ivClearBtn.visibility = View.GONE
        viewBind.etSearchContentText
            .textChanges()
            .subscribe {
                val text = it.toString()
                if (!DENY_FIRST_TIME_CALL_BACK_INIT) {
                    if (Utils.isAllNumber(text)) {
                        viewModel.flushUserList(
                            userId = text,
                            dialog = showDownLoadProcess(),
                            sortType = 0
                        )
                    } else {
                        viewModel.flushUserList(
                            userName = text,
                            dialog = showDownLoadProcess(),
                            sortType = 0
                        )
                    }
                }
            }.bindLife()
        viewBind.etSearchContentText
            .textChanges()
            .subscribe {
                if (it.isNotEmpty())
                    viewBind.ivClearBtn.visibility = View.VISIBLE
                else viewBind.ivClearBtn.visibility = View.GONE
            }.bindLife()
        viewBind.ivClearBtn.clicks()
            .subscribe {
                viewBind.etSearchContentText.text.clear()
                viewModel.flushUserList(
                    dialog = showDownLoadProcess(),
                    sortType = 0
                )
            }.bindLife()

        adapter.btnUpdateCall = { userId, userState ->
            viewModel.upLoadUserState(userId, userState, showUpLoadDialog())
        }
    }
}