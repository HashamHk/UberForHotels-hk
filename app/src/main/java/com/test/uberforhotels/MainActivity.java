package com.test.uberforhotels;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

public class MainActivity extends AppCompatActivity {

    protected Button hotelLoginButton, userLoginButton, adminLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        hotelLoginButton= findViewById(R.id.hotelLoginButton);
        userLoginButton= findViewById(R.id.userLoginButton);
        adminLoginButton= findViewById(R.id.adminLoginButton);
        hotelLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHotelLoginActivity();


            }
        });
        userLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserLoginActivity();
            }
        });
        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminLoginActivity();
            }
        });
    }

    public void openHotelLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginHotelActivity.class);
        startActivity(intent);
    }
    public void openUserLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginUserActivity.class);
        startActivity(intent);
    }
    public void openAdminLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginAdminActivity.class);
        startActivity(intent);
    }
}