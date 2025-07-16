package com.example.coroutine.jetpack.room.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coroutine.databinding.RoomAdapterItemBinding;
import com.example.coroutine.jetpack.room.db.PersonBean;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<PersonBean> mDataList = new ArrayList<>();
    private Context mContext;

    public RoomAdapter(Context context, List<PersonBean> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    public void addDatas(List<PersonBean> dataList){
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RoomAdapterItemBinding binding = RoomAdapterItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new RoomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        PersonBean personBean = mDataList.get(position);
        holder.binding.setPerson(personBean);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder{
        private RoomAdapterItemBinding binding;
        public RoomViewHolder(@NonNull RoomAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
