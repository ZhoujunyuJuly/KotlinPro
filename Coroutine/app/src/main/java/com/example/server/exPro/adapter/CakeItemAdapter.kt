package com.example.server.exPro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.coroutine.databinding.CakeItemBinding
import com.example.server.exPro.net.model.CakeItemModel

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