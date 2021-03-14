package com.sembiyan.app.ui.customer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sembiyan.app.R;

public class AddNewCustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}