package com.example.solarapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterManufactureName extends RecyclerView.Adapter<AdapterManufactureName.MyView> {
    List<ManufactureModel> list;
    Context context;

    public AdapterManufactureName(List<ManufactureModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listlayout, parent, false);
        return new MyView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        ManufactureModel manufactureModel = list.get(position);
        holder.textView.setText(manufactureModel.getManufactureName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyView extends RecyclerView.ViewHolder {
        TextView textView;

        public MyView(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.ListItem);

        }
    }
}
