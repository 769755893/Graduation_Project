package com.app.project.hotel.ui.fragments.login.root

import android.app.AlertDialog
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.app.project.hotel.R
import com.app.project.hotel.api.SuperApi
import com.app.project.hotel.databinding.FragmentLoginSuperUserBinding
import com.app.project.hotel.ui.activity.MainActivity
import com.app.project.hotel.common.BaseFragment
import com.example.uitraning.util.coroutines.Co
import com.example.uitraning.util.coroutines.Main
import com.jakewharton.rxbinding3.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginSuperUserFragment : BaseFragment<FragmentLoginSuperUserBinding>() {
    @Inject
    lateinit var service: SuperApi
    override fun provideLayoutId() = R.layout.fragment_login_super_user
    override fun initCase() {
        viewBind.btnEnter.clicks().subscribe {
            superLoginStart()
        }.bindLife()
    }


    override fun onStart() {
        super.onStart()
        (activity as MainActivity).apply {
            leftScroll = {
                Toast.makeText(requireContext(), "右边已经到底了哦", Toast.LENGTH_SHORT).show()
            }
            rightScroll = {
                findNavController().navigate(R.id.action_loginSuperUser_to_login_Hotel)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).touchClear()
    }

    private fun superLoginStart() {
        Co.launch(Dispatchers.IO) {
            if (!viewBind.etInput.text.isNullOrBlank()) {
                val ans = service.login(viewBind.etInput.text.toString())
                if (ans.msg == "true") {
                    Main {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Correct")
                            .setIcon(
                                ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.ic_baseline_check_24,
                                    resources.newTheme()
                                )
                            )
                            .setMessage("Welcome, Controller~")
                            .setCancelable(false)
                            .setPositiveButton(
                                "Confirm"
                            ) { p0, p1 ->
                                p0?.dismiss()
                                findNavController().navigate(R.id.action_loginSuperUser_to_superMainPage)
                            }.show()
                    }
                } else {
                    Main {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Warning")
                            .setIcon(
                                ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.ic_baseline_error_outline_24,
                                    resources.newTheme()
                                )
                            )
                            .setMessage("The key is wrong!")
                            .setCancelable(false)
                            .setPositiveButton(
                                "Confirm"
                            ) { p0, p1 -> p0?.dismiss() }.show()
                    }
                }
            }
        }
    }
}