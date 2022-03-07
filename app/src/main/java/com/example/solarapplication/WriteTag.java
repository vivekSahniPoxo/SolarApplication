package com.example.solarapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class WriteTag extends AppCompatActivity implements View.OnClickListener {
    Button ViewGraph, ViewDetails, SearchData;
    IUHFService iuhfService;
    EditText SerialInput;
    File filepath = new File(Environment.getExternalStorageDirectory() + "/SolarExcel.xls");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_tag);

        //Binding Components with Java File
        SerialInput = findViewById(R.id.SearchKey);

        ViewGraph = findViewById(R.id.ViewGraph);
        ViewDetails = findViewById(R.id.ViewAllData);
        SearchData = findViewById(R.id.Search_Data);
        SearchData.setOnClickListener(this::onClick);
        ViewGraph.setOnClickListener(this::onClick);
        ViewDetails.setOnClickListener(this::onClick);


        //RFID Module Initialize
        iuhfService = UHFManager.getUHFService(this);
        iuhfService.openDev();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ViewAllData:
                convertStringToHex(SerialInput.getText().toString());
//                Toast.makeText(WriteTag.this, "View ALL DATA", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ViewGraph:
                startActivity(new Intent(WriteTag.this, GraphViewData.class));
                break;
            case R.id.Search_Data:
                try {
                    FetchData(SerialInput.getText().toString().trim());
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

    public void WriteData() {
        iuhfService.setOnWriteListener(new OnSpdWriteListener() {
            @Override
            public void getWriteData(SpdWriteData var1) {
                System.out.print("Data Having " + var1.getStatus());
            }
        });
        iuhfService.inventory_start();
        byte[] bytes = new byte[10];
        String example = SerialInput.getText().toString().trim();

        bytes = example.getBytes();
        int readArea = iuhfService.writeArea(3, 0, 3, "00000000", bytes);//1 count = 2 character
        System.out.print("Value" + readArea);
        ErrorCode(readArea);
    }

//    private void createExcelSheet() {
//        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
//        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
//        HSSFRow row = hssfSheet.createRow(0);
//        HSSFRow row1 = hssfSheet.createRow(1);
//        HSSFCell cell = row.createCell(0);
////        Row row = sheet.createRow(0);
//
//
//        cell = (HSSFCell) row.createCell(0);
//        cell.setCellValue("SerialNumber");
////        cell.setCellStyle(cellStyle);
//
//        cell = (HSSFCell) row.createCell(1);
//        cell.setCellValue("Pmax");
////        cell.setCellStyle(cellStyle);
//
//        cell = (HSSFCell) row.createCell(2);
//        cell.setCellValue("Vmax");
////        cell.setCellStyle(cellStyle);
//
//        cell = (HSSFCell) row.createCell(3);
//        cell.setCellValue("Imax");
//        cell = (HSSFCell) row.createCell(4);
//        cell.setCellValue("Isc");
//        cell = (HSSFCell) row.createCell(5);
//        cell.setCellValue("VSC");
////        cell.setCellStyle(cellStyle);
//        cell = (HSSFCell) row.createCell(6);
//        cell.setCellValue("FF");
//        //Value Here
//
//
//        cell = (HSSFCell) row1.createCell(0);
//        cell.setCellValue(SerialId);
////        cell.setCellStyle(cellStyle);
//
//        cell = (HSSFCell) row1.createCell(1);
//        cell.setCellValue(pmax1);
////        cell.setCellStyle(cellStyle);
//
//        cell = (HSSFCell) row1.createCell(2);
//        cell.setCellValue(Vmax1);
////        cell.setCellStyle(cellStyle);
//
//        cell = (HSSFCell) row1.createCell(3);
//        cell.setCellValue(IPMAx1);
//        cell = (HSSFCell) row1.createCell(4);
//        cell.setCellValue(ISC1);
//        cell = (HSSFCell) row1.createCell(5);
//        cell.setCellValue(ISC1);
////        cell.setCellStyle(cellStyle);
//        cell = (HSSFCell) row1.createCell(6);
//        cell.setCellValue(FF);
//        try {
//            if (!filepath.exists()) {
//
//                filepath.createNewFile();
//            }
//            FileOutputStream fileOutputStream = new FileOutputStream(filepath);
//            hssfWorkbook.write(fileOutputStream);
//            if (fileOutputStream != null) {
//                Toast.makeText(WriteTag.this, "" + filepath, Toast.LENGTH_SHORT).show();
//                fileOutputStream.flush();
//                fileOutputStream.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
//    }

//    private void generatePDF() {
//        PdfDocument pdfDocument = new PdfDocument();
//
//
//        Paint paint = new Paint();
//        Paint title = new Paint();
//        Paint title1 = new Paint();
//
//
//        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
//
//        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
//
//        Canvas canvas = myPage.getCanvas();
//        Bitmap resized1 = Bitmap.createScaledBitmap(scaledbmp, 900, 650, true);
//
//        canvas.drawBitmap(resized1, 400, 1300, paint);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", 350, 1300, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", 350, 1990, title);
//// Left Side Graph Box Line
//        canvas.drawText("|", 350, 1305, title);
//        canvas.drawText("|", 350, 1310, title);
//        canvas.drawText("|", 350, 1320, title);
//        canvas.drawText("|", 350, 1330, title);
//        canvas.drawText("|", 350, 1340, title);
//        canvas.drawText("|", 350, 1350, title);
//        canvas.drawText("|", 350, 1360, title);
//        canvas.drawText("|", 350, 1370, title);
//        canvas.drawText("|", 350, 1380, title);
//        canvas.drawText("|", 350, 1390, title);
//        canvas.drawText("|", 350, 1400, title);
//        canvas.drawText("|", 350, 1410, title);
//        canvas.drawText("|", 350, 1420, title);
//        canvas.drawText("|", 350, 1430, title);
//        canvas.drawText("|", 350, 1440, title);
//        canvas.drawText("|", 350, 1450, title);
//        canvas.drawText("|", 350, 1460, title);
//        canvas.drawText("|", 350, 1470, title);
//        canvas.drawText("|", 350, 1480, title);
//        canvas.drawText("|", 350, 1490, title);
//        canvas.drawText("|", 350, 1500, title);
//        canvas.drawText("|", 350, 1510, title);
//        canvas.drawText("|", 350, 1520, title);
//        canvas.drawText("|", 350, 1530, title);
//        canvas.drawText("|", 350, 1540, title);
//        canvas.drawText("|", 350, 1550, title);
//        canvas.drawText("|", 350, 1560, title);
//        canvas.drawText("|", 350, 1570, title);
//        canvas.drawText("|", 350, 1580, title);
//        canvas.drawText("|", 350, 1590, title);
//        canvas.drawText("|", 350, 1600, title);
//        canvas.drawText("|", 350, 1610, title);
//        canvas.drawText("|", 350, 1620, title);
//        canvas.drawText("|", 350, 1630, title);
//        canvas.drawText("|", 350, 1640, title);
//        canvas.drawText("|", 350, 1650, title);
//        canvas.drawText("|", 350, 1660, title);
//        canvas.drawText("|", 350, 1670, title);
//        canvas.drawText("|", 350, 1680, title);
//        canvas.drawText("|", 350, 1690, title);
//        canvas.drawText("|", 350, 1700, title);
//        canvas.drawText("|", 350, 1710, title);
//        canvas.drawText("|", 350, 1720, title);
//        canvas.drawText("|", 350, 1730, title);
//        canvas.drawText("|", 350, 1740, title);
//        canvas.drawText("|", 350, 1750, title);
//        canvas.drawText("|", 350, 1760, title);
//        canvas.drawText("|", 350, 1770, title);
//        canvas.drawText("|", 350, 1780, title);
//        canvas.drawText("|", 350, 1790, title);
//        canvas.drawText("|", 350, 1800, title);
//        canvas.drawText("|", 350, 1810, title);
//        canvas.drawText("|", 350, 1820, title);
//        canvas.drawText("|", 350, 1830, title);
//        canvas.drawText("|", 350, 1840, title);
//        canvas.drawText("|", 350, 1850, title);
//        canvas.drawText("|", 350, 1860, title);
//        canvas.drawText("|", 350, 1870, title);
//        canvas.drawText("|", 350, 1880, title);
//        canvas.drawText("|", 350, 1890, title);
//        canvas.drawText("|", 350, 1900, title);
//        canvas.drawText("|", 350, 1910, title);
//        canvas.drawText("|", 350, 1920, title);
//        canvas.drawText("|", 350, 1930, title);
//        canvas.drawText("|", 350, 1940, title);
//        canvas.drawText("|", 350, 1950, title);
//        canvas.drawText("|", 350, 1960, title);
//        canvas.drawText("|", 350, 1970, title);
//        canvas.drawText("|", 350, 1980, title);
////        canvas.drawText("|", 350, 1990, title);
//
//        //Right Side Line Graph Table
//        int xaxis = 1400;
//        canvas.drawText("|", xaxis, 1305, title);
//        canvas.drawText("|", xaxis, 1310, title);
//        canvas.drawText("|", xaxis, 1320, title);
//        canvas.drawText("|", xaxis, 1330, title);
//        canvas.drawText("|", xaxis, 1340, title);
//        canvas.drawText("|", xaxis, 1350, title);
//        canvas.drawText("|", xaxis, 1360, title);
//        canvas.drawText("|", xaxis, 1370, title);
//        canvas.drawText("|", xaxis, 1380, title);
//        canvas.drawText("|", xaxis, 1390, title);
//        canvas.drawText("|", xaxis, 1400, title);
//        canvas.drawText("|", xaxis, 1410, title);
//        canvas.drawText("|", xaxis, 1420, title);
//        canvas.drawText("|", xaxis, 1430, title);
//        canvas.drawText("|", xaxis, 1440, title);
//        canvas.drawText("|", xaxis, 1450, title);
//        canvas.drawText("|", xaxis, 1460, title);
//        canvas.drawText("|", xaxis, 1470, title);
//        canvas.drawText("|", xaxis, 1480, title);
//        canvas.drawText("|", xaxis, 1490, title);
//        canvas.drawText("|", xaxis, 1500, title);
//        canvas.drawText("|", xaxis, 1510, title);
//        canvas.drawText("|", xaxis, 1520, title);
//        canvas.drawText("|", xaxis, 1530, title);
//        canvas.drawText("|", xaxis, 1540, title);
//        canvas.drawText("|", xaxis, 1550, title);
//        canvas.drawText("|", xaxis, 1560, title);
//        canvas.drawText("|", xaxis, 1570, title);
//        canvas.drawText("|", xaxis, 1580, title);
//        canvas.drawText("|", xaxis, 1590, title);
//        canvas.drawText("|", xaxis, 1600, title);
//        canvas.drawText("|", xaxis, 1610, title);
//        canvas.drawText("|", xaxis, 1620, title);
//        canvas.drawText("|", xaxis, 1630, title);
//        canvas.drawText("|", xaxis, 1640, title);
//        canvas.drawText("|", xaxis, 1650, title);
//        canvas.drawText("|", xaxis, 1660, title);
//        canvas.drawText("|", xaxis, 1670, title);
//        canvas.drawText("|", xaxis, 1680, title);
//        canvas.drawText("|", xaxis, 1690, title);
//        canvas.drawText("|", xaxis, 1700, title);
//        canvas.drawText("|", xaxis, 1710, title);
//        canvas.drawText("|", xaxis, 1720, title);
//        canvas.drawText("|", xaxis, 1730, title);
//        canvas.drawText("|", xaxis, 1740, title);
//        canvas.drawText("|", xaxis, 1750, title);
//        canvas.drawText("|", xaxis, 1760, title);
//        canvas.drawText("|", xaxis, 1770, title);
//        canvas.drawText("|", xaxis, 1780, title);
//        canvas.drawText("|", xaxis, 1790, title);
//        canvas.drawText("|", xaxis, 1800, title);
//        canvas.drawText("|", xaxis, 1810, title);
//        canvas.drawText("|", xaxis, 1820, title);
//        canvas.drawText("|", xaxis, 1830, title);
//        canvas.drawText("|", xaxis, 1840, title);
//        canvas.drawText("|", xaxis, 1850, title);
//        canvas.drawText("|", xaxis, 1860, title);
//        canvas.drawText("|", xaxis, 1870, title);
//        canvas.drawText("|", xaxis, 1880, title);
//        canvas.drawText("|", xaxis, 1890, title);
//        canvas.drawText("|", xaxis, 1900, title);
//        canvas.drawText("|", xaxis, 1910, title);
//        canvas.drawText("|", xaxis, 1920, title);
//        canvas.drawText("|", xaxis, 1930, title);
//        canvas.drawText("|", xaxis, 1940, title);
//        canvas.drawText("|", xaxis, 1950, title);
//        canvas.drawText("|", xaxis, 1960, title);
//        canvas.drawText("|", xaxis, 1970, title);
//        canvas.drawText("|", xaxis, 1980, title);
////        canvas.drawText("|", xaxis, 1990, title);
//
//        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
//        Bitmap resized = Bitmap.createScaledBitmap(bmp, 350, 200, true);
//        canvas.drawBitmap(resized, 700, 10, paint);
//        title1.setColor(RED);
//        title1.setTextSize(30);
//        title1.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//
//        // below line is used for setting text size
//        // which we will be displaying in our PDF file.
//        title.setTextSize(35);
//
//        // below line is sued for setting color
//        // of our text inside our PDF file.
//        title.setColor(ContextCompat.getColor(this, R.color.black));
//
//
////
////        // similarly we are creating anothe]r text and in this
////        // we are aligning this text to center of our PDF file.
//        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//        title.setColor(ContextCompat.getColor(this, R.color.black));
////        title.setTextSize(20);
//
//        // below line is used for setting
//        // our text to center of PDF.
//        int xhead = 200, xdata = 1200, xline = 180, Yline = 1700;
//
//
//        canvas.drawText("|", xline, 240, title);
//        canvas.drawText("|", xline, 250, title);
//        canvas.drawText("|", xline, 260, title);
//        canvas.drawText("|", xline, 270, title);
//        canvas.drawText("|", xline, 280, title);
//        canvas.drawText("|", xline, 290, title);
//        canvas.drawText("|", xline, 300, title);
//        canvas.drawText("|", xline, 310, title);
//        canvas.drawText("|", xline, 320, title);
//        canvas.drawText("|", xline, 330, title);
//        canvas.drawText("|", xline, 340, title);
//        canvas.drawText("|", xline, 350, title);
//        canvas.drawText("|", xline, 360, title);
//        canvas.drawText("|", xline, 370, title);
//        canvas.drawText("|", xline, 380, title);
//        canvas.drawText("|", xline, 390, title);
//        canvas.drawText("|", xline, 400, title);
//        canvas.drawText("|", xline, 410, title);
//        canvas.drawText("|", xline, 420, title);
//        canvas.drawText("|", xline, 430, title);
//        canvas.drawText("|", xline, 440, title);
//        canvas.drawText("|", xline, 450, title);
//        canvas.drawText("|", xline, 460, title);
//        canvas.drawText("|", xline, 470, title);
//        canvas.drawText("|", xline, 480, title);
//        canvas.drawText("|", xline, 490, title);
//        canvas.drawText("|", xline, 500, title);
//        canvas.drawText("|", xline, 510, title);
//        canvas.drawText("|", xline, 520, title);
//        canvas.drawText("|", xline, 530, title);
//        canvas.drawText("|", xline, 540, title);
//        canvas.drawText("|", xline, 550, title);
//        canvas.drawText("|", xline, 560, title);
//        canvas.drawText("|", xline, 570, title);
//        canvas.drawText("|", xline, 580, title);
//        canvas.drawText("|", xline, 590, title);
//        canvas.drawText("|", xline, 600, title);
//        canvas.drawText("|", xline, 610, title);
//        canvas.drawText("|", xline, 620, title);
//        canvas.drawText("|", xline, 630, title);
//        canvas.drawText("|", xline, 640, title);
//        canvas.drawText("|", xline, 650, title);
//        canvas.drawText("|", xline, 660, title);
//        canvas.drawText("|", xline, 670, title);
//        canvas.drawText("|", xline, 680, title);
//        canvas.drawText("|", xline, 690, title);
//        canvas.drawText("|", xline, 700, title);
//        canvas.drawText("|", xline, 710, title);
//        canvas.drawText("|", xline, 720, title);
//        canvas.drawText("|", xline, 730, title);
//        canvas.drawText("|", xline, 740, title);
//        canvas.drawText("|", xline, 750, title);
//        canvas.drawText("|", xline, 760, title);
//        canvas.drawText("|", xline, 760, title);
//        canvas.drawText("|", xline, 770, title);
//        canvas.drawText("|", xline, 780, title);
//        canvas.drawText("|", xline, 780, title);
//        canvas.drawText("|", xline, 790, title);
//        canvas.drawText("|", xline, 800, title);
//        canvas.drawText("|", xline, 810, title);
//        canvas.drawText("|", xline, 820, title);
//        canvas.drawText("|", xline, 830, title);
//        canvas.drawText("|", xline, 840, title);
//        canvas.drawText("|", xline, 850, title);
//        canvas.drawText("|", xline, 860, title);
//        canvas.drawText("|", xline, 870, title);
//        canvas.drawText("|", xline, 880, title);
//        canvas.drawText("|", xline, 890, title);
//        canvas.drawText("|", xline, 900, title);
//        canvas.drawText("|", xline, 910, title);
//        canvas.drawText("|", xline, 920, title);
//        canvas.drawText("|", xline, 930, title);
//        canvas.drawText("|", xline, 940, title);
//        canvas.drawText("|", xline, 950, title);
//        canvas.drawText("|", xline, 960, title);
//        canvas.drawText("|", xline, 960, title);
//        canvas.drawText("|", xline, 970, title);
//        canvas.drawText("|", xline, 980, title);
//        canvas.drawText("|", xline, 990, title);
//        canvas.drawText("|", xline, 1000, title);
//        canvas.drawText("|", xline, 1010, title);
//        canvas.drawText("|", xline, 1020, title);
//        canvas.drawText("|", xline, 1030, title);
//        canvas.drawText("|", xline, 1030, title);
//        canvas.drawText("|", xline, 1040, title);
//        canvas.drawText("|", xline, 1050, title);
//        canvas.drawText("|", xline, 1060, title);
//        canvas.drawText("|", xline, 1070, title);
//        canvas.drawText("|", xline, 1080, title);
//
//        //Right Side line of Table
//        canvas.drawText("|", Yline, 240, title);
//        canvas.drawText("|", Yline, 250, title);
//        canvas.drawText("|", Yline, 260, title);
//        canvas.drawText("|", Yline, 270, title);
//        canvas.drawText("|", Yline, 280, title);
//        canvas.drawText("|", Yline, 290, title);
//        canvas.drawText("|", Yline, 300, title);
//        canvas.drawText("|", Yline, 310, title);
//        canvas.drawText("|", Yline, 320, title);
//        canvas.drawText("|", Yline, 330, title);
//        canvas.drawText("|", Yline, 340, title);
//        canvas.drawText("|", Yline, 350, title);
//        canvas.drawText("|", Yline, 360, title);
//        canvas.drawText("|", Yline, 370, title);
//        canvas.drawText("|", Yline, 380, title);
//        canvas.drawText("|", Yline, 390, title);
//        canvas.drawText("|", Yline, 400, title);
//        canvas.drawText("|", Yline, 410, title);
//        canvas.drawText("|", Yline, 420, title);
//        canvas.drawText("|", Yline, 430, title);
//        canvas.drawText("|", Yline, 440, title);
//        canvas.drawText("|", Yline, 450, title);
//        canvas.drawText("|", Yline, 460, title);
//        canvas.drawText("|", Yline, 470, title);
//        canvas.drawText("|", Yline, 480, title);
//        canvas.drawText("|", Yline, 490, title);
//        canvas.drawText("|", Yline, 500, title);
//        canvas.drawText("|", Yline, 510, title);
//        canvas.drawText("|", Yline, 520, title);
//        canvas.drawText("|", Yline, 530, title);
//        canvas.drawText("|", Yline, 540, title);
//        canvas.drawText("|", Yline, 550, title);
//        canvas.drawText("|", Yline, 560, title);
//        canvas.drawText("|", Yline, 570, title);
//        canvas.drawText("|", Yline, 580, title);
//        canvas.drawText("|", Yline, 590, title);
//        canvas.drawText("|", Yline, 600, title);
//        canvas.drawText("|", Yline, 610, title);
//        canvas.drawText("|", Yline, 620, title);
//        canvas.drawText("|", Yline, 630, title);
//        canvas.drawText("|", Yline, 640, title);
//        canvas.drawText("|", Yline, 650, title);
//        canvas.drawText("|", Yline, 660, title);
//        canvas.drawText("|", Yline, 670, title);
//        canvas.drawText("|", Yline, 680, title);
//        canvas.drawText("|", Yline, 690, title);
//        canvas.drawText("|", Yline, 700, title);
//        canvas.drawText("|", Yline, 710, title);
//        canvas.drawText("|", Yline, 720, title);
//        canvas.drawText("|", Yline, 730, title);
//        canvas.drawText("|", Yline, 740, title);
//        canvas.drawText("|", Yline, 750, title);
//        canvas.drawText("|", Yline, 760, title);
//        canvas.drawText("|", Yline, 760, title);
//        canvas.drawText("|", Yline, 770, title);
//        canvas.drawText("|", Yline, 780, title);
//        canvas.drawText("|", Yline, 780, title);
//        canvas.drawText("|", Yline, 790, title);
//        canvas.drawText("|", Yline, 800, title);
//        canvas.drawText("|", Yline, 810, title);
//        canvas.drawText("|", Yline, 820, title);
//        canvas.drawText("|", Yline, 830, title);
//        canvas.drawText("|", Yline, 840, title);
//        canvas.drawText("|", Yline, 850, title);
//        canvas.drawText("|", Yline, 860, title);
//        canvas.drawText("|", Yline, 870, title);
//        canvas.drawText("|", Yline, 880, title);
//        canvas.drawText("|", Yline, 890, title);
//        canvas.drawText("|", Yline, 900, title);
//        canvas.drawText("|", Yline, 910, title);
//        canvas.drawText("|", Yline, 920, title);
//        canvas.drawText("|", Yline, 930, title);
//        canvas.drawText("|", Yline, 940, title);
//        canvas.drawText("|", Yline, 950, title);
//        canvas.drawText("|", Yline, 960, title);
//        canvas.drawText("|", Yline, 960, title);
//        canvas.drawText("|", Yline, 970, title);
//        canvas.drawText("|", Yline, 980, title);
//        canvas.drawText("|", Yline, 990, title);
//        canvas.drawText("|", Yline, 1000, title);
//        canvas.drawText("|", Yline, 1010, title);
//        canvas.drawText("|", Yline, 1020, title);
//        canvas.drawText("|", Yline, 1030, title);
//        canvas.drawText("|", Yline, 1030, title);
//        canvas.drawText("|", Yline, 1040, title);
//        canvas.drawText("|", Yline, 1050, title);
//        canvas.drawText("|", Yline, 1060, title);
//        canvas.drawText("|", Yline, 1070, title);
//        canvas.drawText("|", Yline, 1080, title);
//
//
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 1100, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 230, title);
//
//
//        title.setTextAlign(Paint.Align.LEFT);
//        canvas.drawText("ID of the Tag", xhead, 260, title);
//        canvas.drawText(TagId, xdata, 260, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 285, title);
//
//        canvas.drawText("PV Module Manufacture Name", xhead, 310, title);
//        canvas.drawText(Pvmanufacture, xdata, 310, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 335, title);
//
//        canvas.drawText("Month & Year of Pv Module Manufacture", xhead, 360, title);
//        canvas.drawText(PVMonth, xdata, 360, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 385, title);
//        canvas.drawText("Country of Origin of Pv Module ", xhead, 410, title);
//        canvas.drawText(Pvmanufacture, xdata, 410, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 435, title);
//
//        canvas.drawText("Unique Serial number of the Module ", xhead, 460, title);
//        canvas.drawText(SerialId, xdata, 460, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 485, title);
//
//        canvas.drawText("Model Type", xhead, 510, title);
//        canvas.drawText(Modelname, xdata, 510, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 535, title);
//
//        canvas.drawText("Max Wattage of the Module (P-max)", xhead, 560, title);
//        canvas.drawText(pmax1, xdata, 560, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 585, title);
//
//        canvas.drawText("Max Current of the Module (I-max)", xhead, 610, title);
//        canvas.drawText(IPMAx1, xdata, 610, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 635, title);
//
//        canvas.drawText("Max Voltage of the Module (V-max)", xhead, 660, title);
//        canvas.drawText(Vmax1, xdata, 660, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 685, title);
//
//        canvas.drawText("Short circuit current of the Module (ISC)", xhead, 710, title);
//        canvas.drawText(ISC1, xdata, 710, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 735, title);
//
//        canvas.drawText("Open circuit current of the Module (VOC)", xhead, 760, title);
//        canvas.drawText(VOC1, xdata, 760, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 785, title);
//
//        canvas.drawText("Fill Factor of the Module (ISC)", xhead, 810, title);
//        canvas.drawText(ISC1, xdata, 810, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 835, title);
//
//        canvas.drawText("Name of the Manufacture of Solar Cell", xhead, 860, title);
//        canvas.drawText(cellManufacture, xdata, 860, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 885, title);
//
//        canvas.drawText("Month & Year of Solar Cell Manufacture", xhead, 910, title);
//        canvas.drawText(CellMonth, xdata, 910, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 935, title);
//
//        canvas.drawText("Country of Origin Cell", xhead, 960, title);
//        canvas.drawText(CellCountry, xdata, 960, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 985, title);
//
//        canvas.drawText("Date & Year of IEC Pv Module Qualification Certificate", xhead, 1010, title);
//        canvas.drawText(QualityCertificate, xdata, 1010, title);
//        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 1035, title);
//
//        canvas.drawText("Name of the test lab Issuing IEC  Certificate", xhead, 1060, title);
//        canvas.drawText(LAb, xdata, 1060, title);
//
//        // after adding all attributes to our
//        // PDF file we will be finishing our page.
//        pdfDocument.finishPage(myPage);
//
//        // below line is used to set the name of
//        // our PDF file and its path.
//        File file = new File(Environment.getExternalStorageDirectory(), "Solar.pdf");
//
//        try {
//            // after creating a file name we will
//            // write our PDF file to that location.
//            pdfDocument.writeTo(new FileOutputStream(file));
//
//            // below line is to print toast message
//            // on completion of PDF generation.
//            Toast.makeText(ReadTag.this, "PDF file generated successfully." + file, Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            // below line is used
//            // to handle error
//            e.printStackTrace();
//        }
//        // after storing our pdf to that
//        // location we are closing our PDF file.
//        pdfDocument.close();
//    }

    public void ErrorCode(int ErrorCode) {
        switch (ErrorCode) {
            case 0:
                Toast.makeText(WriteTag.this, "Writing Successfully...", Toast.LENGTH_SHORT).show();
                break;
            case -1:
//                dataEnter.setText("");
                Toast.makeText(WriteTag.this, "Writing failure...", Toast.LENGTH_SHORT).show();
                break;
            case -2:
                Toast.makeText(WriteTag.this, "Incorrect content length", Toast.LENGTH_SHORT).show();
                break;
            case -3:
                Toast.makeText(WriteTag.this, " Invalid character", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(WriteTag.this, "Writing Error ", Toast.LENGTH_SHORT).show();
                break;


        }
    }

    private void FetchData(String trim) throws JSONException {

        String url = "http://164.52.223.163:4502/api/GetbyId";
        JSONObject obj = new JSONObject();
        obj.put("serialNo", "A");
        obj.put("moduleId", trim);
        obj.put("formateid", "1");

        RequestQueue queue = Volley.newRequestQueue(this);


        final String requestBody = obj.toString();
//
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {

            SerialInput.setText("");
            try {
                JSONObject object = new JSONObject(response);
                JSONObject object1 = object.getJSONObject("technicleSettings_Information");
//                JSONArray array = object1.

//              String rd=object.getString("technicleSettings_Information");
//              System.out.print("Value "+rd);

//                JSONObject object1 = (JSONObject) technicleSettings.get(0);
//                String SerialNo = object1.getString("Serial number");
//                String date = object1.getString("date");
//                String Pmaxnew = object1.getString("Pmax");
//                String Time = object1.getString("time");
//                String FillFactor = object1.getString("Fill Factor");
//                String Voc = object1.getString("Voc");
//                String Isc = object1.getString("Isc");
//                String Vmp = object1.getString("Vmp");
//                String Imp = object1.getString("Imp");
//                String Rs = object1.getString("Rs");
//                String Rsh = object1.getString("Rsh");
//                String CEff = object1.getString("C.Eff");
//                String MTemp = object1.getString("M.Temp");
//                String RefVoltage = object1.getString("RefVoltage");
//                String RefCurent = object1.getString("RefCurent");
//                String RefPmax = object1.getString("RefPmax");
//                String Irra = object1.getString("Irra");
//                String Binnumber = object1.getString("Bin number");
//                String JEX = toHex(SerialNo);
//                System.out.print("VAP" + JEX);
//                Toast.makeText(WriteTag.this, ""+Irra, Toast.LENGTH_SHORT).show();
////                PopulateGraphValue( Double.parseDouble(Vmp.trim()),
////                        Double.parseDouble(Imp.trim()),
////                        Double.parseDouble(Voc.trim()),
////                        Double.parseDouble(Isc.trim()));
//                JSONArray companySettings = object.getJSONArray("companySettings_Information");
//                JSONObject object2 = companySettings.getJSONObject(0);
//
//                String Sno = object2.getString("Sno");
//                String ModuleID = object2.getString("Module ID");
//                String PVMdlNumber = object2.getString("PV Model Number");
//                String CellMfgName = object2.getString("Cell Mfg Name");
//                String CellMfgCuntry = object2.getString("Cell Mfg Cuntry");
//                String CellMfgDate = object2.getString("Cell Mfg Date");
//                String ModuleMfg = object2.getString("Module Mfg");
//                String ModuleMfgCountry = object2.getString("Module Mfg Country");
//                String ModuleMfgDate = object2.getString("Module Mfg Date");
//                String IECLab = object2.getString("IEC Lab");


                Toast.makeText(WriteTag.this, response, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("VOLLEY", response);
//            dialog.dismiss();
        }, error -> {
            Toast.makeText(WriteTag.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            SerialInput.setText("");
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
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5, 2,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public String toHex(String arg) {
        return String.format("%x", new BigInteger(1, arg.getBytes()));
    }

    public static String convertStringToHex(String str) {
        // display in lowercase, default
        char[] chars = Hex.encodeHex(str.getBytes(StandardCharsets.UTF_8));

        leadingZeros(String.valueOf(chars));
        return String.valueOf(chars);

    }

    public static String leadingZeros(String s) {
        String lemn;
        if (s.length() >= 45) {
            return s;
        } else {
             lemn = String.format("%0" + (45 - s.length()) + "d%s", 0, s);
            System.out.print("Value of Length"+lemn);
            DataFormatting(lemn);
            return lemn;
        }
    }

    public  static  void DataFormatting(String lemn)
    {
     String NewID=lemn.concat("000");
     System.out.print("VALUE WITH ID"+NewID);
    }
}