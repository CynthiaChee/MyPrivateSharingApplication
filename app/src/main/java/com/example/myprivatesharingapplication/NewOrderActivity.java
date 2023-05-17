package com.example.myprivatesharingapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewOrderActivity extends AppCompatActivity {

    //Initializing variables
    String pickupTime, pickupDate, pickupLocation, dropOffTime, dropOffDate, dropOffLocation, hour, minute, username, receiverName;
    EditText receiverNameInput, pickupLocationEdit, dropOffLocationEdit;
    CalendarView calendarView;
    TimePicker pickupTimePicker;
    Button nextButton;
    double pickupLat, pickupLong, dropOffLat, dropOffLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        //Find view by ID
        username = getIntent().getStringExtra("username");
        nextButton = findViewById(R.id.orderNextButton);
        receiverNameInput = findViewById(R.id.receiverNameEdit);
        calendarView = findViewById(R.id.pickupCalendarView);
        pickupTimePicker = findViewById(R.id.pickUpTimePicker);
        pickupTimePicker.setIs24HourView(true);
        pickupLocationEdit = findViewById(R.id.pickupLocationEdit);
        dropOffLocationEdit = findViewById(R.id.dropOffLocationEdit);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(i,i1,i2);
                pickupDate = dateFormat.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_YEAR,1);
                dropOffDate = dateFormat.format(calendar.getTime());
            }
        });

        //Initialize Places API
        Places.initialize(getApplicationContext(),"(api-key-here)");
        pickupLocationEdit.setFocusable(false);
        dropOffLocationEdit.setFocusable(false);
        //Set pickup location
        pickupLocationEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent pickupLocationIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(NewOrderActivity.this);
                startActivityForResult(pickupLocationIntent, 100);
            }
        });
        //Set drop-off location
        dropOffLocationEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickupLocationEdit.setFocusable(false);
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent dropOffLocationIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(NewOrderActivity.this);
                startActivityForResult(dropOffLocationIntent, 101);
            }
        });

        //If next button is clicked
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiverName = receiverNameInput.getText().toString();

                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = String.valueOf(pickupTimePicker.getHour());
                    minute = String.valueOf(pickupTimePicker.getMinute());
                }
                else {
                    hour = String.valueOf(pickupTimePicker.getCurrentHour());
                    minute = String.valueOf(pickupTimePicker.getCurrentMinute());
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                String time = hour+":"+minute;
                Calendar calendar = Calendar.getInstance();
                Date date = null;
                try {
                    date = dateFormat.parse(time);
                } catch (ParseException exception) {
                    exception.printStackTrace();
                }
                calendar.setTime(date);
                calendar.add(Calendar.HOUR, 3);
                time = dateFormat.format(calendar.getTime());
                String newTime = dateFormat.format(calendar.getTime());
                pickupTime =  time + " " + pickupDate;
                dropOffTime = newTime+ " " + dropOffDate;

                pickupLocation = pickupLocationEdit.getText().toString();
                dropOffLocation = dropOffLocationEdit.getText().toString();
                Intent orderMoreDetailsIntent = new Intent(getApplicationContext(), NewOrderDetailsActivity.class);
                orderMoreDetailsIntent.putExtra("username",username);
                orderMoreDetailsIntent.putExtra("receiverName",receiverName);
                orderMoreDetailsIntent.putExtra("pickupTime",pickupTime);
                orderMoreDetailsIntent.putExtra("dropOffTime", dropOffTime);
                orderMoreDetailsIntent.putExtra("pickupLocation",pickupLocation);
                orderMoreDetailsIntent.putExtra("pickupLatitude", pickupLat);
                orderMoreDetailsIntent.putExtra("pickupLongitude", pickupLong);
                orderMoreDetailsIntent.putExtra("dropOffLocation", dropOffLocation);
                orderMoreDetailsIntent.putExtra("dropOffLatitude", dropOffLat);
                orderMoreDetailsIntent.putExtra("dropOffLongitude", dropOffLong);
                startActivity(orderMoreDetailsIntent);
                finish();
            }
        });
    }

    //Get Latitude and Longitude from Place Autocomplete
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            Place place = Autocomplete.getPlaceFromIntent(data);
            pickupLocationEdit.setText(place.getName());
            pickupLat = place.getLatLng().latitude;
            pickupLong = place.getLatLng().longitude;
        }
        else if (requestCode==101){
            Place place = Autocomplete.getPlaceFromIntent(data);
            dropOffLocationEdit.setText(place.getName());
            dropOffLat = place.getLatLng().latitude;
            dropOffLong = place.getLatLng().longitude;
        }
    }
}
