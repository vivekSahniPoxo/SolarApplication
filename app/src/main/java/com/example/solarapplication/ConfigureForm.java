package com.example.solarapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ConfigureForm extends AppCompatActivity {
    String PVManuName, PVmodleName, CellManuName, LabName, Warranty, CountryPv, CountryCell, DateCell, DatePv, DateLab;
    Button Savebtn;
    Spinner PvModulenameSpinner, wrannty, spinner_Manufacturepv, spinner_Datecertificate, spinner_originpv, spinner_origincell, spinner_Manufacturecell, spinner_Certificate;
    List<ManufactureModel> list;
    LocalDbManufactue dbManufactue;
    List<String> stringList;
    TextView calenderPV, calenderViewcell;
    final Calendar myCalendar = Calendar.getInstance();
    RecyclerView RecyclerviewReport;
    ReportAdapter adapter;
    ReportDb reportDb;
    List<ReportModelClass> reportModelClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_form);

        RecyclerviewReport = findViewById(R.id.RecyclerviewReport);
        PvModulenameSpinner = findViewById(R.id.spinner_PVname);
        wrannty = findViewById(R.id.spinner_warranty);
        spinner_Manufacturepv = findViewById(R.id.spinner_Manufacturepv);
        spinner_originpv = findViewById(R.id.spinner_originpv);
        calenderPV = findViewById(R.id.calenderViewpv);
        spinner_origincell = findViewById(R.id.spinner_origincell);
        spinner_Manufacturecell = findViewById(R.id.spinner_Manufacturecell);
        calenderViewcell = findViewById(R.id.calenderViewcell);
        spinner_Certificate = findViewById(R.id.spinner_Certificate);
        spinner_Datecertificate = findViewById(R.id.spinner_Datecertificate);
        Savebtn = findViewById(R.id.buttonSave);
        reportDb = new ReportDb(this);


        Savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reportDb.addContact(new ReportModelClass(PVManuName, PVmodleName, CellManuName, LabName, Warranty, CountryPv, CountryCell, DateCell, DatePv, DateLab));
                adapter.notifyDataSetChanged();
            }
        });

        SetupSpinnerModuleName();
        SetupSpinnerPVmodule();
        SetupCellManufacture();
        SetupCertificate();
        SetupRecycler();

    }

    private void SetupRecycler() {
        reportModelClassList = new ArrayList<>();
        reportModelClassList = reportDb.getAllContacts();
        if (reportModelClassList.size() > 0) {
            adapter = new ReportAdapter(reportModelClassList, this);
            RecyclerviewReport.setLayoutManager(new LinearLayoutManager(this));
            RecyclerviewReport.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void SetupSpinnerModuleName() {
        String[] value = new String[]{"1 Years", "2 Years", "3 Years", "4 Years", "5 Years"};
        final ArrayAdapter<String> spinnerArrayAdapterwarranty = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, value);
        spinnerArrayAdapterwarranty.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        wrannty.setAdapter(spinnerArrayAdapterwarranty);
        wrannty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Warranty = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stringList = new ArrayList<>();
        dbManufactue = new LocalDbManufactue(this);
        list = new ArrayList<>();
        list = dbManufactue.getallManufactureName();
        for (int i = 0; i < list.size(); i++) {
            stringList.add(list.get(i).getManufactureName());
        }
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, stringList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        PvModulenameSpinner.setAdapter(spinnerArrayAdapter);
        PvModulenameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PVmodleName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void SetupSpinnerPVmodule() {
        //Country name Spinner
        List<DataModelClass> listData = new ArrayList<>();
        List<String> stringList = new ArrayList<>();
        LocalDB localDB = new LocalDB(this);
        listData = localDB.getAllContacts();
        for (int i = 0; i < listData.size(); i++) {
            stringList.add(listData.get(i).getCountryName());
        }
        final ArrayAdapter<String> SpinnerCountrty = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, stringList);
        SpinnerCountrty.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_originpv.setAdapter(SpinnerCountrty);
        spinner_originpv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryPv = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Manufacture name of Pv module
        List<ModuleDetailsModel> modelList = new ArrayList<>();
        ModuleDB moduleDB = new ModuleDB(this);
        List<String> ListDATA = new ArrayList<>();

        modelList = moduleDB.getAllContacts();
        for (int i = 0; i < modelList.size(); i++) {
            ListDATA.add(modelList.get(i).getModuleName());
        }
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ListDATA);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_Manufacturepv.setAdapter(spinnerArrayAdapter);

        spinner_Manufacturepv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PVManuName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        calenderPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ConfigureForm.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }


    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        calenderPV.setText(dateFormat.format(myCalendar.getTime()));
        DatePv = dateFormat.format(myCalendar.getTime());
    }


    private void SetupCellManufacture() {
        //Country name Spinner
        List<DataModelClass> listData = new ArrayList<>();
        List<String> stringList = new ArrayList<>();
        LocalDB localDB = new LocalDB(this);
        listData = localDB.getAllContacts();
        for (int i = 0; i < listData.size(); i++) {
            stringList.add(listData.get(i).getCountryName());
        }
        final ArrayAdapter<String> SpinnerCountrty = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, stringList);
        SpinnerCountrty.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_origincell.setAdapter(SpinnerCountrty);
        spinner_origincell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CellManuName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Cell Manufacture Name
        CellDb cellDb = new CellDb(this);
        List<CellModel> list = new ArrayList<>();
        List<String> Datacell = new ArrayList<>();

        list = cellDb.getAllContacts();

        for (int i = 0; i < list.size(); i++) {
            Datacell.add(list.get(i).getCellManufacture());
        }
        final ArrayAdapter<String> Cellmanuname = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Datacell);
        Cellmanuname.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_Manufacturecell.setAdapter(Cellmanuname);
        spinner_Manufacturecell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryCell = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Date calender
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
//                updateLabel();
                String myFormat = "MM/dd/yy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                calenderViewcell.setText(dateFormat.format(myCalendar.getTime()));
                DateCell = dateFormat.format(myCalendar.getTime());
            }
        };

        calenderViewcell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ConfigureForm.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }

    private void SetupCertificate() {
        LocalDBIEC localDB = new LocalDBIEC(this);
        List<LabModel> list = new ArrayList<>();
        List<String> ValueList = new ArrayList<>();

        list = localDB.getAllContacts();
        for (int i = 0; i < list.size(); i++) {
            ValueList.add(list.get(i).getName());
        }
        final ArrayAdapter<String> Certificatename = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ValueList);
        Certificatename.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_Certificate.setAdapter(Certificatename);
        spinner_Certificate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LabName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Date of  LAB
        List<String> datevale = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            datevale.add(list.get(i).getDate());
        }
        final ArrayAdapter<String> CertificateDate = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, datevale);
        Certificatename.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_Datecertificate.setAdapter(CertificateDate);
        spinner_Datecertificate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DateLab = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}