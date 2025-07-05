package com.example.coroutine.realPro

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coroutine.R
import com.example.coroutine.databinding.ActivityCarHomeBinding
import com.example.coroutine.databinding.ActivityMainBinding

class CarHomeActivity : AppCompatActivity() {

    private val mBinding:ActivityCarHomeBinding by lazy {
        ActivityCarHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(mBinding.root)

    }
}