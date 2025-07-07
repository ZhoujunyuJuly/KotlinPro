package com.example.coroutine.exPro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.coroutine.databinding.CakeItemBinding
import com.example.coroutine.databinding.CarbrandItemBinding
import com.example.coroutine.exPro.net.model.CakeItemModel
import com.example.coroutine.realPro.binding.adapter.CarBrandViewHolder
import com.example.coroutine.realPro.viewmodel.CarViewModel
import dagger.hilt.android.scopes.ActivityScoped

class CakeItemAdapter(private val context: Context): PagingDataAdapter<CakeItemModel,CakeViewHolder>(object :
    DiffUtil.ItemCallback<CakeItemModel>(){
    override fun areItemsTheSame(oldItem: CakeItemModel, newItem: CakeItemModel): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: CakeItemModel, newItem: CakeItemModel): Boolean {
        return oldItem == newItem
    }
}) {

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        getItem(position).let {
            val cakeItemBinding = holder.binding as CakeItemBinding
            cakeItemBinding.cake = it
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        val view = CakeItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return CakeViewHolder(view)
    }
}