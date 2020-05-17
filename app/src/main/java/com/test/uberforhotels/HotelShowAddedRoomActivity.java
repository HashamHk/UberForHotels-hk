package com.test.uberforhotels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class HotelShowAddedRoomActivity extends AppCompatActivity {

    ListView listView;
    String[] IMAGES = {};
    String[] ROOM_NUMBER = {};
    String[] NUMBER_OF_BEDS = {};
    String[] INTERNET = {};
    String[] ROOM_RENT = {};

    Button button;

    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_show_added_room);

        listView = findViewById(R.id.listView);
        button = findViewById(R.id.loadRooms);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reff = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("rooms");
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String roomNumber = dataSnapshot.child("roomNumber").getValue().toString();
//                        String roomRent = dataSnapshot.child("roomRent").getValue().toString();
//                        String numberOfBeds = dataSnapshot.child("numberOfBeds").getValue().toString();
//                        String internetAvailability = dataSnapshot.child("internetAvailability").getValue().toString();

                        getAddedRooms((Map<String,Object>)dataSnapshot.getValue());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            private void getAddedRooms(Map<String, Object> rooms) {
                ArrayList<Long> roomRent = new ArrayList<>();

                for (Map.Entry<String, Object> entry : rooms.entrySet()){

                    Map singleUser = (Map) entry.getValue();

                    roomRent.add((Long) singleUser.get("roomRent"));
                }

                Toast.makeText(HotelShowAddedRoomActivity.this, (CharSequence) roomRent,Toast.LENGTH_SHORT).show();

//                System.out.println(roomRent.toString());
            }
        });

        CustomAdapter customAdapter = new CustomAdapter();

        listView.setAdapter(customAdapter);
    }

    class CustomAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return IMAGES.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.custom_list_layout, null);

            ImageView imageView = (ImageView)findViewById(R.id.imageView_room);
            TextView textView_roomNumber = (TextView)findViewById(R.id.textView_roomNumber);
            TextView textView_numberOfBeds = (TextView)findViewById(R.id.textView_numberOfBeds);
            TextView textView_internet = (TextView)findViewById(R.id.textView_internet);
            TextView textView_roomRent = (TextView)findViewById(R.id.textView_roomRent);

            imageView.setImageResource(Integer.parseInt((IMAGES[i])));
            textView_roomNumber.setText(ROOM_NUMBER[i]);
            textView_numberOfBeds.setText(NUMBER_OF_BEDS[i]);
            textView_internet.setText(INTERNET[i]);
            textView_roomRent.setText(ROOM_RENT[i]);

            return view;
        }
    }
}
