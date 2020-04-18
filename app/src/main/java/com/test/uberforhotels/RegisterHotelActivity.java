package com.test.uberforhotels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.google.firebase.firestore.GeoPoint;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;
import com.sucho.placepicker.PlacePickerActivity;

import java.util.HashMap;
import java.util.Map;

public class RegisterHotelActivity extends AppCompatActivity {

    public static final String MAP = "MAP";
    EditText hotelName, hotelEmail, hotelNumber, hotelPassword, hotelLatitude, hotelLongitude;
    Button getRegisterHotelButton, placePickerButton;
    FirebaseAuth hAuth;
    ProgressBar progressBar;
    FirebaseFirestore hStore;
    String hotelID;
    float lat;
    float lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_hotel);

        hotelName = findViewById(R.id.hotelName);
        hotelEmail = findViewById(R.id.hotelEmail);
        hotelNumber = findViewById(R.id.hotelNumber);
        hotelPassword = findViewById(R.id.hotelPassword);
        placePickerButton = findViewById(R.id.placePicker);
        hotelLatitude = findViewById(R.id.hotelLatitude);
        hotelLongitude = findViewById(R.id.hotelLongitude);
        getRegisterHotelButton = findViewById(R.id.getRegisterHotel);

        hAuth = FirebaseAuth.getInstance();
        hStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBarRegisterHotel);

        if (hAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),HotelHomeActivity.class));
            finish();
        }

        placePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String apiKey=getResources().getString(R.string.google_maps_key);

                Intent intent = new PlacePicker.IntentBuilder()
                        .setLatLong(33.6844, 73.0479)  // Initial Latitude and Longitude the Map will load into
                        .showLatLong(true)  // Show Coordinates in the Activity
                        .setMapZoom(12.0f)  // Map Zoom Level. Default: 14.0
                        .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                        .hideMarkerShadow(true) // Hides the shadow under the map marker. Default: False
                        .setMarkerImageImageColor(R.color.colorPrimary)
                        .setMapType(MapType.NORMAL)
                        .setPlaceSearchBar(true, apiKey)
                        .onlyCoordinates(true)  //Get only Coordinates from Place Picker
                        .build(RegisterHotelActivity.this);
                startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
            }
        });

        getRegisterHotelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailHotel = hotelEmail.getText().toString().trim();
                String passwordHotel = hotelPassword.getText().toString().trim();
                final String nameHotel = hotelName.getText().toString();
                final String numberHotel = hotelNumber.getText().toString();
                final String latitudeHotel = hotelLatitude.getText().toString();
                final String longitudeHotel = hotelLongitude.getText().toString();


                if (TextUtils.isEmpty(emailHotel)){
                    hotelEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(passwordHotel)){
                    hotelPassword.setError("Password is required");
                    return;
                }
                if (TextUtils.isEmpty(latitudeHotel)){
                    hotelLatitude.setError("Latitude is required");
                    return;
                }
                if (TextUtils.isEmpty(longitudeHotel)){
                    hotelLongitude.setError("Longitude is required");
                    return;
                }
                if (passwordHotel.length() < 6){
                    hotelPassword.setError("Minimum 6 characters required for password.");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                hAuth.createUserWithEmailAndPassword(emailHotel, passwordHotel).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterHotelActivity.this, "Hotel Registered",Toast.LENGTH_SHORT).show();
                            hotelID = hAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = hStore.collection("hotels").document(hotelID);
                            Map<String, Object> hotel = new HashMap<>();
                            hotel.put("nameHotel", nameHotel);
                            hotel.put("hotelEmail", emailHotel);
                            hotel.put("hotelNumber", numberHotel);

                            GeoPoint locationOfHotel = new GeoPoint(lat, lon);

                            hotel.put("hotelLocation", locationOfHotel);

                            documentReference.set(hotel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(MAP, "onSuccess : Hotel Profile is Created for " + hotelID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(MAP, "onFailure : " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),HotelHomeActivity.class));
                        }
                        else {
                            Toast.makeText(RegisterHotelActivity.this, "Something went wrong ! Please try again." + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);


                lat=(float)addressData.getLatitude();
                lon=(float)addressData.getLongitude();


                Toast.makeText(RegisterHotelActivity.this, (String)Float.toString(lat),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}