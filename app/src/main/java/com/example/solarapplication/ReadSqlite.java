package com.example.solarapplication;

import static android.graphics.Color.RED;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;
import com.speedata.libuhf.bean.SpdInventoryData;
import com.speedata.libuhf.interfaces.OnSpdInventoryListener;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadSqlite extends AppCompatActivity {
    GraphView linegraph;
    int pageHeight = 2200;
    int pagewidth = 1800;
    CheckBox ExcelGenerate, PdfGenerate;
    Bitmap bmp, scaledbmp;
    File filepath = new File(Environment.getExternalStorageDirectory() + "/SolarLocalExcel.xls");
    Button ViewDetails;
    IUHFService iuhfService;
    CardView cardView;
    String FF, pmax1, Vmax1, IPMAx1, VOC1, ISC1,Idq;
    List<ReadList> readLists;
    String SerialId;
    Double V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, C1, C2, C3, C10, C4, C5, C6, C7, C8, C9;
    String ID;
    List<ReportModelClass> modelList;
    String TagId, pdfPVManuName, pdfPVmodleName, pdfCellManuName, pdfDatePv, pdfDateCell, pdfLabName, pdfDateLab, pdfCountryPv, pdfcountrycell;
    public TextView t1, t2, t3, t4, t5, t6, t7, t8, t9;
    ReportDb reportDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_sqlite);
        t1 = findViewById(R.id.Booktitle);
        t2 = findViewById(R.id.SolarCell);
        t3 = findViewById(R.id.MonthPV);
        t4 = findViewById(R.id.MonthSolar);
        ViewDetails = findViewById(R.id.ViewAll);
        t5 = findViewById(R.id.IECcertificate);
        PdfGenerate = findViewById(R.id.GeneratePDf);
        ExcelGenerate = findViewById(R.id.GenerateExcel);


        iuhfService = UHFManager.getUHFService(this);
        iuhfService.openDev();
        iuhfService.inventoryStart();
        iuhfService.setOnInventoryListener(new OnSpdInventoryListener() {
            @Override
            public void getInventoryData(SpdInventoryData var1) {
                TagId = var1.getEpc();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        reportDb = new ReportDb(this);
        modelList = new ArrayList<>();
        readLists = new ArrayList<>();


        cardView = findViewById(R.id.button_Scan);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ReadData();
            }
        });
        linegraph = (GraphView) findViewById(R.id.line_graph);
        ViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dailogbox = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.customdailog, null);
                TextView manufactureNamePV = dailogbox.findViewById(R.id.PvModule);
                TextView manufactureNameSolar = dailogbox.findViewById(R.id.SolarCell);
                TextView MonthPV = dailogbox.findViewById(R.id.MonthPV);
                TextView MonthSolar = dailogbox.findViewById(R.id.MonthSolar);
                TextView IECcertificate = dailogbox.findViewById(R.id.IECcertificate);
                TextView DateIEC = dailogbox.findViewById(R.id.DateIEC);
                TextView OriginCountry = dailogbox.findViewById(R.id.OriginCountry);
                TextView OriginSolar = dailogbox.findViewById(R.id.OriginSolar);
                TextView Pmax = dailogbox.findViewById(R.id.Pmax);
                TextView Imax = dailogbox.findViewById(R.id.Imax);
                TextView Voc = dailogbox.findViewById(R.id.Voc);
                TextView SerialModule = dailogbox.findViewById(R.id.SerialModule);
                TextView ModuleName = dailogbox.findViewById(R.id.ModuleName);
                TextView Vmax = dailogbox.findViewById(R.id.Vmax);
                TextView FillFactor = dailogbox.findViewById(R.id.FillFactor);
                TextView ISC = dailogbox.findViewById(R.id.ISC);

                FillFactor.setText(FF);
                Vmax.setText(Vmax1);
                ISC.setText(ISC1);
                Voc.setText(VOC1);
                Pmax.setText(pmax1);
                Imax.setText(IPMAx1);
                manufactureNamePV.setText(pdfPVManuName);
                ModuleName.setText(pdfPVmodleName);
                SerialModule.setText(SerialId);
                manufactureNameSolar.setText(pdfCellManuName);
                MonthPV.setText(pdfDatePv);
                MonthSolar.setText(pdfDateCell);
                IECcertificate.setText(pdfLabName);
                DateIEC.setText(pdfDateLab);
                OriginCountry.setText(pdfCountryPv);
                OriginSolar.setText(pdfcountrycell);


                FillFactor.setText(FF);
                Vmax.setText(Vmax1);
                ISC.setText(ISC1);
                Voc.setText(VOC1);
                Pmax.setText(pmax1);
                Imax.setText(IPMAx1);

                builder.setView(dailogbox);
                builder.setCancelable(true);
                builder.show();

            }
        });

    }


    //Convert Byte Arrray to
    public void convertByteToHexadecimal(byte[] byteArray) {
        String hex = "";
        // Iterating through each byte in the array
        for (byte i : byteArray) {
            hex += String.format("%02X", i);
        }
//        System.out.print(hex);
        SetDAta(hex);
//        SetDAta("00000000000000000000000000000032323431303936350003504839020898000774715095800000000000000000000000000000000000000000000000000000");
        System.out.print("VALUE OF DATA " + hex);

    }

    private void SetDAta(String hex) {
        modelList = reportDb.getAllContacts();
        String vv = hex.substring(18, 46);
        System.out.print("VALUE OF DATA " + vv);
        byte[] bytes = hexStringToByteArray(vv);
        SerialId = new String(bytes, StandardCharsets.UTF_8);
//        t1.setText(SerialId);

        ID = hex.substring(46, 49);
     Idq=ID.substring(2);
//        t2.setText(ID);
        Integer p = Integer.parseInt(hex.substring(49, 52));
        String max = hex.substring(52, 54);
        pmax1 = p + "." + max;
//        t3.setText(pmax1);
        Integer V = Integer.parseInt(hex.substring(54, 56));
        String max1 = hex.substring(56, 58);
        Vmax1 = V + "." + max1;
//        t4.setText(Vmax1);
        Integer IP = Integer.parseInt(hex.substring(59, 60));
        String max2 = hex.substring(60, 62);
        IPMAx1 = IP + "." + max2;
//        t5.setText(IPMAx1);
        Integer F1 = Integer.parseInt(hex.substring(63, 65));
        String F2 = hex.substring(65, 67);
        FF = F1 + "." + F2;
        Integer VO = Integer.parseInt(hex.substring(67, 69));
        String OC = hex.substring(69, 71);
        VOC1 = VO + "." + OC;
        Integer IS = Integer.parseInt(hex.substring(71, 73));
        String C = hex.substring(73, 75);
        ISC1 = IS + "." + C;
        PopulateGraphValue(Double.parseDouble(Vmax1), Double.parseDouble(IPMAx1), Double.parseDouble(VOC1), Double.parseDouble(ISC1));
        for (int i = 0; i < modelList.size(); i++) {
            if (ID.substring(2).matches(modelList.get(i).getID())) {

                pdfPVManuName = modelList.get(i).getPVManuName();
                pdfPVmodleName = modelList.get(i).getPVmodleName();
                pdfCellManuName = modelList.get(i).getCellManuName();
                pdfDatePv = modelList.get(i).getDatePv();
                pdfDateCell = modelList.get(i).getDateCell();
                pdfLabName = modelList.get(i).getLabName();
                pdfDateLab = modelList.get(i).getDateLab();
                pdfCountryPv = modelList.get(i).getCountryPv();
                pdfcountrycell = modelList.get(i).getCountryPv();
                SerialId = modelList.get(i).getID();
//                generatePDF();
                Check();
                t1.setText(pdfPVManuName);
                t2.setText(pdfCellManuName);
                t3.setText(pdfDatePv);
                t4.setText(pdfDateCell);
                t5.setText(pdfLabName);
            }
        }
    }

    public static byte[] hexStringToByteArray(String hex) {
        int l = hex.length();
        byte[] data = new byte[l / 2];
        for (int i = 0; i < l; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    //Read RFID TAg
    public void ReadData() {
        iuhfService.setOnReadListener(var1 -> {
            convertByteToHexadecimal(var1.getReadData());
        });

        iuhfService.inventory_start();
        // Calling Method For Getting the Data OF Card
        int readArea = iuhfService.readArea(3, 0, 32, "00000000");
    }

    private void PopulateGraphValue(double Vpm, double Ipm, double Voc, double Isc) {
        double lStart, lMiddle, lLast, lSecondLast, lCheck;
        lCheck = Math.exp(Voc);
        lStart = 1 / (Isc - Ipm);
        lMiddle = Isc * Math.exp(Voc - Vpm);
        lLast = Ipm * Math.exp(Voc);
        lSecondLast = lStart * lMiddle;
        double lK = -(lSecondLast - lLast);
        double K;
        K = Math.log10(lK);
        double B, A;
        B = Voc * K;
        A = Isc / (Math.exp(-B) - 1);
        double lValue = Vpm / 5;
        double I;
        int lCount = 0;

        for (double V = lValue; V <= Vpm; V = V + lValue) {
            I = V * ((Ipm - Isc) / Vpm) + Isc;
            readLists.add(new ReadList(V, I));

        }
        double lValue1 = (Voc - Vpm) / 5;
        for (double V = Vpm + lValue1; V <= Voc; V = V + lValue1) {
            I = ((V - Voc) * Ipm) / (Vpm - Voc);
            readLists.add(new ReadList(V, I));


        }

        this.V10 = Voc;
        this.C10 = 0.00;

        C1 = readLists.get(0).getI();
        V1 = readLists.get(0).getV();
        C2 = readLists.get(1).getI();
        V2 = readLists.get(1).getV();
        C3 = readLists.get(2).getI();
        V3 = readLists.get(2).getV();
        C4 = readLists.get(3).getI();
        V4 = readLists.get(3).getV();
        C5 = readLists.get(4).getI();
        V5 = readLists.get(4).getV();
        C6 = readLists.get(5).getI();
        V6 = readLists.get(5).getV();
        C7 = readLists.get(6).getI();
        V7 = readLists.get(6).getV();
        C8 = readLists.get(7).getI();
        V8 = readLists.get(7).getV();
        C9 = readLists.get(8).getI();
        V9 = readLists.get(8).getV();
        C10 = readLists.get(9).getI();
        V10 = readLists.get(9).getV();
        LineGraphSeries<DataPoint> lineSeries = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(V1, C1), new DataPoint(V2, C2),
                new DataPoint(V3, C3), new DataPoint(V4, C4),
                new DataPoint(V5, C5), new DataPoint(V6, C6),
                new DataPoint(V7, C7), new DataPoint(V8, C8),
                new DataPoint(V9, C9), new DataPoint(V10, C10),


        });

        linegraph.addSeries(lineSeries);
        linegraph.getViewport().setMinX(0);
        linegraph.getViewport().setBackgroundColor(Color.parseColor("#C8E9E9"));
        linegraph.getViewport().setMaxX(60);
        linegraph.getViewport().setMinY(0);
        linegraph.getViewport().setMaxY(10);
        linegraph.getViewport().setYAxisBoundsManual(true);
        lineSeries.setDrawDataPoints(true);
        linegraph.setTitle("IV Curve");
        linegraph.setTitleColor(RED);
        linegraph.setTitleTextSize(50);
        linegraph.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        linegraph.setClickable(true);
        lineSeries.setTitle("Current");
//        lineSeries.setAnimated(true);

        linegraph.getGridLabelRenderer().setPadding(50);
        linegraph.getGridLabelRenderer().setVerticalAxisTitle("Current (I)");
        linegraph.getGridLabelRenderer().setHorizontalAxisTitle("Voltage (V)");
        linegraph.getViewport().setScalable(true);
        linegraph.getViewport().setXAxisBoundsManual(true);
        linegraph.getViewport().setScrollable(true);

        linegraph.buildDrawingCache();
        scaledbmp = linegraph.getDrawingCache();
        lineSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                //Toast.makeText(MainActivity.this, "Series1: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
                double pointY = dataPoint.getY();
                double pointX = dataPoint.getX();
                Toast.makeText(ReadSqlite.this, pointX + " " + pointY, Toast.LENGTH_SHORT).show();
            }
        });
        lineSeries.setThickness(8);

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
        cell.setCellValue(SerialId);
