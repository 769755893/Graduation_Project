package com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.comments

import android.annotation.SuppressLint
import com.app.project.hotel.R
import com.app.project.hotel.base.responsmodel.HotelCommentDataModel
import com.app.project.hotel.databinding.ItemUserHotelCommentsBinding
import com.app.project.hotel.common.BaseRecyclerAdapter

class UserHotelCommentAdapter: BaseRecyclerAdapter<ItemUserHotelCommentsBinding>() {
    var data = mutableListOf<HotelCommentDataModel.Data?>()
    var goodClickCall: ((position: Int, commentId: String) -> Unit) ? = null
    override fun provideLayoutId() = R.layout.item_user_hotel_comments

    @SuppressLint("NotifyDataSetChanged")
    fun sortCommentData(sortType: Int) {
        if (sortType == 0) {
            data.sortByDescending {
                it?.startTime?.dropLast(9)?.filter {
                    it != '-'
                }.toString().toInt()
            }
        }
        else if (sortType == 1) {
            data.sortByDescending {
                it?.userCommentScore
            }
        } else if (sortType == 2) {
            data.sortBy {
                it?.userCommentScore
            }
        } else if (sortType == 3) {
            data.sortByDescending {
                it?.goodCnt?.toInt()
            }
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun flushHotelCommentData(key: String) {
        data = data.filter {
            key in it?.userComment.toString()
        }.toMutableList()
        notifyDataSetChanged()
    }

    override fun initViewCase(binding: ItemUserHotelCommentsBinding, position: Int) {
        binding.data = data[position]
        binding.goodClick = {
            val commentId = binding.data!!.commentId!!
            goodClickCall?.invoke(position, commentId)
        }
    }


    override fun initViewCase(
        binding: ItemUserHotelCommentsBinding,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.initViewCase(binding, position, payloads)
        if (payloads[0] == "goodClick") {
            binding.data!!.goodCnt = (binding.data!!.goodCnt.toString().toInt() + 1).toString()
            binding.tvGoodCount.text = binding.data!!.goodCnt.toString()
        }
    }

    override fun getItemCount() = data.size
}