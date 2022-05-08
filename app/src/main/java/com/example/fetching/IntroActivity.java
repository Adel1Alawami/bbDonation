package com.example.fetching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private   Button btnNext;
    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator ;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Full Screen | No title

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_intro);

        // hide the Action Bar

    getSupportActionBar().hide();



        sharedPreferences= getApplicationContext().getSharedPreferences("mySharedPreference", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        if(sharedPreferences != null)
        {
            boolean checkedState= sharedPreferences.getBoolean("Checked", false);

            if(checkedState)
            {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
  
        btnNext = findViewById(R.id.button);
    btnNext.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            editor.putBoolean("Checked", true);
            editor.commit();
            finish();
        }
    });
        tabIndicator = findViewById(R.id.tab_Indicator);

        List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Welcome", "we are happy that you use our App", R.drawable.img1));
        mList.add(new ScreenItem("Benifets", "Blood Donating Has Alot of benifits", R.drawable.img2));
        mList.add(new ScreenItem("Help", "Let Us Help Each Other", R.drawable.img3));
//setup viewpager



        screenPager=  findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this , mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // setup tableLayout

        tabIndicator.setupWithViewPager(screenPager);


    }


}