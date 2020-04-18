package com.test.uberforhotels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class UserHomeActivity extends AppCompatActivity {
    Button goToMap;
    Button logoutUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        goToMap = findViewById(R.id.goToMap);
        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserMapsActivity();
            }
        });
        logoutUserButton = findViewById(R.id.userLogoutButton);
        logoutUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToLogin = new Intent(UserHomeActivity.this, LoginUserActivity.class);
                startActivity(intToLogin);
            }
        });
    }
    public void openUserMapsActivity(){
        Intent intent = new Intent(UserHomeActivity.this, UserMapsActivity.class);
        startActivity(intent);
    }
}
