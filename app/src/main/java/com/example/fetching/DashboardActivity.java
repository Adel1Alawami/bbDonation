package com.example.fetching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.CarrierConfigManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class DashboardActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    CardView btnGotoPosts;
    CardView userProfile;
    CardView gotoTips;
    CardView gps;
    Button signOut;
//   User user;
    @Override



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.dashboard);
        getSupportActionBar().hide();


        mAuth = FirebaseAuth.getInstance();

        gps = findViewById(R.id.gps);
        btnGotoPosts = findViewById(R.id.btnGoToPosts);
        userProfile = findViewById(R.id.UserProfile);
        gotoTips = findViewById(R.id.btnGoToTips);
        signOut = findViewById(R.id.signOut);
        btnGotoPosts.setOnClickListener(view -> {
            Intent myIntent = new Intent(DashboardActivity.this, PostsList.class);
            startActivity(myIntent);
        });


        userProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
            startActivity(intent);
        });

        gotoTips.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), InformationTips.class);
            startActivity(intent);
        });

        // GPS TRACKER

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);




            };


        });
        signOut.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });


    }}



