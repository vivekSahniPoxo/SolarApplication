package com.example.solarapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class cellmanufacture extends Fragment {
    EditText cellName;
    RecyclerView recyclerView;
    Button saveBtn;
    CellDb cellDb;
    List<CellModel> list;
    AdapterCell adapterCell;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cellmanufacture, container, false);
        cellName = v.findViewById(R.id.CellName);
        recyclerView = v.findViewById(R.id.Recyclerview_cell);
        saveBtn = v.findViewById(R.id.BtnSaveCell);
        list=new ArrayList<>();
        cellDb=new CellDb(getContext());
        list = cellDb.getAllContacts();
        //Setting Data in List
        if (list.size()>0) {
            adapterCell = new AdapterCell(list, getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapterCell);
            adapterCell.notifyDataSetChanged();
        }
        else {
            Toast.makeText(getContext(), "No Data Available...", Toast.LENGTH_SHORT).show();
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cellDb.addContact(new CellModel(cellName.getText().toString().trim()));
                cellName.setText("");
            }
        });
        return v;
    }
}