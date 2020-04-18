package com.test.uberforhotels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterUserActivity extends AppCompatActivity {

    public static final String MAP = "MAP";
    EditText userName, userEmail, userNumber, userPassword;
    Button getRegisterUserButton;
    FirebaseAuth uAuth;
    FirebaseFirestore uStore;
    ProgressBar progressBar;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userNumber = findViewById(R.id.userNumber);
        userPassword = findViewById(R.id.userPassword);
        getRegisterUserButton = findViewById(R.id.getRegisterUser);

        uAuth = FirebaseAuth.getInstance();
        uStore =FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBarRegisterUser);

        if (uAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),UserHomeActivity.class));
            finish();
        }

        getRegisterUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailUser = userEmail.getText().toString().trim();
                String passwordUser = userPassword.getText().toString().trim();
                final String nameUser = userName.getText().toString();
                final String numberUser = userNumber.getText().toString();

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

                uAuth.createUserWithEmailAndPassword(emailUser, passwordUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterUserActivity.this, "User Registered",Toast.LENGTH_SHORT).show();
                            userID = uAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = uStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("nameUser", nameUser);
                            user.put("userEmail", emailUser);
                            user.put("userNumber", numberUser);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(MAP, "onSuccess : User Profile is Created for " + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(MAP, "onFailure : " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),UserHomeActivity.class));
                        }
                        else {
                            Toast.makeText(RegisterUserActivity.this, "Something went wrong ! Please try again." + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

    }
}