package com.example.solarapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    CardView Readtag, Writetag, Configure, Setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Readtag = findViewById(R.id.Read_tab);
        Writetag = findViewById(R.id.Write_tab);
        Configure = findViewById(R.id.Configure_Tab);
        Setting = findViewById(R.id.Setting_tab);
        Readtag.setOnClickListener(this);
        Writetag.setOnClickListener(this);
        Configure.setOnClickListener(this);
        Setting.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        CardView b = (CardView) v;
        switch (b.getId()) {
            case R.id.Read_tab:
                startActivity(new Intent(MainActivity.this, ReadTag.class));
                break;
            case R.id.Write_tab:
                startActivity(new Intent(MainActivity.this, WriteTag.class));
                break;
            case R.id.Configure_Tab:
                startActivity(new Intent(MainActivity.this, ConfigureForm.class));
                break;
            case R.id.Setting_tab:
                startActivity(new Intent(MainActivity.this, SettingForm.class));
                break;
        }
    }
}