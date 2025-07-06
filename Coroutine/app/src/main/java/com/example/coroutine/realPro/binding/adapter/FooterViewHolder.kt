package com.example.coroutine.realPro.binding.adapter

import android.app.Activity
import android.view.View
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.coroutine.databinding.NetworkStateItemBinding

class FooterViewHolder(
    private val binding : NetworkStateItemBinding,
    private val retryCallback : ()->Unit
) :  RecyclerView.ViewHolder(binding.root){

    fun bindData(loadState: LoadState){
        binding.apply {
            progress.isVisible = loadState is LoadState.Loading
            retryBtn.isVisible = loadState is LoadState.Error
            retryBtn.setOnClickListener {
                retryCallback()
            }
            errorMsg.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
            errorMsg.text = (loadState as? LoadState.Error) ?.error?.message
        }
    }

    inline var View.isVisible : Boolean
        get() = visibility == View.VISIBLE
        set(value){
            visibility = if(value) View.VISIBLE else View.GONE
        }
}