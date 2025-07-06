package com.example.coroutine.realPro

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.example.coroutine.R
import com.example.coroutine.databinding.ActivityCarHomeBinding
import com.example.coroutine.databinding.ActivityMainBinding
import com.example.coroutine.realPro.binding.adapter.CarBrandAdapter
import com.example.coroutine.realPro.binding.adapter.FooterAdapter
import com.example.coroutine.realPro.viewmodel.CarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * @HiltAndroidApp:触发Hilt代码生成
 * @AndroidEntryPoint:创建一个容器，该容器遵循Android类的生命周期
 * @Module:告诉Hilt如何提供不同类型的实例
 * @InstallIn:告诉Hilt这个模块会被安装到哪个组件上
 * @Provides:告诉Hilt如何获得具体实例
 * @Singleton:单例
 * @ViewModelInject:通过构造函数，给ViewModel注入实例
 *
 * 🧠【架构流程】
 * 🏖️repository 仓库
 * 1.⭕️NetWork 将网络数据通过 🍺RemoteMediator 存入 📊Database
 * 2.📊Database 通过 📃PagingSource 处理分页数据
 *
 * .🈳ViewModel数据
 * 3.🈳ViewModel 通过 🍺RemoteMediator 和  📃PagingSource 管理数据并生成 📒Pager
 * 4.📒Pager 转化为 💧Flow<PagingData>
 *
 * 🍍UI
 * 5.💧Flow<PagingData> 提供给 🧺PagingDataAdapter 刷新UI
 */

@AndroidEntryPoint
class CarHomeActivity : AppCompatActivity() {

    private val mBinding:ActivityCarHomeBinding by lazy {
        ActivityCarHomeBinding.inflate(layoutInflater)
    }
    private val mViewModel : CarViewModel by viewModels()
    private val mAdapter:CarBrandAdapter by lazy {
        CarBrandAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(mBinding.root)

        mBinding.carRecyclerview.adapter =
            mAdapter.withLoadStateFooter(FooterAdapter(mAdapter,this))
        mViewModel.data.observe(this) {
            mAdapter.submitData(lifecycle, it)
            //mBinding.swipeRefreshLayout.isEnabled = false
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                mAdapter.loadStateFlow.collectLatest {
                    mBinding.swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
                }
            }
        }
    }
}