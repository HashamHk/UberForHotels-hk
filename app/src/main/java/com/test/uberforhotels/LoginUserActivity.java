package com.test.uberforhotels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginUserActivity extends AppCompatActivity {
    protected TextView registerUserText;

    EditText userEmail, userPassword;
    Button getUserLoginButton;
    ProgressBar progressBar;
    FirebaseAuth uAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        progressBar = findViewById(R.id.progressBarLoginUser);
        getUserLoginButton = findViewById(R.id.getUserLoginButton);
        uAuth = FirebaseAuth.getInstance();

        getUserLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser = userEmail.getText().toString().trim();
                String passwordUser = userPassword.getText().toString().trim();

                if (TextUtils.isEmpty(emailUser)){
                    userEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(passwordUser)){
                    userPassword.setError("Password is required");
                    return;
                }
                if (passwordUser.length() < 6){
                    userPassword.setError("Minimum 6 characters required for password.");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                uAuth.signInWithEmailAndPassword(emailUser, passwordUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginUserActivity.this, "User Logged In Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),UserHomeActivity.class));
                        }
                        else {
                            Toast.makeText(LoginUserActivity.this, "Something went wrong ! Please Login again",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        registerUserText=findViewById(R.id.registerUserText);
        registerUserText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterUserActivity();
            }
        });
    }
    public void openRegisterUserActivity() {
        Intent intent = new Intent(LoginUserActivity.this, RegisterUserActivity.class);
        startActivity(intent);
    }
}