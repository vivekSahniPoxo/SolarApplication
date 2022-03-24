package com.example.solarapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterCell extends RecyclerView.Adapter<AdapterCell.Myview> {
    List<CellModel> list;
    Context context;

    public AdapterCell(List<CellModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listlayout, parent, false);
return new Myview(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myview holder, int position) {
        CellModel datamodel = list.get(position);
        holder.textView.setText(datamodel.getCellManufacture());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myview extends RecyclerView.ViewHolder {
        TextView textView;

        public Myview(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.ListItem);
        }
    }
}
