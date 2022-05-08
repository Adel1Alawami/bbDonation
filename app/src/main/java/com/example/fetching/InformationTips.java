package com.example.fetching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class InformationTips extends AppCompatActivity {


    CardView gotoBenifits , gotoCautions ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_information_tips);
        getSupportActionBar().hide();

        gotoBenifits= findViewById(R.id.btnGoToBenifits);
        gotoCautions = findViewById(R.id.btnGoToCautions);

        gotoBenifits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), BenifitActivity.class);
                startActivity(myIntent);
            }
        });

        gotoCautions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), CautionActivity.class);
                startActivity(myIntent);
            }
        });




    }
}