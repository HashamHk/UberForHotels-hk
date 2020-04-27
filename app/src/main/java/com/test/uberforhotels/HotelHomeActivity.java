package com.test.uberforhotels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HotelHomeActivity extends AppCompatActivity {
    Button logoutHotelButton;
    Button addRoomsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_hotel);
        Intent intent = getIntent();
        final String hotelEmail=intent.getStringExtra("hotelEmail");
        addRoomsButton = findViewById(R.id.addRooms);
        addRoomsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToAddRooms = new Intent(HotelHomeActivity.this, AddRoomsActivity.class);
                intToAddRooms.putExtra("hotelEmail", hotelEmail);

                startActivity(intToAddRooms);
            }
        });

        logoutHotelButton = findViewById(R.id.hotelLogoutButton);
        logoutHotelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToLogin = new Intent(HotelHomeActivity.this, LoginHotelActivity.class);
                startActivity(intToLogin);
            }
        });
    }
}