//        cell.setCellStyle(cellStyle);

        cell = (HSSFCell) row1.createCell(1);
        cell.setCellValue(pmax1);
//        cell.setCellStyle(cellStyle);

        cell = (HSSFCell) row1.createCell(2);
        cell.setCellValue(Vmax1);
//        cell.setCellStyle(cellStyle);

        cell = (HSSFCell) row1.createCell(3);
        cell.setCellValue(IPMAx1);
        cell = (HSSFCell) row1.createCell(4);
        cell.setCellValue(ISC1);
        cell = (HSSFCell) row1.createCell(5);
        cell.setCellValue(ISC1);
//        cell.setCellStyle(cellStyle);
        cell = (HSSFCell) row1.createCell(6);
        cell.setCellValue(FF);
        try {
            if (!filepath.exists()) {

                filepath.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filepath);
            hssfWorkbook.write(fileOutputStream);
            if (fileOutputStream != null) {
                Toast.makeText(ReadSqlite.this, "" + filepath, Toast.LENGTH_SHORT).show();
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void generatePDF() {
        PdfDocument pdfDocument = new PdfDocument();


        Paint paint = new Paint();
        Paint title = new Paint();
        Paint title1 = new Paint();


        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        Canvas canvas = myPage.getCanvas();
        Bitmap resized1 = Bitmap.createScaledBitmap(scaledbmp, 900, 650, true);

        canvas.drawBitmap(resized1, 400, 1300, paint);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", 350, 1300, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------", 350, 1990, title);
// Left Side Graph Box Line
        canvas.drawText("|", 350, 1305, title);
        canvas.drawText("|", 350, 1310, title);
        canvas.drawText("|", 350, 1320, title);
        canvas.drawText("|", 350, 1330, title);
        canvas.drawText("|", 350, 1340, title);
        canvas.drawText("|", 350, 1350, title);
        canvas.drawText("|", 350, 1360, title);
        canvas.drawText("|", 350, 1370, title);
        canvas.drawText("|", 350, 1380, title);
        canvas.drawText("|", 350, 1390, title);
        canvas.drawText("|", 350, 1400, title);
        canvas.drawText("|", 350, 1410, title);
        canvas.drawText("|", 350, 1420, title);
        canvas.drawText("|", 350, 1430, title);
        canvas.drawText("|", 350, 1440, title);
        canvas.drawText("|", 350, 1450, title);
        canvas.drawText("|", 350, 1460, title);
        canvas.drawText("|", 350, 1470, title);
        canvas.drawText("|", 350, 1480, title);
        canvas.drawText("|", 350, 1490, title);
        canvas.drawText("|", 350, 1500, title);
        canvas.drawText("|", 350, 1510, title);
        canvas.drawText("|", 350, 1520, title);
        canvas.drawText("|", 350, 1530, title);
        canvas.drawText("|", 350, 1540, title);
        canvas.drawText("|", 350, 1550, title);
        canvas.drawText("|", 350, 1560, title);
        canvas.drawText("|", 350, 1570, title);
        canvas.drawText("|", 350, 1580, title);
        canvas.drawText("|", 350, 1590, title);
        canvas.drawText("|", 350, 1600, title);
        canvas.drawText("|", 350, 1610, title);
        canvas.drawText("|", 350, 1620, title);
        canvas.drawText("|", 350, 1630, title);
        canvas.drawText("|", 350, 1640, title);
        canvas.drawText("|", 350, 1650, title);
        canvas.drawText("|", 350, 1660, title);
        canvas.drawText("|", 350, 1670, title);
        canvas.drawText("|", 350, 1680, title);
        canvas.drawText("|", 350, 1690, title);
        canvas.drawText("|", 350, 1700, title);
        canvas.drawText("|", 350, 1710, title);
        canvas.drawText("|", 350, 1720, title);
        canvas.drawText("|", 350, 1730, title);
        canvas.drawText("|", 350, 1740, title);
        canvas.drawText("|", 350, 1750, title);
        canvas.drawText("|", 350, 1760, title);
        canvas.drawText("|", 350, 1770, title);
        canvas.drawText("|", 350, 1780, title);
        canvas.drawText("|", 350, 1790, title);
        canvas.drawText("|", 350, 1800, title);
        canvas.drawText("|", 350, 1810, title);
        canvas.drawText("|", 350, 1820, title);
        canvas.drawText("|", 350, 1830, title);
        canvas.drawText("|", 350, 1840, title);
        canvas.drawText("|", 350, 1850, title);
        canvas.drawText("|", 350, 1860, title);
        canvas.drawText("|", 350, 1870, title);
        canvas.drawText("|", 350, 1880, title);
        canvas.drawText("|", 350, 1890, title);
        canvas.drawText("|", 350, 1900, title);
        canvas.drawText("|", 350, 1910, title);
        canvas.drawText("|", 350, 1920, title);
        canvas.drawText("|", 350, 1930, title);
        canvas.drawText("|", 350, 1940, title);
        canvas.drawText("|", 350, 1950, title);
        canvas.drawText("|", 350, 1960, title);
        canvas.drawText("|", 350, 1970, title);
        canvas.drawText("|", 350, 1980, title);
//        canvas.drawText("|", 350, 1990, title);

        //Right Side Line Graph Table
        int xaxis = 1400;
        canvas.drawText("|", xaxis, 1305, title);
        canvas.drawText("|", xaxis, 1310, title);
        canvas.drawText("|", xaxis, 1320, title);
        canvas.drawText("|", xaxis, 1330, title);
        canvas.drawText("|", xaxis, 1340, title);
        canvas.drawText("|", xaxis, 1350, title);
        canvas.drawText("|", xaxis, 1360, title);
        canvas.drawText("|", xaxis, 1370, title);
        canvas.drawText("|", xaxis, 1380, title);
        canvas.drawText("|", xaxis, 1390, title);
        canvas.drawText("|", xaxis, 1400, title);
        canvas.drawText("|", xaxis, 1410, title);
        canvas.drawText("|", xaxis, 1420, title);
        canvas.drawText("|", xaxis, 1430, title);
        canvas.drawText("|", xaxis, 1440, title);
        canvas.drawText("|", xaxis, 1450, title);
        canvas.drawText("|", xaxis, 1460, title);
        canvas.drawText("|", xaxis, 1470, title);
        canvas.drawText("|", xaxis, 1480, title);
        canvas.drawText("|", xaxis, 1490, title);
        canvas.drawText("|", xaxis, 1500, title);
        canvas.drawText("|", xaxis, 1510, title);
        canvas.drawText("|", xaxis, 1520, title);
        canvas.drawText("|", xaxis, 1530, title);
        canvas.drawText("|", xaxis, 1540, title);
        canvas.drawText("|", xaxis, 1550, title);
        canvas.drawText("|", xaxis, 1560, title);
        canvas.drawText("|", xaxis, 1570, title);
        canvas.drawText("|", xaxis, 1580, title);
        canvas.drawText("|", xaxis, 1590, title);
        canvas.drawText("|", xaxis, 1600, title);
        canvas.drawText("|", xaxis, 1610, title);
        canvas.drawText("|", xaxis, 1620, title);
        canvas.drawText("|", xaxis, 1630, title);
        canvas.drawText("|", xaxis, 1640, title);
        canvas.drawText("|", xaxis, 1650, title);
        canvas.drawText("|", xaxis, 1660, title);
        canvas.drawText("|", xaxis, 1670, title);
        canvas.drawText("|", xaxis, 1680, title);
        canvas.drawText("|", xaxis, 1690, title);
        canvas.drawText("|", xaxis, 1700, title);
        canvas.drawText("|", xaxis, 1710, title);
        canvas.drawText("|", xaxis, 1720, title);
        canvas.drawText("|", xaxis, 1730, title);
        canvas.drawText("|", xaxis, 1740, title);
        canvas.drawText("|", xaxis, 1750, title);
        canvas.drawText("|", xaxis, 1760, title);
        canvas.drawText("|", xaxis, 1770, title);
        canvas.drawText("|", xaxis, 1780, title);
        canvas.drawText("|", xaxis, 1790, title);
        canvas.drawText("|", xaxis, 1800, title);
        canvas.drawText("|", xaxis, 1810, title);
        canvas.drawText("|", xaxis, 1820, title);
        canvas.drawText("|", xaxis, 1830, title);
        canvas.drawText("|", xaxis, 1840, title);
        canvas.drawText("|", xaxis, 1850, title);
        canvas.drawText("|", xaxis, 1860, title);
        canvas.drawText("|", xaxis, 1870, title);
        canvas.drawText("|", xaxis, 1880, title);
        canvas.drawText("|", xaxis, 1890, title);
        canvas.drawText("|", xaxis, 1900, title);
        canvas.drawText("|", xaxis, 1910, title);
        canvas.drawText("|", xaxis, 1920, title);
        canvas.drawText("|", xaxis, 1930, title);
        canvas.drawText("|", xaxis, 1940, title);
        canvas.drawText("|", xaxis, 1950, title);
        canvas.drawText("|", xaxis, 1960, title);
        canvas.drawText("|", xaxis, 1970, title);
        canvas.drawText("|", xaxis, 1980, title);
//        canvas.drawText("|", xaxis, 1990, title);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        Bitmap resized = Bitmap.createScaledBitmap(bmp, 350, 200, true);
        canvas.drawBitmap(resized, 700, 10, paint);
        title1.setColor(RED);
        title1.setTextSize(30);
        title1.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(35);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, R.color.black));


