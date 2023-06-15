package com.enov.bel.mvvm.view

import android.content.Intent
import com.enov.bel.R
import com.enov.bel.core.base.BaseFragment
import com.enov.bel.core.base.TitleBar
import com.enov.bel.databinding.FragmentMainBinding
import com.enov.bel.mvvm.vm.FragMainViewModel

class MainFragment: BaseFragment<FragMainViewModel, FragmentMainBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun initView() {
        binding.viewModel = viewModel
        initObserve()
        initListener()
        binding.titleBar.setDelegate(object : TitleBar.Delegate {
            override fun onClickLeft() {
                activity?.finish()
            }
        })
    }

    private fun initListener() {
        binding.toBluetoothActivity.setOnClickListener {
            startActivity(Intent(requireActivity(), BluetoothActivity::class.java))
        }
    }

    private fun initObserve() {

    }
}