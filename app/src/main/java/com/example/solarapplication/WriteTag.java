package com.example.solarapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WriteTag extends AppCompatActivity implements View.OnClickListener {
    Button ViewGraph, ViewDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_tag);
        ViewGraph = findViewById(R.id.ViewGraph);
        ViewDetails = findViewById(R.id.ViewAllData);
        ViewGraph.setOnClickListener(this::onClick);
        ViewDetails.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ViewAllData:
                Toast.makeText(WriteTag.this, "View ALL DATA", Toast.LENGTH_SHORT).show();
                break;

            case R.id.ViewGraph:
                Toast.makeText(WriteTag.this, "View Graph", Toast.LENGTH_SHORT).show();
        }
    }
}