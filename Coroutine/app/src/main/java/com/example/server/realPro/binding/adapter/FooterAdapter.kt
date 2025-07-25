package com.example.server.realPro.binding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.coroutine.databinding.NetworkStateItemBinding

class FooterAdapter(
    private val adapter: CarBrandAdapter,
    private val context : Context
) : LoadStateAdapter<FooterViewHolder>() {
    override fun onBindViewHolder(holder: FooterViewHolder, loadState: LoadState) {
        //水平居中
        val layoutParams = holder.itemView.layoutParams
        if( layoutParams is StaggeredGridLayoutManager.LayoutParams){
            layoutParams.isFullSpan = true
        }
        holder.bindData(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FooterViewHolder {
        val binding = NetworkStateItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return FooterViewHolder(binding){
            adapter.retry()
        }
    }
}