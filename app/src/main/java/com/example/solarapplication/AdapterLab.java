package com.example.solarapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterLab extends  RecyclerView.Adapter<AdapterLab.Myview>{
Context context;
List<LabModel> list;

    public AdapterLab(Context context, List<LabModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.lablayout, parent, false);
        return new Myview(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myview holder, int position) {
        LabModel dataModelClass =list.get(position);
        holder.name.setText(dataModelClass.getName());
        holder.date.setText(dataModelClass.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Myview extends RecyclerView.ViewHolder {
        TextView name,date;
        public Myview(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.NameCertificate);
            date=itemView.findViewById(R.id.Date);
        }
    }
}
