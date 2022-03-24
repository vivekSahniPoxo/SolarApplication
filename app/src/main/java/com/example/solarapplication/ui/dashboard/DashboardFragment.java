package com.example.solarapplication.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solarapplication.AdapterManufactureName;
import com.example.solarapplication.LocalDbManufactue;
import com.example.solarapplication.ManufactureModel;
import com.example.solarapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    EditText ManufactureName;
    RecyclerView recyclerView;
    Button btnSave;
    LocalDbManufactue localDB;
    List<ManufactureModel> list;
    AdapterManufactureName  adapterManufactureName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ManufactureName = root.findViewById(R.id.ManufactureName);
        list = new ArrayList<>();
        recyclerView = root.findViewById(R.id.Recyclerview_Manfacture);
        btnSave = root.findViewById(R.id.BtnSave);
        localDB = new LocalDbManufactue(getContext());

        //Getting Local DB Data in List
        list = localDB.getallManufactureName();
        //Setting Data in List
        if (list.size()>0) {
            adapterManufactureName = new AdapterManufactureName(list, getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapterManufactureName);
            adapterManufactureName.notifyDataSetChanged();
        }
        else {
            Toast.makeText(getContext(), "No Data Available...", Toast.LENGTH_SHORT).show();
        }
        btnSave.setOnClickListener(v -> {
            localDB.addManufacture(new ManufactureModel(ManufactureName.getText().toString().trim()));
            ManufactureName.setText("");
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}