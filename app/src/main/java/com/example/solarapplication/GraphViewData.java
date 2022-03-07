package com.example.solarapplication;

import static android.graphics.Color.RED;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;

import java.util.ArrayList;
import java.util.List;

public class GraphViewData extends AppCompatActivity {
    GraphView linegraph;
    List<ReadList> readLists;
    String FF, pmax1, Vmax1, IPMAx1, VOC1, ISC1;
    Double V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, C1, C2, C3, C10, C4, C5, C6, C7, C8, C9;
    IUHFService iuhfService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view);

        iuhfService= UHFManager.getUHFService(this);
        iuhfService.openDev();
        readLists=new ArrayList<>();

        linegraph = findViewById(R.id.line_graph);
        ReadData();
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
        linegraph.getViewport().setMaxX(50);
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

        linegraph.getGridLabelRenderer().setPadding(20);
        linegraph.getGridLabelRenderer().setVerticalAxisTitle("Current (I)");
        linegraph.getGridLabelRenderer().setHorizontalAxisTitle("Voltage (V)");
        linegraph.getViewport().setScalable(true);
        linegraph.getViewport().setXAxisBoundsManual(true);
        linegraph.getViewport().setScrollable(true);

//        linegraph.buildDrawingCache();
//        scaledbmp = linegraph.getDrawingCache();
        lineSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                //Toast.makeText(MainActivity.this, "Series1: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
                double pointY = dataPoint.getY();
                double pointX = dataPoint.getX();
                Toast.makeText(GraphViewData.this, pointX + " " + pointY, Toast.LENGTH_SHORT).show();
            }
        });
        lineSeries.setThickness(8);

    }

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
//        SerialId = new String(bytes, StandardCharsets.UTF_8);
//        t1.setText(SerialId);
//
//        ID = hex.substring(46, 49);

        Integer p = Integer.parseInt(hex.substring(49, 52));
        String max = hex.substring(52, 54);
        pmax1 = p + "." + max;
        Integer V = Integer.parseInt(hex.substring(54, 56));
        String max1 = hex.substring(56, 58);
        Vmax1 = V + "." + max1;
        Integer IP = Integer.parseInt(hex.substring(59, 60));
        String max2 = hex.substring(60, 62);
        IPMAx1 = IP + "." + max2;
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
//        for (int i = 0; i < modelList.size(); i++) {
//            if (ID.substring(2).matches(modelList.get(i).getId())) {
//                t1.setText(SerialId);
//                Check();
//                t2.setText(modelList.get(i).getPVMName());
//                t3.setText(pmax1);
//                t4.setText(Vmax1);
//                t5.setText(IPMAx1);
//                Pvmanufacture = modelList.get(i).getPVMName();
//                cellManufacture = modelList.get(i).getCellMName();
//                PVMonth = modelList.get(i).getCellMName();
//                CellMonth = modelList.get(i).getCellDate();
//                PVcountry = modelList.get(i).getPVCountry();
//                CellCountry = modelList.get(i).getCellCountry();
//                Modelname = modelList.get(i).getPVModule();
//                LAb = modelList.get(i).getIECLab();
//                QualityCertificate = modelList.get(i).getCellCountry();
//            }
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


}