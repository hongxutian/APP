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

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {
    List<Map<String, String>> record_info_list=new ArrayList<>();

    public RecordAdapter(List<Map<String, String>> record_info_list){
        this.record_info_list=record_info_list;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.record_info_cell,parent,false);
        return new RecordViewHolder(itemView);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        holder.record_user_name.setText(record_info_list.get(position).get("user_name").toString());
        holder.record_open_time.setText(record_info_list.get(position).get("open_time").toString());

    }

    @Override
    public int getItemCount() {
        return record_info_list.size();
    }

    static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView record_user_name,record_open_time;
        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            record_user_name=itemView.findViewById(R.id.device_user_name);
            record_open_time=itemView.findViewById(R.id.query_device_number);

    }
        }

}
