package com.app.project.hotel.common

import androidx.navigation.fragment.findNavController
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentCommonSupportBinding
import com.app.project.hotel.ui.activity.MainActivity

class SupportFragment: BaseFragment<FragmentCommonSupportBinding>() {
    override fun provideLayoutId(): Int {
        return R.layout.fragment_common_support
    }

    override fun initCase() {
    }
}