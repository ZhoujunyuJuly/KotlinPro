package com.example.coroutine.dataBinding

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.coroutine.R
import com.example.coroutine.dataBinding.net.MainViewModel
import com.example.coroutine.databinding.ActivityRetrofit2Binding


class CoroutineActivity2 : AppCompatActivity() {
    private var netResponse: TextView? = null
    private var clickBtn: Button? = null
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = DataBindingUtil.setContentView<ActivityRetrofit2Binding>(
            this,
            R.layout.activity_retrofit2
        )
        //使用DataBinding，获取在xml命名的viewModel，进行同类型赋值
        binding.viewModelCoroutine = mainViewModel
        //指定生命周期所属者
        binding.lifecycleOwner = this
        binding.netButton.setOnClickListener{
            //在xml通过databinding绑定的控件，能感知到viewModel的数据变化，会自动更新UI
            mainViewModel.getUser()
        }

    }
}