package com.test.uberforhotels;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HotelShowAddedRoomActivity extends AppCompatActivity {

    ListView listView;
    String[] IMAGES = {};
    String[] ROOM_NUMBER = {};
    String[] NUMBER_OF_BEDS = {};
    String[] INTERNET = {};
    String[] ROOM_RENT = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_show_added_room);

        listView = findViewById(R.id.listView);

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
