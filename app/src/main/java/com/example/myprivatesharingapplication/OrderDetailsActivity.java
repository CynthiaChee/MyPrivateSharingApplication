package com.example.myprivatesharingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class OrderDetailsActivity extends AppCompatActivity {

    //Initializing variables
    TextView senderText, pickupDetails, receiverText, dropOffDetails, weightText, goodTypeText, widthText, heightText, lengthText, quantityText;
    ImageView orderDetailsImage;
    Button getEstimateButton;
    String pickupLocation, pickupTime, dropOffLocation, dropOfftime;
    String sender, receiver, pickupDet, dropOffDet, goodWeight, goodType, goodWidth, goodHeight, goodLength, goodQuantity;
    Double pickupLat, pickupLong, dropOffLat, dropOffLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        //Find view by ID
        senderText = findViewById(R.id.senderTextView);
        pickupDetails = findViewById(R.id.orderPickUpDetails);
        receiverText = findViewById(R.id.orderReceiverText);
        dropOffDetails = findViewById(R.id.orderDropOffDetails);
        weightText = findViewById(R.id.goodWeightTextView);
        goodTypeText = findViewById(R.id.goodTypeTextView);
        widthText = findViewById(R.id.goodWidthTextView);
        heightText = findViewById(R.id.goodHeightTextView);
        lengthText = findViewById(R.id.goodLengthTextView);
        quantityText = findViewById(R.id.goodQuantityTextView);
        orderDetailsImage = findViewById(R.id.orderDetailsImage);
        getEstimateButton = findViewById(R.id.getEstimateButton);

        //Get data from previous intent
        pickupLocation = getIntent().getStringExtra("pickupLocation");
        pickupTime = getIntent().getStringExtra("pickuptime");
        pickupLat = getIntent().getDoubleExtra("pickupLatitude",0);
        pickupLong = getIntent().getDoubleExtra("pickupLongitude",0);
        dropOffLocation = getIntent().getStringExtra("dropOffLocation");
        dropOffLat = getIntent().getDoubleExtra("dropOffLatitude",0);
        dropOffLong = getIntent().getDoubleExtra("dropOffLongitude",0);
        dropOfftime = getIntent().getStringExtra("dropOfftime");

        //Getting delivery values
        sender = "Sender: " + getIntent().getStringExtra("username");
        receiver = "Receiver: " + getIntent().getStringExtra("receiverName");

        //Pickup date and time
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        pickupDet = "Pickup location: " + getIntent().getStringExtra("pickupLocation");
        dropOffDet = "Drop-off location: " + getIntent().getStringExtra("dropOffLocation");

        //Getting goods values
        goodWeight = "Weight: \n" + getIntent().getStringExtra("weight");
        goodType = "Good type: \n" + getIntent().getStringExtra("type");
        goodWidth = "Width: \n" + getIntent().getStringExtra("width");
        goodHeight = "Height: \n" + getIntent().getStringExtra("height");
        goodLength = "Length: \n" + getIntent().getStringExtra("length");
        goodQuantity = "Quantity: \n" + getIntent().getStringExtra("quantity");

        //Setting values to text views
        senderText.setText(sender);
        pickupDetails.setText(pickupDet);
        receiverText.setText(receiver);
        dropOffDetails.setText(dropOffDet);
        weightText.setText(goodWeight);
        goodTypeText.setText(goodType);
        widthText.setText(goodWidth);
        heightText.setText(goodHeight);
        lengthText.setText(goodLength);
        quantityText.setText(goodQuantity);

        orderDetailsImage.setImageResource(R.drawable.person_icon);

        //If get estimate button is clicked
        getEstimateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapActivityIntent=new Intent(getApplicationContext(),MapsActivity.class);
                mapActivityIntent.putExtra("pickupLocation",pickupLocation);
                mapActivityIntent.putExtra("pickupLatitude", pickupLat);
                mapActivityIntent.putExtra("pickupLongitude", pickupLong);
                mapActivityIntent.putExtra("dropOffLocation", dropOffLocation);
                mapActivityIntent.putExtra("dropOffLatitude", dropOffLat);
                mapActivityIntent.putExtra("dropOffLongitude", dropOffLong);
                startActivity(mapActivityIntent);
            }
        });
    }
}
