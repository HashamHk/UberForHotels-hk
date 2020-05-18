package com.test.uberforhotels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddRoomsActivity extends AppCompatActivity {
    private ImageView imageView;
     String hotelEmail;
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText edit_text_file_name, roomNumber, numberOfBeds, internet, roomRent;
    Button choose_image_button, roomSaveButton;
    FirebaseFirestore roomStore;

    private Uri mImageUri;

    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rooms);

        roomNumber = findViewById(R.id.roomNumber);
        numberOfBeds = findViewById(R.id.numberOfBeds);
        internet = findViewById(R.id.internet);
        roomRent = findViewById(R.id.roomRent);
        imageView = findViewById(R.id.imgView);
        choose_image_button = findViewById(R.id.choose_image_button);
        roomSaveButton = findViewById(R.id.roomSaveButton);
        Intent intent = getIntent();
        hotelEmail=intent.getStringExtra("hotelEmail");
        Toast.makeText(AddRoomsActivity.this, hotelEmail, Toast.LENGTH_SHORT).show();

        roomStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        choose_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        roomSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();

                Intent intentAddRoom = new Intent(getApplicationContext(), AddRoomsActivity.class);
                intentAddRoom.putExtra("hotelEmail", hotelEmail);

                startActivity(intentAddRoom);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            mImageUri = data.getData();

                     try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private String getImageExtention(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadImage() {

        if(mImageUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String imageName= UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/"+ imageName);

//            Intent intent = getIntent();
//             hotelEmail=intent.getStringExtra("hotelEmail");
//            Toast.makeText(AddRoomsActivity.this, hotelEmail, Toast.LENGTH_SHORT).show();

            final String numberRoom = roomNumber.getText().toString().trim();
            final String numberBeds = numberOfBeds.getText().toString().trim();
            final String internetRoom = internet.getText().toString().trim();
            final String rentRoom = roomRent.getText().toString().trim();

            Map<String, Object> room = new HashMap<>();
            room.put("imageName", imageName);
            room.put("roomNumber", numberRoom);
            room.put("numberOfBeds", numberBeds);
            room.put("internetAvailability", internetRoom);
            room.put("roomRent", rentRoom);
            room.put("hotelEmail", hotelEmail);

            roomStore.collection("rooms").document()
                    .set(room)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddRoomsActivity.this, "Successfully Added Room", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddRoomsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

            ref.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddRoomsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddRoomsActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
//        Toast.makeText(AddRoomsActivity.this, getImageExtention(mImageUri), Toast.LENGTH_SHORT).show();
    }
}
