package com.example.solarapplication;

import static android.graphics.Color.RED;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ReadTag extends AppCompatActivity {
    GraphView linegraph;
    int pageHeight = 1120;
    int pagewidth = 792;

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;
    File filepath = new File(Environment.getExternalStorageDirectory() + "/SolarExcel.xls");
    Button ViewDetails;
    IUHFService iuhfService;
    CardView cardView;
    String s;
    String FF, pmax1, Vmax1, IPMAx1, VOC1, ISC1;
    String IDCheck;
    List<ReadList> readLists;
    String SerialId;
    Double V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, C1, C2, C3, C10, C4, C5, C6, C7, C8, C9;
    String ID;
    List<XmlModel> modelList;
    public TextView t1, t2, t3, t4, t5, t6, t7, t8, t9;
    Button GenerateExcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_tag);
        t1 = findViewById(R.id.Booktitle);
        t2 = findViewById(R.id.SolarCell);
        t3 = findViewById(R.id.MonthPV);
        t4 = findViewById(R.id.MonthSolar);
        ViewDetails = findViewById(R.id.ViewAll);
        t5 = findViewById(R.id.IECcertificate);
        GenerateExcel = findViewById(R.id.GenerateExcel);
        GenerateExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //your code

                }
                createExcelSheet();
                generatePDF();

            }
        });

        modelList = new ArrayList<>();
        readLists = new ArrayList<>();
        ParseXML();
        iuhfService = UHFManager.getUHFService(this);
        iuhfService.openDev();
        cardView = findViewById(R.id.button_Scan);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadData();
            }
        });

//

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

                for (int i = 0; i < modelList.size(); i++) {
                    if (ID.substring(2).matches(modelList.get(i).getId())) {
                        FillFactor.setText(FF);
                        Vmax.setText(Vmax1);
                        ISC.setText(ISC1);
                        Voc.setText(VOC1);
                        Pmax.setText(pmax1);
                        Imax.setText(IPMAx1);
                        manufactureNamePV.setText(modelList.get(i).getPVMName());
                        ModuleName.setText(modelList.get(i).getPVModule());
                        SerialModule.setText(SerialId);
                        manufactureNameSolar.setText(modelList.get(i).getCellMName());
                        MonthPV.setText(modelList.get(i).getPVDate());
                        MonthSolar.setText(modelList.get(i).getCellDate());
                        IECcertificate.setText(modelList.get(i).getIECLab());
                        DateIEC.setText(modelList.get(i).getIECDate());
                        OriginCountry.setText(modelList.get(i).getPVCountry());
                        OriginSolar.setText(modelList.get(i).CellCountry);


                    }
                }
                FillFactor.setText(FF);
                Vmax.setText(Vmax1);
                ISC.setText(ISC1);
                Voc.setText(VOC1);
                Pmax.setText(pmax1);
                Imax.setText(IPMAx1);
//                t7.setText(model_search.getTagID());

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
        System.out.print("VALUE OF DATA " + hex);

    }

    private void SetDAta(String hex) {
        String vv = hex.substring(18, 46);
        System.out.print("VALUE OF DATA " + vv);
        byte[] bytes = hexStringToByteArray(vv);
        SerialId = new String(bytes, StandardCharsets.UTF_8);
        t1.setText(SerialId);

        ID = hex.substring(46, 49);

        t2.setText(ID);
        Integer p = Integer.parseInt(hex.substring(49, 52));
        String max = hex.substring(52, 54);
        pmax1 = p + "." + max;
        t3.setText(pmax1);
        Integer V = Integer.parseInt(hex.substring(54, 56));
        String max1 = hex.substring(56, 58);
        Vmax1 = V + "." + max1;
        t4.setText(Vmax1);
        Integer IP = Integer.parseInt(hex.substring(59, 60));
        String max2 = hex.substring(60, 62);
        IPMAx1 = IP + "." + max2;
        t5.setText(IPMAx1);
        double F1 = Double.parseDouble(hex.substring(63, 65));
        String F2 = hex.substring(65, 67);
        FF = F1 + "." + F2;
        Integer VO = Integer.parseInt(hex.substring(67, 69));
        String OC = hex.substring(65, 67);
        VOC1 = VO + "." + OC;
        Integer IS = Integer.parseInt(hex.substring(71, 73));
        String C = hex.substring(73, 75);
        ISC1 = IS + "." + C;
//        t8.setText("ISC: " + ISC);
        PopulateGraphValue(Double.parseDouble(Vmax1), Double.parseDouble(IPMAx1), Double.parseDouble(VOC1), Double.parseDouble(ISC1));
//        SetData(ID.substring(2));
        for (int i = 0; i < modelList.size(); i++) {
            if (ID.substring(2).matches(modelList.get(i).getId())) {
                Toast.makeText(ReadTag.this, "Matches", Toast.LENGTH_SHORT).show();
                t1.setText(SerialId);
                t2.setText(modelList.get(i).getPVMName());
                t3.setText(pmax1);
                t4.setText(Vmax1);
                t5.setText(IPMAx1);

            }
        }
    }

    private void ParseXML() {
        XmlPullParserFactory parserFactory;
        try {
            InputStream is = getAssets().open("XMLCompanySettings.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("Company");
            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    modelList.add(new XmlModel(getValue("ID", element2), getValue("PVModule", element2), getValue("Warranty", element2), getValue("PVMName", element2), getValue("PVCountry", element2), getValue("PVDate", element2), getValue("CellMName", element2), getValue("CellCountry", element2), getValue("CellDate", element2), getValue("IECLab", element2), getValue("IECDate", element2)));
//                    model = new XmlModel(getValue("ID", element2), getValue("PVModule", element2), getValue("Warranty", element2), getValue("PVMName", element2), getValue("PVCountry", element2), getValue("PVDate", element2), getValue("CellMName", element2), getValue("CellCountry", element2), getValue("CellDate", element2), getValue("IECLab", element2), getValue("IECDate", element2));//                    textView.setText(textView.getText() + "\nName : " + getValue("ID", element2) + "\n");
//                    textView.setText(textView.getText() + "Surname : " + getValue("Warranty", element2) + "\n");
//                    textView.setText(textView.getText() + "-----------------------");
//                    textView.setText(model.getId());

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
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

    private void SetData(String id) {
//        XmlModel xmlModel = new XmlModel();

    }


    public String toHex(String arg) {
        return String.format("%040x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
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
        linegraph.getViewport().setMaxX(50);
        linegraph.getViewport().setMinY(0);
        linegraph.getViewport().setMaxY(10);
        linegraph.getViewport().setYAxisBoundsManual(true);
        linegraph.setCursorMode(true);
        linegraph.setTitle("IV Curve");
        linegraph.setTitleColor(RED);
        linegraph.setTitleTextSize(50);
        linegraph.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        linegraph.setClickable(true);
        lineSeries.setTitle("Current");
        linegraph.getViewport().setScalable(true);

        linegraph.getViewport().setXAxisBoundsManual(true);

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
                Toast.makeText(ReadTag.this, "" + filepath, Toast.LENGTH_SHORT).show();
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

//        canvas.drawBitmap(scaledbmp, 56, 40, paint);

        title1.setColor(RED);
        title1.setTextSize(25);
        title1.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(15);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, R.color.purple_200));

        canvas.drawText("Data of Graph", 400, 150, title);
        canvas.drawText("Solar Application ", 370, 120, title1);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.purple_200));
        title.setTextSize(15);

        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("This is sample document which we have created.", 350, 250, title);

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        File file = new File(Environment.getExternalStorageDirectory(), "Solar.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(ReadTag.this, "PDF file generated successfully." + file, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }

}