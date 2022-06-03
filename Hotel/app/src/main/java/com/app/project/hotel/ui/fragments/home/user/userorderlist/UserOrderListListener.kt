package com.app.project.hotel.ui.fragments.home.user.userorderlist

import com.app.project.hotel.base.responsmodel.UserOrderListDataModel

interface UserOrderListListener {
    fun orderCancel(userId: String?, orderId: String?, position: Int)
    fun commentClick(data: UserOrderListDataModel.Data, position: Int)
}