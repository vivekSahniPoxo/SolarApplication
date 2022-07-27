package com.example.solarapplication.ui.notifications;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solarapplication.AdapterLab;
import com.example.solarapplication.LabModel;
import com.example.solarapplication.LocalDBIEC;
import com.example.solarapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NotificationsFragment extends Fragment {
    TextView textView;
    String DateValue;
    EditText certificatename;
    RecyclerView recyclerView;
    Button btnSave;
    LocalDBIEC localDB;
    List<LabModel> list;
    AdapterLab adapterLab;
    final Calendar myCalendar = Calendar.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        textView = root.findViewById(R.id.textView31);
        btnSave = root.findViewById(R.id.button_Lab);
        recyclerView = root.findViewById(R.id.Recyclerview_Lab);
        certificatename = root.findViewById(R.id.CertificateName);
        list = new ArrayList<>();
        SetRecyclerview();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<String> temp = new ArrayList<>();
                list = localDB.getAllContacts();
                for (int i = 0; i < list.size(); i++) {
                    temp.add(list.get(i).getName());
                }

                String var = certificatename.getText().toString().trim();

                if (var.length() > 0) {
                    if (DateValue != null) {
                        if (!temp.contains(var)) {
                            localDB.addContact(new LabModel(DateValue, var));
                            certificatename.setText("");
                            SetRecyclerview();
                        } else {
                            certificatename.setText("");
                            Toast.makeText(getContext(), "already Register...", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(), "Choose Date...", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    certificatename.setError("Enter Name...");
                }
//                localDB.addContact(new LabModel(DateValue.toString().trim(), certificatename.getText().toString().trim()));
//                certificatename.setText("");
//                SetRecyclerview();
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        return root;
    }

    private void SetRecyclerview() {
        localDB = new LocalDBIEC(getContext());
        list = localDB.getAllContacts();
        if (list.size() > 0) {
            adapterLab = new AdapterLab(getContext(), list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapterLab);
            adapterLab.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "No Data Available...", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        textView.setText(dateFormat.format(myCalendar.getTime()));
        DateValue = String.valueOf(dateFormat.format(myCalendar.getTime()));

    }
}