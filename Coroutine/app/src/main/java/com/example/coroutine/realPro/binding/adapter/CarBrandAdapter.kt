package com.example.coroutine.realPro.binding.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.coroutine.databinding.CarbrandItemBinding
import com.example.coroutine.realPro.model.CarBrandItemModel

class CarBrandAdapter(private val context:Context) : PagingDataAdapter<CarBrandItemModel,CarBrandViewHolder>(object : DiffUtil.ItemCallback<CarBrandItemModel>(){
    //渲染差异对比，增强渲染性能
    override fun areItemsTheSame(oldItem: CarBrandItemModel, newItem: CarBrandItemModel): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: CarBrandItemModel,
        newItem: CarBrandItemModel
    ): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onBindViewHolder(holder: CarBrandViewHolder, position: Int) {
        getItem(position).let {
            val carBrandItemBinding = holder.binding as CarbrandItemBinding
            carBrandItemBinding.carBrand = it
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarBrandViewHolder {
        val view = CarbrandItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return CarBrandViewHolder(view)
    }
}