//
//        // similarly we are creating anothe]r text and in this
//        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.black));
//        title.setTextSize(20);

        // below line is used for setting
        // our text to center of PDF.
        int xhead = 200, xdata = 1200, xline = 180, Yline = 1700;


        canvas.drawText("|", xline, 240, title);
        canvas.drawText("|", xline, 250, title);
        canvas.drawText("|", xline, 260, title);
        canvas.drawText("|", xline, 270, title);
        canvas.drawText("|", xline, 280, title);
        canvas.drawText("|", xline, 290, title);
        canvas.drawText("|", xline, 300, title);
        canvas.drawText("|", xline, 310, title);
        canvas.drawText("|", xline, 320, title);
        canvas.drawText("|", xline, 330, title);
        canvas.drawText("|", xline, 340, title);
        canvas.drawText("|", xline, 350, title);
        canvas.drawText("|", xline, 360, title);
        canvas.drawText("|", xline, 370, title);
        canvas.drawText("|", xline, 380, title);
        canvas.drawText("|", xline, 390, title);
        canvas.drawText("|", xline, 400, title);
        canvas.drawText("|", xline, 410, title);
        canvas.drawText("|", xline, 420, title);
        canvas.drawText("|", xline, 430, title);
        canvas.drawText("|", xline, 440, title);
        canvas.drawText("|", xline, 450, title);
        canvas.drawText("|", xline, 460, title);
        canvas.drawText("|", xline, 470, title);
        canvas.drawText("|", xline, 480, title);
        canvas.drawText("|", xline, 490, title);
        canvas.drawText("|", xline, 500, title);
        canvas.drawText("|", xline, 510, title);
        canvas.drawText("|", xline, 520, title);
        canvas.drawText("|", xline, 530, title);
        canvas.drawText("|", xline, 540, title);
        canvas.drawText("|", xline, 550, title);
        canvas.drawText("|", xline, 560, title);
        canvas.drawText("|", xline, 570, title);
        canvas.drawText("|", xline, 580, title);
        canvas.drawText("|", xline, 590, title);
        canvas.drawText("|", xline, 600, title);
        canvas.drawText("|", xline, 610, title);
        canvas.drawText("|", xline, 620, title);
        canvas.drawText("|", xline, 630, title);
        canvas.drawText("|", xline, 640, title);
        canvas.drawText("|", xline, 650, title);
        canvas.drawText("|", xline, 660, title);
        canvas.drawText("|", xline, 670, title);
        canvas.drawText("|", xline, 680, title);
        canvas.drawText("|", xline, 690, title);
        canvas.drawText("|", xline, 700, title);
        canvas.drawText("|", xline, 710, title);
        canvas.drawText("|", xline, 720, title);
        canvas.drawText("|", xline, 730, title);
        canvas.drawText("|", xline, 740, title);
        canvas.drawText("|", xline, 750, title);
        canvas.drawText("|", xline, 760, title);
        canvas.drawText("|", xline, 760, title);
        canvas.drawText("|", xline, 770, title);
        canvas.drawText("|", xline, 780, title);
        canvas.drawText("|", xline, 780, title);
        canvas.drawText("|", xline, 790, title);
        canvas.drawText("|", xline, 800, title);
        canvas.drawText("|", xline, 810, title);
        canvas.drawText("|", xline, 820, title);
        canvas.drawText("|", xline, 830, title);
        canvas.drawText("|", xline, 840, title);
        canvas.drawText("|", xline, 850, title);
        canvas.drawText("|", xline, 860, title);
        canvas.drawText("|", xline, 870, title);
        canvas.drawText("|", xline, 880, title);
        canvas.drawText("|", xline, 890, title);
        canvas.drawText("|", xline, 900, title);
        canvas.drawText("|", xline, 910, title);
        canvas.drawText("|", xline, 920, title);
        canvas.drawText("|", xline, 930, title);
        canvas.drawText("|", xline, 940, title);
        canvas.drawText("|", xline, 950, title);
        canvas.drawText("|", xline, 960, title);
        canvas.drawText("|", xline, 960, title);
        canvas.drawText("|", xline, 970, title);
        canvas.drawText("|", xline, 980, title);
        canvas.drawText("|", xline, 990, title);
        canvas.drawText("|", xline, 1000, title);
        canvas.drawText("|", xline, 1010, title);
        canvas.drawText("|", xline, 1020, title);
        canvas.drawText("|", xline, 1030, title);
        canvas.drawText("|", xline, 1030, title);
        canvas.drawText("|", xline, 1040, title);
        canvas.drawText("|", xline, 1050, title);
        canvas.drawText("|", xline, 1060, title);
        canvas.drawText("|", xline, 1070, title);
        canvas.drawText("|", xline, 1080, title);

        //Right Side line of Table
        canvas.drawText("|", Yline, 240, title);
        canvas.drawText("|", Yline, 250, title);
        canvas.drawText("|", Yline, 260, title);
        canvas.drawText("|", Yline, 270, title);
        canvas.drawText("|", Yline, 280, title);
        canvas.drawText("|", Yline, 290, title);
        canvas.drawText("|", Yline, 300, title);
        canvas.drawText("|", Yline, 310, title);
        canvas.drawText("|", Yline, 320, title);
        canvas.drawText("|", Yline, 330, title);
        canvas.drawText("|", Yline, 340, title);
        canvas.drawText("|", Yline, 350, title);
        canvas.drawText("|", Yline, 360, title);
        canvas.drawText("|", Yline, 370, title);
        canvas.drawText("|", Yline, 380, title);
        canvas.drawText("|", Yline, 390, title);
        canvas.drawText("|", Yline, 400, title);
        canvas.drawText("|", Yline, 410, title);
        canvas.drawText("|", Yline, 420, title);
        canvas.drawText("|", Yline, 430, title);
        canvas.drawText("|", Yline, 440, title);
        canvas.drawText("|", Yline, 450, title);
        canvas.drawText("|", Yline, 460, title);
        canvas.drawText("|", Yline, 470, title);
        canvas.drawText("|", Yline, 480, title);
        canvas.drawText("|", Yline, 490, title);
        canvas.drawText("|", Yline, 500, title);
        canvas.drawText("|", Yline, 510, title);
        canvas.drawText("|", Yline, 520, title);
        canvas.drawText("|", Yline, 530, title);
        canvas.drawText("|", Yline, 540, title);
        canvas.drawText("|", Yline, 550, title);
        canvas.drawText("|", Yline, 560, title);
        canvas.drawText("|", Yline, 570, title);
        canvas.drawText("|", Yline, 580, title);
        canvas.drawText("|", Yline, 590, title);
        canvas.drawText("|", Yline, 600, title);
        canvas.drawText("|", Yline, 610, title);
        canvas.drawText("|", Yline, 620, title);
        canvas.drawText("|", Yline, 630, title);
        canvas.drawText("|", Yline, 640, title);
        canvas.drawText("|", Yline, 650, title);
        canvas.drawText("|", Yline, 660, title);
        canvas.drawText("|", Yline, 670, title);
        canvas.drawText("|", Yline, 680, title);
        canvas.drawText("|", Yline, 690, title);
        canvas.drawText("|", Yline, 700, title);
        canvas.drawText("|", Yline, 710, title);
        canvas.drawText("|", Yline, 720, title);
        canvas.drawText("|", Yline, 730, title);
        canvas.drawText("|", Yline, 740, title);
        canvas.drawText("|", Yline, 750, title);
        canvas.drawText("|", Yline, 760, title);
        canvas.drawText("|", Yline, 760, title);
        canvas.drawText("|", Yline, 770, title);
        canvas.drawText("|", Yline, 780, title);
        canvas.drawText("|", Yline, 780, title);
        canvas.drawText("|", Yline, 790, title);
        canvas.drawText("|", Yline, 800, title);
        canvas.drawText("|", Yline, 810, title);
        canvas.drawText("|", Yline, 820, title);
        canvas.drawText("|", Yline, 830, title);
        canvas.drawText("|", Yline, 840, title);
        canvas.drawText("|", Yline, 850, title);
        canvas.drawText("|", Yline, 860, title);
        canvas.drawText("|", Yline, 870, title);
        canvas.drawText("|", Yline, 880, title);
        canvas.drawText("|", Yline, 890, title);
        canvas.drawText("|", Yline, 900, title);
        canvas.drawText("|", Yline, 910, title);
        canvas.drawText("|", Yline, 920, title);
        canvas.drawText("|", Yline, 930, title);
        canvas.drawText("|", Yline, 940, title);
        canvas.drawText("|", Yline, 950, title);
        canvas.drawText("|", Yline, 960, title);
        canvas.drawText("|", Yline, 960, title);
        canvas.drawText("|", Yline, 970, title);
        canvas.drawText("|", Yline, 980, title);
        canvas.drawText("|", Yline, 990, title);
        canvas.drawText("|", Yline, 1000, title);
        canvas.drawText("|", Yline, 1010, title);
        canvas.drawText("|", Yline, 1020, title);
        canvas.drawText("|", Yline, 1030, title);
        canvas.drawText("|", Yline, 1030, title);
        canvas.drawText("|", Yline, 1040, title);
        canvas.drawText("|", Yline, 1050, title);
        canvas.drawText("|", Yline, 1060, title);
        canvas.drawText("|", Yline, 1070, title);
        canvas.drawText("|", Yline, 1080, title);


        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 1100, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 230, title);


        title.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("ID of the Tag", xhead, 260, title);
        canvas.drawText(TagId, xdata, 260, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 285, title);

        canvas.drawText("PV Module Manufacture Name", xhead, 310, title);
        canvas.drawText(pdfPVManuName, xdata, 310, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 335, title);

        canvas.drawText("Month & Year of Pv Module Manufacture", xhead, 360, title);
        canvas.drawText(pdfDatePv, xdata, 360, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 385, title);
        canvas.drawText("Country of Origin of Pv Module ", xhead, 410, title);
        canvas.drawText(pdfCountryPv, xdata, 410, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 435, title);

        canvas.drawText("Unique Serial number of the Module ", xhead, 460, title);
        canvas.drawText(SerialId, xdata, 460, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 485, title);

        canvas.drawText("Model Type", xhead, 510, title);
        canvas.drawText(pdfPVmodleName, xdata, 510, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 535, title);

        canvas.drawText("Max Wattage of the Module (P-max)", xhead, 560, title);
        canvas.drawText(pmax1, xdata, 560, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 585, title);

        canvas.drawText("Max Current of the Module (I-max)", xhead, 610, title);
        canvas.drawText(IPMAx1, xdata, 610, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 635, title);

        canvas.drawText("Max Voltage of the Module (V-max)", xhead, 660, title);
        canvas.drawText(Vmax1, xdata, 660, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 685, title);

        canvas.drawText("Short circuit current of the Module (ISC)", xhead, 710, title);
        canvas.drawText(ISC1, xdata, 710, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 735, title);

        canvas.drawText("Open circuit current of the Module (VOC)", xhead, 760, title);
        canvas.drawText(VOC1, xdata, 760, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 785, title);

        canvas.drawText("Fill Factor of the Module (ISC)", xhead, 810, title);
        canvas.drawText(ISC1, xdata, 810, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 835, title);

        canvas.drawText("Name of the Manufacture of Solar Cell", xhead, 860, title);
        canvas.drawText(pdfCellManuName, xdata, 860, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 885, title);

        canvas.drawText("Month & Year of Solar Cell Manufacture", xhead, 910, title);
        canvas.drawText(pdfDateCell, xdata, 910, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 935, title);

        canvas.drawText("Country of Origin Cell", xhead, 960, title);
        canvas.drawText(pdfcountrycell, xdata, 960, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 985, title);

        canvas.drawText("Date & Year of IEC Pv Module Qualification Certificate", xhead, 1010, title);
        canvas.drawText(pdfDateLab, xdata, 1010, title);
        canvas.drawText("--------------------------------------------------------------------------------------------------------------------------------------------------------", 180, 1035, title);

        canvas.drawText("Name of the test lab Issuing IEC  Certificate", xhead, 1060, title);
        canvas.drawText(pdfLabName, xdata, 1060, title);

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        Date d = new Date();
        CharSequence s  = DateFormat.format("yyyy-MM-dd HH:mm:ss", d.getTime());
        String serialNO=SerialId.concat(s.toString());
        File file = new File(Environment.getExternalStorageDirectory(), serialNO.concat((String) s)+" LocalSolar.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(ReadSqlite.this, "PDF file generated successfully." + file, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F1) {//KeyEvent { action=ACTION_UP, keyCode=KEYCODE_F1, scanCode=59, metaState=0, flags=0x8, repeatCount=0, eventTime=13517236, downTime=13516959, deviceId=1, source=0x101 }
            ReadData();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void Check() {
        if (ExcelGenerate.isChecked()) {
            if (filepath.exists()) {
                UpdateExcel();
            } else {
                createExcelSheet();
            }
        }
            if (PdfGenerate.isChecked()) {
                generatePDF();
            }


    }

    private void UpdateExcel() {
        Object[][] newStudents = {
                {SerialId, pmax1, Vmax1, IPMAx1, ISC1, ISC1, FF},
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
            Toast.makeText(ReadSqlite.this, "Excel file has been updated successfully.", Toast.LENGTH_SHORT).show();
            System.out.println("Excel file has been updated successfully.");

        } catch (EncryptedDocumentException | IOException e) {
            System.err.println("Exception while updating an existing excel file.");
            Toast.makeText(ReadSqlite.this, "Exception while updating an existing excel file.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}