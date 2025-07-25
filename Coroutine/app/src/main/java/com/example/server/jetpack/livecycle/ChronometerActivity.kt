package com.example.server.jetpack.livecycle

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.coroutine.databinding.ActivityLivecycleBinding

class ChronometerActivity : AppCompatActivity() {

    private val mBinding : ActivityLivecycleBinding by lazy {
        ActivityLivecycleBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(mBinding.root)
    }

    override fun onResume() {
        super.onResume()
        mBinding.time.start()
    }

    override fun onPause() {
        super.onPause()
        mBinding.time.stop()

    }
}