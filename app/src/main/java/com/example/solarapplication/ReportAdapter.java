package com.example.solarapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyviewHolder> {
    List<ReportModelClass> list;
    Context context;
    private ReportListener reportListener;

    public ReportAdapter(List<ReportModelClass> list, Context context, ReportListener reportListener) {
        this.list = list;
        this.reportListener = reportListener;
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
        TextView id, PVManuName, PVmodleName, CellManuName, LabName, Warranty, CountryPv, CountryCell, DateCell, DatePv, DateLab;
        TableLayout layout;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.Id);
            layout = itemView.findViewById(R.id.Myreportlayout);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reportListener.onItemClick(getAdapterPosition());
                }
            });

        }
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(ReportModelClass item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public List<ReportModelClass> getData() {
        return list;
    }
}
