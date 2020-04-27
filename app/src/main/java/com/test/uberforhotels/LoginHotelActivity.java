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

public class LoginHotelActivity extends AppCompatActivity {

    EditText hotelEmail, hotelPassword;
    Button getHotelLoginButton;
    ProgressBar progressBar;
    FirebaseAuth hAuth;

    protected TextView hotelRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_hotel);

        hotelEmail = findViewById(R.id.hotelEmail);
        hotelPassword = findViewById(R.id.hotelPassword);
        progressBar = findViewById(R.id.progressBarLoginHotel);
        getHotelLoginButton = findViewById(R.id.hotelLoginButton);
        hAuth = FirebaseAuth.getInstance();

        getHotelLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailHotel = hotelEmail.getText().toString().trim();
                String passwordHotel = hotelPassword.getText().toString().trim();

                if (TextUtils.isEmpty(emailHotel)){
                    hotelEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(passwordHotel)){
                    hotelPassword.setError("Password is required");
                    return;
                }
                if (passwordHotel.length() < 6){
                    hotelPassword.setError("Minimum 6 characters required for password.");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                hAuth.signInWithEmailAndPassword(emailHotel, passwordHotel).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginHotelActivity.this, "Hotel Logged In Successfully",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),HotelHomeActivity.class);
                            intent.putExtra("hotelEmail",emailHotel);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(LoginHotelActivity.this, "Something went wrong ! Please Login again",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        hotelRegisterButton=findViewById(R.id.hotelRegisterButton);
        hotelRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterHotelActivity();
            }
        });
    }
    public void openRegisterHotelActivity() {
        Intent intent = new Intent(LoginHotelActivity.this, RegisterHotelActivity.class);
        startActivity(intent);
    }
}