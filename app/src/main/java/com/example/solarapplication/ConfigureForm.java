package com.example.solarapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ConfigureForm extends AppCompatActivity implements ReportListener {
    String PVManuName, PVmodleName, CellManuName, LabName, Warranty, CountryPv, CountryCell, DateCell, DatePv, DateLab;
    Button Savebtn, updatebtn, deletebtn;
    Spinner PvModulenameSpinner, wrannty, spinner_Manufacturepv, spinner_Datecertificate, spinner_originpv, spinner_origincell, spinner_Manufacturecell, spinner_Certificate;
    List<ManufactureModel> list;
    LocalDbManufactue dbManufactue;
    List<String> stringList;
    TextView calenderPV, calenderViewcell;
    final Calendar myCalendar = Calendar.getInstance();
    RecyclerView RecyclerviewReport;
    ReportAdapter adapter;
    CoordinatorLayout coordinatorLayout;
    ReportDb reportDb;
    List<ReportModelClass> reportModelClassList;
    Context c = this;
    String idvalue;

    String updateId, DeleteId, updatePVManuName, updatePVmodleName, updateCellManuName, updateLabName, updateWarranty, updateCountryPv, updateCountryCell, updateDateCell, updateDatePv, updateDateLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_form);
        updatebtn = findViewById(R.id.buttonupdate);
        deletebtn = findViewById(R.id.button_delete);
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
        coordinatorLayout = findViewById(R.id.coordinator);
        calenderPV.setText("Date");
        //Method for Left Swipe to Delete
        enableSwipeToDeleteAndUndo();


        deletebtn.setOnClickListener(v -> {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
            View mView = layoutInflaterAndroid.inflate(R.layout.dboperationdailog, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
            alertDialogBuilderUserInput.setView(mView);
            Spinner spinner = mView.findViewById(R.id.spinner_dailog);
            List temp = new ArrayList();
            List<ReportModelClass> reportModelClassList1 = reportDb.getAllContacts();
            for (int i = 0; i < reportModelClassList1.size(); i++) {
                temp.add(reportModelClassList1.get(i).getID());
            }
            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, temp);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    idvalue = String.valueOf(parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Delete", (dialogBox, id) -> {
                        reportDb.deleteRow(updateId);
                        Toast.makeText(getApplicationContext(), "Delete  " + idvalue, Toast.LENGTH_SHORT).show();
                        dialogBox.dismiss();
                        SetupRecycler();
                    })

                    .setNegativeButton("Cancel",
                            (dialogBox, id) -> dialogBox.cancel());

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
        });
        Savebtn.setOnClickListener(v -> {
            reportDb.addContact(new ReportModelClass(PVManuName, PVmodleName, CellManuName, LabName, Warranty, CountryPv, CountryCell, DateCell, DatePv, DateLab));
            SetupRecycler();
            Toast.makeText(c, "Saved... ", Toast.LENGTH_SHORT).show();

        });

        SetupSpinnerModuleName();
        SetupSpinnerPVmodule();
        SetupCellManufacture();
        SetupCertificate();
        SetupRecycler();
        updatebtn.setOnClickListener(v -> {

            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
            View mView = layoutInflaterAndroid.inflate(R.layout.dboperationdailog, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
            alertDialogBuilderUserInput.setView(mView);
            Spinner spinner = mView.findViewById(R.id.spinner_dailog);

            List temp = new ArrayList();
            List<ReportModelClass> reportModelClassList1 = reportDb.getAllContacts();
            for (int i = 0; i < reportModelClassList1.size(); i++) {
                temp.add(reportModelClassList1.get(i).getID());
            }
            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, temp);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    idvalue = String.valueOf(parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Update", (dialogBox, id) -> {
                        reportDb.updateContact(new ReportModelClass(PVManuName, PVmodleName, CellManuName, LabName, Warranty, CountryPv, CountryCell, DateCell, DatePv, DateLab), updateId);
                        Toast.makeText(getApplicationContext(), "Update... " + updateId, Toast.LENGTH_SHORT).show();
                        dialogBox.dismiss();
                        SetupRecycler();
                    })

                    .setNegativeButton("Cancel",
                            (dialogBox, id) -> dialogBox.cancel());

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();


        });
    }

    private void SetupRecycler() {
        reportModelClassList = new ArrayList<>();
        reportModelClassList = reportDb.getAllContacts();
        if (reportModelClassList.size() > 0) {
            adapter = new ReportAdapter(reportModelClassList, this, this);
            RecyclerviewReport.setLayoutManager(new LinearLayoutManager(this));
            RecyclerviewReport.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void SetupSpinnerModuleName() {
        String[] value = new String[]{"1 Years", "2 Years", "3 Years", "4 Years", "5 Years", "6 Years", "7 Years", "8 Years", "9 Years", "9 Years", "10 Years", "11 Years", "12 Years", "13 Years", "14 Years", "15 Years", "16 Years", "17 Years", "18 Years", "19 Years", "20 Years", "21 Years", "22 Years", "23 Years", "24 Years", "25 Years", "26 Years", "27 Years", "28 Years", "29 Years", "30 Years"};


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
        int selectionPosition = spinnerArrayAdapterwarranty.getPosition(updateWarranty);
        wrannty.setSelection(selectionPosition);
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
        int selectionPosition1 = spinnerArrayAdapter.getPosition(updatePVmodleName);
        PvModulenameSpinner.setSelection(selectionPosition1);
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

        int selectionPosition1 = SpinnerCountrty.getPosition(updateCountryPv);
        spinner_originpv.setSelection(selectionPosition1);
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
        int selectionPosition2 = SpinnerCountrty.getPosition(updatePVManuName);
        spinner_Manufacturepv.setSelection(selectionPosition2);

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
        calenderPV.setText(updateDatePv);
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
        int selectionPosition1 = SpinnerCountrty.getPosition(updateCountryCell);
        spinner_origincell.setSelection(selectionPosition1);

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

        int selectionPosition = SpinnerCountrty.getPosition(updateCellManuName);
        spinner_Manufacturecell.setSelection(selectionPosition);

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
        calenderViewcell.setText(updateDateCell);
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
        int selectionPosition1 = Certificatename.getPosition(updateLabName);
        spinner_Certificate.setSelection(selectionPosition1);

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
        int selectionPosition = Certificatename.getPosition(updateDateLab);
        spinner_Certificate.setSelection(selectionPosition);
    }

    //Method For Left swipe for Delete
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final ReportModelClass item = adapter.getData().get(position);
                adapter.removeItem(position);
                DeleteId = item.getID();
                ShowDailog(position, item);
//                reportDb.deleteRow(item.getID());
//                Snackbar snackbar = Snackbar
//                        .make(coordinatorLayout, "Item Deleted from the Database.", Snackbar.LENGTH_LONG);
//                snackbar.setAction("UNDO", view -> {
////                    reportDb.addContact(new ReportModelClass(item.getPVManuName(), item.getPVmodleName(), item.getCellManuName(), item.getLabName(), item.getWarranty(), item.getCountryPv(), item.getCountryCell(), item.getDateCell(), item.getDatePv(), item.getDateLab()));
//
//                    adapter.restoreItem(item, position);
//                    RecyclerviewReport.scrollToPosition(position);
//                });
//
//                snackbar.setActionTextColor(Color.YELLOW);
//                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(RecyclerviewReport);
    }

    @Override
    public void onItemClick(int position) {
        updateId = reportModelClassList.get(position).getID();
        updatePVManuName = reportModelClassList.get(position).getPVManuName();
        updatePVmodleName = reportModelClassList.get(position).getPVmodleName();
        updateCellManuName = reportModelClassList.get(position).getCellManuName();
        updateLabName = reportModelClassList.get(position).getLabName();
        updateWarranty = reportModelClassList.get(position).getWarranty();
        updateCountryPv = reportModelClassList.get(position).getCountryPv();
        updateCountryCell = reportModelClassList.get(position).getCountryCell();
        updateDateCell = reportModelClassList.get(position).getDateCell();
        updateDatePv = reportModelClassList.get(position).getDatePv();
        updateDateLab = reportModelClassList.get(position).getDateLab();
//        Toast.makeText(ConfigureForm.this, reportModelClassList.get(position).getID(), Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setTitle("Update")
                .setMessage("Are you sure you want to update this item in Local Database?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    SetupSpinnerModuleName();
                    SetupSpinnerPVmodule();
                    SetupCertificate();
                    SetupCellManufacture();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();

    }

    private void ShowDailog(int position, ReportModelClass item) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setTitle("Delete")
                .setMessage("Are you sure you want to Delete this item in Local Database?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    reportDb.deleteRow(DeleteId);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(ConfigureForm.this, "Deleted item ID" + DeleteId, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    adapter.restoreItem(item, position);
                    RecyclerviewReport.scrollToPosition(position);
                    dialog.dismiss();
                })
                .show();
    }
}