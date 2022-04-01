package com.example.solarapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyviewHolder> {
    List<ReportModelClass> list;
    Context context;

    public ReportAdapter(List<ReportModelClass> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reportrecyclerlayout, parent, false);
        return new MyviewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        ReportModelClass reportModelClass = list.get(position);
        holder.id.setText(reportModelClass.getID());
        holder.PVManuName.setText(reportModelClass.getPVManuName());
        holder.PVmodleName.setText(reportModelClass.getPVmodleName());
        holder.CellManuName.setText(reportModelClass.getCellManuName());
        holder.LabName.setText(reportModelClass.getLabName());
        holder.Warranty.setText(reportModelClass.getWarranty());
        holder.CountryPv.setText(reportModelClass.getCountryPv());
        holder.CountryCell.setText(reportModelClass.getCountryCell());
        holder.DateCell.setText(reportModelClass.getDateCell());
        holder.DatePv.setText(reportModelClass.getDatePv());
        holder.DateLab.setText(reportModelClass.getDateLab());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView id,PVManuName,PVmodleName,CellManuName,LabName,Warranty,CountryPv,CountryCell,DateCell,DatePv,DateLab;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.Id);
            PVManuName = itemView.findViewById(R.id.pvnamemanu);
            PVmodleName = itemView.findViewById(R.id.pvmodel);
            CellManuName = itemView.findViewById(R.id.cellmanuname);
            LabName = itemView.findViewById(R.id.certiname);
            Warranty = itemView.findViewById(R.id.warranty);
            CountryPv = itemView.findViewById(R.id.pvcountry);
            CountryCell = itemView.findViewById(R.id.cellcountry);
            DateCell = itemView.findViewById(R.id.datecell1);
            DatePv = itemView.findViewById(R.id.datepv);
            DateLab = itemView.findViewById(R.id.datecertificate);


        }
    }
}
