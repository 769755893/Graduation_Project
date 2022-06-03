package com.app.project.hotel.ui.fragments.home.root.supermanagehotel

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentSuperManageHotelBinding
import com.app.project.hotel.databinding.ItemSuperManageHotelParentBinding
import com.app.project.hotel.databinding.ItemSuperManageRoomChildBinding
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.common.OrderListYsAdapter
import com.app.project.hotel.common.mwindow
import com.app.project.hotel.ui.view.dialog.showDownLoadProcess
import com.app.project.hotel.ui.view.dialog.showRoomDownLoadProcess
import com.app.project.hotel.ui.view.dialog.showUpLoadStatusProgress
import com.example.uitraning.util.Utils
import com.example.uitraning.util.log
import com.example.uitraning.util.showToast
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SuperManageHotelFragment : BaseFragment<FragmentSuperManageHotelBinding>() {
    val viewModel by viewModels<SuperManageHotelViewModel>()
    override fun provideLayoutId(): Int {
        return R.layout.fragment_super_manage_hotel
    }

    override fun initViewModelData() {
        viewModel.getHotelLength()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initCase() {
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
                if (!DENY_FIRST_TIME_CALL_BACK_INIT) {
                    viewModel.downLoadHotelList(dialog = showDownLoadProcess(), sortType = position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        val adapter = SuperManageHotelAdapter()
        viewBind.rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBind.rv.adapter = adapter

        viewModel.parentData.observe(this) {
            if (it.size != 0) {
                showToast(requireContext(), "数据加载成功", mwindow)
            }
            adapter.data = it
            adapter.notifyDataSetChanged()
        }

        var mposition = 0
        var downRoomDialog: ProgressDialog? = null
        adapter.callback = object : SuperManageHotelListener {
            override fun displayRoomListClick(hotelId: String, position: Int) {
                downRoomDialog = showRoomDownLoadProcess()
                viewModel.downLoadRoomList(hotelId, downRoomDialog!!)
                mposition = position
            }

            override fun upLoadRoomStatus(
                roomId: String,
                i: Int,
                binding: ItemSuperManageRoomChildBinding
            ) {
                if (i == 2) {
                    showReasonInputDialog(roomId, i, binding)
                } else {
                    viewModel.upLoadRoomStatus(roomId, i, showUpLoadStatusProgress(), binding, "")
                }
            }

            override fun changeHotelState(binding: ItemSuperManageHotelParentBinding) {
                viewModel.changeHotelState(
                    binding.data!!.hotelId,
                    binding.sr.selectedItemId.toInt(),
                    showUpLoadStatusProgress(),
                    binding
                )
            }
        }

        viewModel.childData.observe(this) {
            downRoomDialog?.dismiss()
            adapter.displayChildData(it, mposition)
        }
    }

    override fun initCaseSecond() {
        val adapter = viewBind.rv.adapter as SuperManageHotelAdapter
        viewBind.ivClearBtn.visibility = View.GONE
        viewBind.etSearchContentText.textChanges()
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                val text = it.toString()
                if (Utils.isAllNumber(text)) {
                    viewModel.downLoadHotelList(
                        hotelId = text.toInt(),
                        dialog = showDownLoadProcess(),
                        sortType = 0
                    )
                } else {
                    viewModel.downLoadHotelList(
                        hotelName = text,
                        dialog = showDownLoadProcess(),
                        sortType = 0
                    )
                }
            }.bindLife()
        viewBind.etSearchContentText.textChanges()
            .subscribe {
                if (it.isNotEmpty()) {
                    viewBind.ivClearBtn.visibility = View.VISIBLE
                } else {
                    viewBind.ivClearBtn.visibility = View.GONE
                }
            }.bindLife()
        viewBind.ivClearBtn.clicks()
            .subscribe {
                viewBind.etSearchContentText.text.clear()
                adapter.data = viewModel.parentData.value!!
                adapter.textInputFilter("")
            }.bindLife()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initCaseThird() {
        val adapter = OrderListYsAdapter()
        viewBind.rvYs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewBind.rvYs.adapter = adapter
        viewModel.hotelLength.observe(this) {
            val data = mutableListOf<Int>()
            if (it > 5) {
                if (it % 5 == 0)
                    for (i in 0 until it / 5) {
                        data.add(i)
                    }
                else {
                    for (i in 0..it / 5) {
                        data.add(i)
                    }
                }
                adapter.data = data
                adapter.notifyDataSetChanged()
            }
        }
        adapter.pageClick = {
            if (!DENY_FIRST_TIME_CALL_BACK_INIT)
                viewModel.downLoadHotelList(
                    offset = it * 5,
                    dialog = showDownLoadProcess(),
                    sortType = 0
                )
        }
    }
}