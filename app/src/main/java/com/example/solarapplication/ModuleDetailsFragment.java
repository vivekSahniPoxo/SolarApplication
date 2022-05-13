package com.example.solarapplication;

import android.app.DatePickerDialog;
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


public class ModuleDetailsFragment extends Fragment {
Button button;
RecyclerView recyclerView;
EditText ModuleNametext;
ModuleDB moduleDB;
AdapterModuleDetails adapterModuleDetails;
List<ModuleDetailsModel> list;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_module_details, container, false);
        button=view.findViewById(R.id.BtnSave);
        recyclerView=view.findViewById(R.id.RecyclerviewModule);
        ModuleNametext=view.findViewById(R.id.ModuleNametext);

        moduleDB=new ModuleDB(getContext());
        list=new ArrayList<>();
       SetupRecycler();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduleDB.addContact(new ModuleDetailsModel(ModuleNametext.getText().toString().trim()));
                ModuleNametext.setText("");
                SetupRecycler();
            }
        });

        return view;
    }

    private void SetupRecycler() {
        list = moduleDB.getAllContacts();
        //Setting Data in List
        if (list.size()>0) {
            adapterModuleDetails = new AdapterModuleDetails(list, getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapterModuleDetails);
            adapterModuleDetails.notifyDataSetChanged();
        }
        else {
            Toast.makeText(getContext(), "No Data Available...", Toast.LENGTH_SHORT).show();
        }
    }
}