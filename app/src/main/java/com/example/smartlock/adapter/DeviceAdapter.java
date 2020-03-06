package com.example.smartlock.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlock.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeviceAdapter extends RecyclerView.Adapter <DeviceAdapter.DeviceViewHolder>{
    List<Map<String, String>> device_info_list=new ArrayList<>();

    public DeviceAdapter(List<Map<String, String>> device_info_list){
        this.device_info_list=device_info_list;
    }
    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.device_info_cell,parent,false);
        return new DeviceViewHolder(itemView);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        holder.device_user_name.setText(device_info_list.get(position).get("user_name").toString());
        holder.query_device_number.setText(device_info_list.get(position).get("device_number").toString());

    }

    @Override
    public int getItemCount() {
        return device_info_list.size();
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder{
        TextView device_user_name,query_device_number;
        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            device_user_name=itemView.findViewById(R.id.device_user_name);
            query_device_number=itemView.findViewById(R.id.query_device_number);
        }
    }

}
