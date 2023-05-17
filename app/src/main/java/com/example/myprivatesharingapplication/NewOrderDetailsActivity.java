package com.example.myprivatesharingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class NewOrderDetailsActivity extends AppCompatActivity {

    //Initializing variables
    EditText otherGoodTypeEdit, otherVehicleEdit, weightEdit, widthEdit, lengthEdit, heightEdit, quantityEdit;
    Button createOrderButton;
    RadioGroup goodTypeRadioGroup, vehicleTypeRadioGroup;
    RadioButton goodTypeRadioButton, vehicleTypeRadioButton;
    String username, receiverName, pickupTime, dropOffTime, pickupLocation, dropOffLocation;
    double pickupLat, pickupLong, dropOffLat, dropOffLong;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_details);

        //Find view by ID
        otherGoodTypeEdit = findViewById(R.id.otherGoodsEdit);
        otherVehicleEdit = findViewById(R.id.otherVehicleEdit);
        weightEdit = findViewById(R.id.weightEditText);
        widthEdit = findViewById(R.id.widthEditText);
        lengthEdit = findViewById(R.id.lengthEditText);
        heightEdit = findViewById(R.id.heightEditText);
        quantityEdit = findViewById(R.id.quantityEditText);
        goodTypeRadioGroup = findViewById(R.id.goodTypeRadioGroup);
        vehicleTypeRadioGroup = findViewById(R.id.vehicleTypeRadioGroup);
        createOrderButton = findViewById(R.id.createOrderButton);
        db = new DatabaseHelper(this);

        //Get data from previous intent
        username = getIntent().getStringExtra("username");
        receiverName = getIntent().getStringExtra("receiverName");
        pickupTime = getIntent().getStringExtra("pickupTime");
        dropOffTime = getIntent().getStringExtra("dropOffTime");
        pickupLocation = getIntent().getStringExtra("pickupLocation");
        dropOffLocation = getIntent().getStringExtra("dropOffLocation");

        pickupLat = getIntent().getDoubleExtra("pickupLatitude",0);
        pickupLong = getIntent().getDoubleExtra("pickupLongitude",0);
        dropOffLat = getIntent().getDoubleExtra("dropOffLatitude",0);
        dropOffLong = getIntent().getDoubleExtra("dropOffLongitude",0);

        //If create order button clicked
        createOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int goodTypeSelectedID, vehicleTypeSelectedID;
                goodTypeSelectedID = goodTypeRadioGroup.getCheckedRadioButtonId();
                vehicleTypeSelectedID = vehicleTypeRadioGroup.getCheckedRadioButtonId();

                //If good type not selected
                if (goodTypeSelectedID == -1) {
                    Toast.makeText(getApplicationContext(), "Good type not selected.", Toast.LENGTH_SHORT).show();

                    //If vehicle type not selected
                } else if (vehicleTypeSelectedID == -1) {
                    Toast.makeText(getApplicationContext(), "Vehicle type not selected.", Toast.LENGTH_SHORT).show();

                    //If any of the dimension fields are empty
                } else if (weightEdit.getText().toString().equals("") || widthEdit.getText().toString().equals("") ||
                    lengthEdit.getText().toString().equals("") || heightEdit.getText().toString().equals("") ) {
                    Toast.makeText(getApplicationContext(), "Please enter all required values.", Toast.LENGTH_SHORT).show();

                } else {
                    goodTypeRadioButton = findViewById(goodTypeSelectedID);
                    vehicleTypeRadioButton = findViewById(vehicleTypeSelectedID);

                    String weight = weightEdit.getText().toString();
                    String width = widthEdit.getText().toString();
                    String length = lengthEdit.getText().toString();
                    String height = heightEdit.getText().toString();
                    String quantity = quantityEdit.getText().toString();

                    //If other good type is entered, else selected from radio group
                    String goodType;
                    if (goodTypeSelectedID == R.id.otherGoodsButton) {
                        goodType = otherGoodTypeEdit.getText().toString();
                    } else {
                        goodType = goodTypeRadioButton.getText().toString();
                    }

                    //If other vehicle type is entered, else selected from radio group
                    String vehicleType;
                    if (vehicleTypeSelectedID == R.id.otherVehicleButton) {
                        vehicleType = otherVehicleEdit.getText().toString();
                    } else {
                        vehicleType = vehicleTypeRadioButton.getText().toString();
                    }

                    //Create new delivery instance and initialize it with the variables
                    Order order = new Order(username, receiverName,pickupTime, dropOffTime,weight,height,width,length,quantity,goodType,pickupLocation, pickupLat, pickupLong, dropOffLocation, dropOffLat, dropOffLong);
                    db.insertOrder(order);
                    Log.d("pickupLatitude", String.valueOf(pickupLat));
                    Log.d("pickupLongitude", String.valueOf(pickupLong));
                    Log.d("dropoffLocation", String.valueOf(dropOffLocation));
                    Log.d("dropoffLatitude", String.valueOf(dropOffLat));
                    Toast.makeText(NewOrderDetailsActivity.this, "Order created successfully!", Toast.LENGTH_SHORT).show();

                    Intent newOrderDetailsIntent = new Intent(NewOrderDetailsActivity.this, HomeActivity.class);
                    newOrderDetailsIntent.putExtra("username",username);
                    startActivity(newOrderDetailsIntent);
                    finish();
                }
            }
        });

    }
}
