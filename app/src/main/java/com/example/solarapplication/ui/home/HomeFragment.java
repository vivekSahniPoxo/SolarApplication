package com.example.solarapplication.ui.home;

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

import com.example.solarapplication.AdapterCountry;
import com.example.solarapplication.DataModelClass;
import com.example.solarapplication.LocalDB;
import com.example.solarapplication.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    EditText Country;
    Button btnSave;
    RecyclerView recyclerView;
    LocalDB localDB;
    AdapterCountry adapterCountry;
    List<DataModelClass> list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.Recyclerview_Country);
        Country = root.findViewById(R.id.CountryName);
        btnSave = root.findViewById(R.id.BtnSave);
        localDB=new LocalDB(getContext());
        //Array List Initial
        list=new ArrayList<>();

        //Getting Local DB Data in List
        list = localDB.getAllContacts();
        //Setting Data in List
        if (list.size()>0) {
            adapterCountry = new AdapterCountry(list, getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapterCountry);
            adapterCountry.notifyDataSetChanged();
        }
        else {
            Toast.makeText(getContext(), "No Data Available...", Toast.LENGTH_SHORT).show();
        }
        btnSave.setOnClickListener(v -> {
            localDB.addContact(new DataModelClass(Country.getText().toString().trim()));
            Country.setText("");

        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}