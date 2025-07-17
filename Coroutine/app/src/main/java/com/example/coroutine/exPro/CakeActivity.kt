package com.example.coroutine.exPro

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.arouter_annotation.api.ARouter
import com.example.coroutine.R
import com.example.coroutine.databinding.ActivityCakeBinding
import com.example.coroutine.databinding.CakeItemBinding
import com.example.coroutine.exPro.adapter.CakeItemAdapter
import com.example.coroutine.exPro.viewmodel.CakeViewModel
import com.example.coroutine.realPro.binding.adapter.CarBrandAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ARouter(path = "/app/CakeActivity")
class CakeActivity : AppCompatActivity() {

    private val mViewBinding : ActivityCakeBinding by lazy {
        ActivityCakeBinding.inflate(layoutInflater)
    }
    private val adapter : CakeItemAdapter by lazy {
        CakeItemAdapter(this)
    }

    private val mViewModel : CakeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        mViewBinding.cakeRecyclerview.adapter = adapter
        mViewModel.data.observe(this){
            adapter.submitData(lifecycle,it)
        }
    }
}