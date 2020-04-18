package com.test.uberforhotels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddRoomsActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    EditText edit_text_file_name, roomNumber, numberOfBeds, internet, roomRent;
    Button choose_image_button, roomSaveButton;
    FirebaseFirestore hotelRoomsStore;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rooms);

        edit_text_file_name = findViewById(R.id.edit_text_file_name);
        roomNumber = findViewById(R.id.roomNumber);
        numberOfBeds = findViewById(R.id.numberOfBeds);
        internet = findViewById(R.id.internet);
        roomRent = findViewById(R.id.roomRent);

        mStorageRef = FirebaseStorage.getInstance().getReference("Room");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Room");

        choose_image_button = findViewById(R.id.choose_image_button);
        roomSaveButton = findViewById(R.id.roomSaveButton);

        hotelRoomsStore = FirebaseFirestore.getInstance();

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
                Toast.makeText(AddRoomsActivity.this, "Room Added Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),AddRoomsActivity.class));
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                               && data != null && data.getData() != null){

        }
    }
    private String getImageExtention(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage() {
        if (mImageUri != null){
            StorageReference imageReference = mStorageRef.child(System.currentTimeMillis()
            + "." + getImageExtention(mImageUri));

            Toast.makeText(AddRoomsActivity.this,mImageUri.getPath(),Toast.LENGTH_LONG).show();
//
            imageReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AddRoomsActivity.this,"Image Uploaded Successfully",Toast.LENGTH_LONG).show();
//                            String uploadId = mDatabaseRef.push().getKey();
//                            mDatabaseRef.child(uploadId).setValue();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddRoomsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this,"No File Selected",Toast.LENGTH_SHORT).show();
        }
    }
}
