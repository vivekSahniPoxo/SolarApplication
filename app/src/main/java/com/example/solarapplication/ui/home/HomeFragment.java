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
        localDB = new LocalDB(getContext());
        //Array List Initial
        list = new ArrayList<>();

        SetupRecycelerview();

        btnSave.setOnClickListener(v -> {
            List<String> temp = new ArrayList<>();
            list = localDB.getAllContacts();
            for (int i = 0; i < list.size(); i++) {
                temp.add(list.get(i).getCountryName());
            }

            String var = Country.getText().toString().trim();

            if (var.length() > 0) {
                if (!temp.contains(var)) {
                    localDB.addContact(new DataModelClass(var));
                    Country.setText("");
                    SetupRecycelerview();
                } else {
                    Country.setText("");
                    Toast.makeText(getContext(), "already Register...", Toast.LENGTH_SHORT).show();
                }
            } else {
                Country.setError("Enter Country Name...");
            }


        });
        return root;
    }

    private void SetupRecycelerview() {
        //Setting Data in List
        list = localDB.getAllContacts();
        if (list.size() > 0) {
            adapterCountry = new AdapterCountry(list, getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapterCountry);
            adapterCountry.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "No Data Available...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}