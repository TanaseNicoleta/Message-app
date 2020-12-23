package com.example.messageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StartActivity extends AppCompatActivity {

    //AICI apare un splash si apoi o sa fie 2 butoate de register si login
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }
}