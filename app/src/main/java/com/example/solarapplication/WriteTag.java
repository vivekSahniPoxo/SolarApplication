package com.example.solarapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;
import com.speedata.libuhf.bean.SpdWriteData;
import com.speedata.libuhf.interfaces.OnSpdWriteListener;

import org.apache.commons.codec.binary.Hex;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class WriteTag extends AppCompatActivity implements View.OnClickListener {
    Button ViewGraph,  SearchData;
    CheckBox ViewDetails;
    IUHFService iuhfService;
    EditText SerialInput;
    String SerialNo = "", date, time, Pmaxnew, FillFactor, Voc, Isc, Vmp, Imp, Sno, ModuleID, PVMdlNumber, CellMfgName, CellMfgCuntry, CellMfgDate, ModuleMfg, ModuleMfgCountry, ModuleMfgDate, IECLab, IECDate;
    File filepath = new File(Environment.getExternalStorageDirectory() + "/SolarWriteTagExcel.xls");
    ProgressDialog progressDialog;
    List<WriteDataModel> dataModelList;
    TextView pvmanutxt, cellmanutxt, pvdatetxt, celldatetxt, cellcountrytxt, pvcoiuntrytxt, labnametxt, labdatetxt, pmaxtxt, imaxtxt, voctxt,
            modeltxt, isctxt, fftxt, vmaxtxt, serialnotxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_tag);

        //Binding Components with Java File
        SerialInput = findViewById(R.id.SearchKey);
        ViewGraph = findViewById(R.id.ViewGraph);
        ViewDetails = findViewById(R.id.ViewAllData);
        SearchData = findViewById(R.id.Search_Data);
        progressDialog = new ProgressDialog(this);
        SearchData.setOnClickListener(this::onClick);
        ViewGraph.setOnClickListener(this::onClick);
        ViewDetails.setOnClickListener(this::onClick);

        dataModelList = new ArrayList<>();
        //RFID Module Initialize
        iuhfService = UHFManager.getUHFService(this);
        iuhfService.openDev();
        modeltxt = findViewById(R.id.ModuleName);
        isctxt = findViewById(R.id.ISC);
        fftxt = findViewById(R.id.FillFactor);
        vmaxtxt = findViewById(R.id.Vmax);
        serialnotxt = findViewById(R.id.SerialModule);
        pvmanutxt = findViewById(R.id.PvModule);
        cellmanutxt = findViewById(R.id.SolarCell);
        pvdatetxt = findViewById(R.id.MonthPV);
        celldatetxt = findViewById(R.id.MonthSolar);
        cellcountrytxt = findViewById(R.id.OriginSolar);
        pvcoiuntrytxt = findViewById(R.id.OriginCountry);
        labnametxt = findViewById(R.id.IECcertificate);
        labdatetxt = findViewById(R.id.DateIEC);
        pmaxtxt = findViewById(R.id.Pmax);
        imaxtxt = findViewById(R.id.Imax);
        voctxt = findViewById(R.id.Voc);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ViewGraph:
                Intent intent = new Intent(WriteTag.this, GraphViewData.class);
                intent.putExtra("SNo", Sno);
                intent.putExtra("ModuleID", ModuleID);
                intent.putExtra("PVMdlNumber", PVMdlNumber);
                intent.putExtra("CellMfgName", CellMfgName);
                intent.putExtra("CellMfgCuntry", CellMfgCuntry);
                intent.putExtra("CellMfgDate", CellMfgDate);
                intent.putExtra("ModuleMfg", ModuleMfg);
                intent.putExtra("ModuleMfgCountry", ModuleMfgCountry);
                intent.putExtra("ModuleMfgDate", ModuleMfgDate);
                intent.putExtra("IECLab", IECLab);
                intent.putExtra("IECDate", IECDate);

                startActivity(intent);

                break;
            case R.id.Search_Data:
                try {
                    FetchData(SerialInput.getText().toString().trim());
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Please wait While Writing Data...");
                    progressDialog.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F1) {//KeyEvent { action=ACTION_UP, keyCode=KEYCODE_F1, scanCode=59, metaState=0, flags=0x8, repeatCount=0, eventTime=13517236, downTime=13516959, deviceId=1, source=0x101 }
//            WriteData();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }


    private void createExcelSheet() {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
        HSSFRow row = hssfSheet.createRow(0);
        HSSFRow row1 = hssfSheet.createRow(1);
        HSSFCell cell = row.createCell(0);
//        Row row = sheet.createRow(0);


        cell = (HSSFCell) row.createCell(0);
        cell.setCellValue("SerialNumber");
//        cell.setCellStyle(cellStyle);

        cell = (HSSFCell) row.createCell(1);
        cell.setCellValue("Pmax");
//        cell.setCellStyle(cellStyle);

        cell = (HSSFCell) row.createCell(2);
        cell.setCellValue("Vmax");
//        cell.setCellStyle(cellStyle);

        cell = (HSSFCell) row.createCell(3);
        cell.setCellValue("Imax");
        cell = (HSSFCell) row.createCell(4);
        cell.setCellValue("Isc");
        cell = (HSSFCell) row.createCell(5);
        cell.setCellValue("VSC");
//        cell.setCellStyle(cellStyle);
        cell = (HSSFCell) row.createCell(6);
        cell.setCellValue("FF");
        //Value Here


        cell = (HSSFCell) row1.createCell(0);
        cell.setCellValue(SerialNo);
//        cell.setCellStyle(cellStyle);

        cell = (HSSFCell) row1.createCell(1);
        cell.setCellValue(Pmaxnew);
//        cell.setCellStyle(cellStyle);

        cell = (HSSFCell) row1.createCell(2);
        cell.setCellValue(Vmp);
//        cell.setCellStyle(cellStyle);

        cell = (HSSFCell) row1.createCell(3);
        cell.setCellValue(Imp);
        cell = (HSSFCell) row1.createCell(4);
        cell.setCellValue(Isc);
        cell = (HSSFCell) row1.createCell(5);
        cell.setCellValue(Voc);
//        cell.setCellStyle(cellStyle);
        cell = (HSSFCell) row1.createCell(6);
        cell.setCellValue(FillFactor);
        try {
            if (!filepath.exists()) {

                filepath.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filepath);
            hssfWorkbook.write(fileOutputStream);
            if (fileOutputStream != null) {
                Toast.makeText(WriteTag.this, "" + filepath, Toast.LENGTH_SHORT).show();
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void ErrorCode(int ErrorCode) {
        switch (ErrorCode) {
            case 0:
                progressDialog.dismiss();
                Toast.makeText(WriteTag.this, "Writing Successfully...", Toast.LENGTH_SHORT).show();
                break;
            case -1:
                progressDialog.dismiss();
                Toast.makeText(WriteTag.this, "Writing failure...", Toast.LENGTH_SHORT).show();
                break;
            case -2:
                progressDialog.dismiss();
                Toast.makeText(WriteTag.this, "Incorrect content length", Toast.LENGTH_SHORT).show();
                break;
            case -3:
                progressDialog.dismiss();
                Toast.makeText(WriteTag.this, " Invalid character", Toast.LENGTH_SHORT).show();
                break;
            default:
                progressDialog.dismiss();
                Toast.makeText(WriteTag.this, "Writing Error ", Toast.LENGTH_SHORT).show();
                break;


        }
    }

    private void FetchData(String parameter) throws JSONException {

        String url = "http://164.52.223.163:4502/api/GetbyId";
        JSONObject obj = new JSONObject();
        obj.put("serialNo", parameter);
        obj.put("moduleId", parameter);
        obj.put("formateid", "1");
        RequestQueue queue = Volley.newRequestQueue(this);


        final String requestBody = obj.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {

            try {
                SerialInput.setText("");
                JSONObject object = new JSONObject(response);
                JSONObject technicleSettings_information = object.getJSONObject("technicleSettings_Information");

                SerialNo = technicleSettings_information.getString("Serial number");

                date = technicleSettings_information.getString("date");
                time = technicleSettings_information.getString("time");

                //PMAX DECODING
                Pmaxnew = technicleSettings_information.getString("pmax");


                FillFactor = technicleSettings_information.getString("Fill Factor");


                //Voc DECODING
                Voc = technicleSettings_information.getString("voc");


                //ISC Decoding
                Isc = (technicleSettings_information.getString("isc"));


                //Vmp Decoding
                Vmp = (technicleSettings_information.getString("vmp"));


                //Imp Decoding
                Imp = (technicleSettings_information.getString("imp"));

                String Rs = technicleSettings_information.getString("rs");
                String Rsh = technicleSettings_information.getString("rsh");
                String CEff = technicleSettings_information.getString("C.Eff");
                String MTemp = technicleSettings_information.getString("M.Temp");
                String RefVoltage = technicleSettings_information.getString("refvoltage");
                String RefCurent = technicleSettings_information.getString("RefCurent");
                String RefPmax = technicleSettings_information.getString("refpmax");
                String Irra = technicleSettings_information.getString("irra");
                String Binnumber = technicleSettings_information.getString("Bin number");


                NEWDATA(SerialNo.trim(), Pmaxnew.trim(), Vmp.trim(), Imp.trim(), FillFactor.trim(), Voc.trim(), Isc.trim());
//                String FilterData = PMAX.concat(VMP.concat(IMP.concat(FF.concat(VOC).concat(ISC))));


//                FormattedData = FilterData.replace(" ", "");
//                Toast.makeText(WriteTag.this, "" + FormattedData, Toast.LENGTH_SHORT).show();
////                PopulateGraphValue( Double.parseDouble(Vmp.trim()),
////                        Double.parseDouble(Imp.trim()),
////                        Double.parseDouble(Voc.trim()),
////                        Double.parseDouble(Isc.trim()));
                JSONObject companySettings = object.getJSONObject("companySettings_Information");


                Sno = companySettings.getString("sno");
                ModuleID = companySettings.getString("Module ID");
                PVMdlNumber = companySettings.getString("PV Model Number");
                CellMfgName = companySettings.getString("Cell Mfg Name");
                CellMfgCuntry = companySettings.getString("Cell Mfg Cuntry");
                CellMfgDate = companySettings.getString("Cell Mfg Date");
                ModuleMfg = companySettings.getString("Module Mfg");
                ModuleMfgCountry = companySettings.getString("Module Mfg Country");
                ModuleMfgDate = companySettings.getString("Module Mfg Date");
                IECLab = companySettings.getString("IEC Lab");
                IECDate = companySettings.getString("IEC Date");

//                writeDataModel = new WriteDataModel(SerialNo, date, time, Pmaxnew, FillFactor, Voc, Isc, Vmp, Imp, Sno, ModuleID, PVMdlNumber, CellMfgName, CellMfgCuntry, CellMfgDate, ModuleMfg, ModuleMfgCountry, ModuleMfgDate, IECLab, IECDate);
                dataModelList.add(new WriteDataModel(SerialNo, date, time, Pmaxnew, FillFactor, Voc, Isc, Vmp, Imp, Sno, ModuleID, PVMdlNumber, CellMfgName, CellMfgCuntry, CellMfgDate, ModuleMfg, ModuleMfgCountry, ModuleMfgDate, IECLab, IECDate));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("VOLLEY", response);
//            dialog.dismiss();
        }, error -> {
//            Log.e("VOLLEY Negative", String.valueOf(error.networkResponse.statusCode));
            Toast.makeText(WriteTag.this, "Not Found", Toast.LENGTH_SHORT).show();
            SerialInput.setText("");
            progressDialog.dismiss();

        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                return super.parseNetworkResponse(response);
            }
        };

        queue.add(stringRequest);
    }

    private void NEWDATA(String serialNo, String pmaxnew1, String vmp1, String imp1, String fillFactor1, String voc1, String isc1) {
//        String FilterData = "";
        String newP1, newp2, newVm1, newvm2, newImp1, newImp2, newFF1, newFF2, newVoc1, newVoc2, newISc1, newISc2;

        StringTokenizer tokens = new StringTokenizer(pmaxnew1, ".");
        String first = tokens.nextToken();
        String second = tokens.nextToken();
        if (first.length() >= 3) {
            newP1 = first.substring(0, 3);
        } else {
            newP1 = "0" + first;
            System.out.print("" + newP1);
        }
        if (second.length() > 2) {
            newp2 = second.substring(0, 2);

        } else {
            newp2 = "0" + second;
            System.out.print("" + newp2);
        }
        String FinalPmax = newP1.concat(newp2);
        System.out.print("" + FinalPmax);

//VMAX DECODING
        StringTokenizer tokens4 = new StringTokenizer(vmp1, ".");
        String first4 = tokens4.nextToken();
        String second4 = tokens4.nextToken();
        if (first4.length() >= 2) {
            newVm1 = first4.substring(0, 2);
        } else {
            newVm1 = "0" + first;
            System.out.print("" + newVm1);
        }
        if (second4.length() > 2) {
            newvm2 = second4.substring(0, 2);

        } else {
            newvm2 = "0" + second4;
            System.out.print("" + newvm2);
        }
        String Finalvmp = newVm1.concat(newvm2);
        System.out.print("" + Finalvmp);

        //Decoding IMP
        StringTokenizer tokens5 = new StringTokenizer(imp1, ".");
        String first5 = tokens5.nextToken();
        String second5 = tokens5.nextToken();

        if (first5.length() >= 2) {
            newImp1 = first5.substring(0, 1);
        } else {
            newImp1 = "0" + first5;
            System.out.print("" + newImp1);
        }
        if (second5.length() > 2) {
            newImp2 = second5.substring(0, 2);

        } else {
            newImp2 = "0" + second5;
            System.out.print("" + newImp2);
        }
        String FinalImp = newImp1.concat(newImp2);
        System.out.print("" + FinalImp);


        //Decoding FILL FACTOR
        StringTokenizer tokens1 = new StringTokenizer(fillFactor1, ".");
        String first1 = tokens1.nextToken();
        String second1 = tokens1.nextToken();
        if (first1.length() >= 3) {
            newFF1 = first1.substring(0, 3);
        } else {
            newFF1 = "00" + first1;
            System.out.print("" + newFF1);
        }
        if (second1.length() > 2) {
            newFF2 = second1.substring(0, 2);

        } else {
            newFF2 = "00" + second1;
            System.out.print("" + newFF2);
        }
        String FinalFF = newFF1.concat(newFF2);
        System.out.print("" + FinalFF);

        //Decoding VOC
        StringTokenizer tokens2 = new StringTokenizer(voc1, ".");
        String first2 = tokens2.nextToken();
        String second2 = tokens2.nextToken();
        if (first2.length() >= 2) {
            newVoc1 = first2.substring(0, 2);
        } else {
            newVoc1 = "0" + first2;
            System.out.print("" + newVoc1);
        }
        if (second2.length() > 2) {
            newVoc2 = second2.substring(0, 2);

        } else {
            newVoc2 = "0" + second2;
            System.out.print("" + newVoc2);
        }
        String FinalVoc = newVoc1.concat(newVoc2);
        System.out.print("" + FinalFF);

//Decoding ISC
        StringTokenizer tokens3 = new StringTokenizer(isc1, ".");
        String first3 = tokens3.nextToken();
        String second3 = tokens3.nextToken();

        if (first3.length() >= 2) {
            newISc1 = first3.substring(0, 2);
        } else {
            newISc1 = "0" + first3;
            System.out.print("" + newISc1);
        }
        if (second3.length() > 2) {
            newISc2 = second3.substring(0, 2);

        } else {
            newISc2 = "0" + second3;
            System.out.print("" + newISc2);
        }
        String FinalIsc = newISc1.concat(newISc2);
        System.out.print("" + FinalFF);


        String full = FinalPmax.concat(Finalvmp.concat(FinalImp.concat(FinalFF.concat(FinalVoc.concat(FinalIsc)))));
        convertStringToHex(serialNo, full);

    }

    public String toHex(String arg) {
        return String.format("%x", new BigInteger(1, arg.getBytes()));
    }

    public String convertStringToHex(String str, String formattedData) {
        // display in lowercase, default
        char[] chars = Hex.encodeHex(str.getBytes(StandardCharsets.UTF_8));


        leadingZeros(String.valueOf(chars), formattedData);
        return String.valueOf(chars);

    }

    public String leadingZeros(String s, String formattedData) {
        String lemn;
        if (s.length() >= 46) {
            return s;
        } else {
            lemn = String.format("%0" + (46 - s.length()) + "d%s", 0, s);
            System.out.print("Value of Length" + lemn);
            DataFormatting(lemn, formattedData);
            return lemn;
        }
    }

    public void DataFormatting(String lemn, String formattedData) {
        String NewID = "000" + formattedData;
//        char[] chars1 = Hex.encodeHex(NewID.getBytes(StandardCharsets.UTF_8));
        String FinalDATA = lemn.concat(String.valueOf(NewID).concat("0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"));
        System.out.print("VALUE WITH ID" + FinalDATA);
//        fromHexString(FinalDATA.substring(0, 128));
//        WriteData(FinalDATA.substring(0, 128));
        hexStringToByteArray(FinalDATA.substring(0, 128));
    }

//    public String fromHexString(String hex) {
//        StringBuilder str = new StringBuilder();
//        for (int i = 0; i < hex.length(); i += 2) {
//            str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
//        }
//        WriteData(str.toString());
//
//        return str.toString();
//    }

    public byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        WriteData(b);
        return b;
    }

    public void WriteData(byte[] finalDATA) {
        iuhfService.setOnWriteListener(new OnSpdWriteListener() {
            @Override
            public void getWriteData(SpdWriteData var1) {
                System.out.print("Data Having " + var1.getStatus());

            }
        });
        iuhfService.inventory_start();
//        byte[] bytes = new byte[100];
//
//        String example = toHex(finalDATA);
//        ;
//
//        bytes = example.getBytes();
        int readArea = iuhfService.writeArea(3, 0, 32, "00000000", finalDATA);//1 count = 2 character
        System.out.print("Value" + readArea);
        ErrorCode(readArea);
        Check();
    }
    public void Check() {
        if (ViewDetails.isChecked()) {
            if (filepath.exists()) {
                UpdateExcelWrite();
            } else {
                createExcelSheet();
            }
        }


}

    private void UpdateExcelWrite() {
        Object[][] newStudents = {
                {SerialNo, Pmaxnew, Vmp, Imp, Isc, Voc, FillFactor},
        };

        try {
            //Creating input stream
            FileInputStream inputStream = new FileInputStream(filepath);

            //Creating workbook from input stream
            Workbook workbook = WorkbookFactory.create(inputStream);

            //Reading first sheet of excel file
            Sheet sheet = workbook.getSheetAt(0);

            //Getting the count of existing records
            int rowCount = sheet.getLastRowNum();

            //Iterating new students to update
            for (Object[] student : newStudents) {

                //Creating new row from the next row count
                Row row = sheet.createRow(++rowCount);

                int columnCount = 0;

                //Iterating student informations
                for (Object info : student) {

                    //Creating new cell and setting the value
                    Cell cell = row.createCell(columnCount++);
                    if (info instanceof String) {
                        cell.setCellValue((String) info);
                    } else if (info instanceof Integer) {
                        cell.setCellValue((Integer) info);
                    }
                }
            }
            //Close input stream
            inputStream.close();

            //Crating output stream and writing the updated workbook
            FileOutputStream os = new FileOutputStream(filepath);
            workbook.write(os);

            //Close the workbook and output stream
            workbook.close();
            os.close();
            Toast.makeText(WriteTag.this, "Excel file has been updated successfully.", Toast.LENGTH_SHORT).show();
            System.out.println("Excel file has been updated successfully.");

        } catch (EncryptedDocumentException | IOException e) {
            System.err.println("Exception while updating an existing excel file.");
            Toast.makeText(WriteTag.this, "Exception while updating an existing excel file.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
    }