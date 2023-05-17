package com.example.myprivatesharingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends AppCompatActivity implements OrderAdapter.OnRowClickListener {

    //Initializing variables
    DatabaseHelper db;
    List<Order> myOrdersList = new ArrayList<>();
    ImageButton addOrderButton;
    RecyclerView myOrdersRecyclerView;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        //Find view by ID
        username = getIntent().getStringExtra("username");
        addOrderButton = findViewById(R.id.addOrderButton);
        myOrdersRecyclerView = findViewById(R.id.orderRecyclerView);

        //Fetch all orders for this user
        db = new DatabaseHelper(this);
        myOrdersList = db.fetchAllOrders(username);

        OrderAdapter orderAdapter = new OrderAdapter(MyOrderActivity.this, myOrdersList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        myOrdersRecyclerView.setLayoutManager(layoutManager);
        myOrdersRecyclerView.setAdapter(orderAdapter);

        //If add order button is clicked
        addOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addOrderIntent = new Intent(getApplicationContext(), NewOrderActivity.class);
                addOrderIntent.putExtra("username", username);
                startActivity(addOrderIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //If an order in the order list is clicked
    @Override
    public void onItemClick(int position) {
        String receivername = myOrdersList.get(position).getReceiverName();
        String dropofftime = myOrdersList.get(position).getDropoffTime();
        String pickuptime = myOrdersList.get(position).getPickupTime();
        String weight = myOrdersList.get(position).getWeight();
        String height = myOrdersList.get(position).getHeight();
        String width = myOrdersList.get(position).getWidth();
        String length = myOrdersList.get(position).getLength();
        String quantity = myOrdersList.get(position).getQuantity();
        String type = myOrdersList.get(position).getType();
        String pickupLocation = myOrdersList.get(position).getPickupLocation();
        double pickupLatitude = myOrdersList.get(position).getPickupLatitude();
        double pickupLongitude = myOrdersList.get(position).getPickupLongitude();
        String dropoffLocation = myOrdersList.get(position).getDropoffLocation();
        double dropoffLatitude = myOrdersList.get(position).getDropoffLatitude();
        double dropoffLongitude = myOrdersList.get(position).getDropoffLongitude();
        int image = myOrdersList.get(position).getImage();

        Intent orderDetailsIntent = new Intent(getApplicationContext(), OrderDetailsActivity.class);
        orderDetailsIntent.putExtra("username",username);
        orderDetailsIntent.putExtra("receivername",receivername);
        orderDetailsIntent.putExtra("dropofftime",dropofftime);
        orderDetailsIntent.putExtra("pickuptime",pickuptime);
        orderDetailsIntent.putExtra("weight",weight);
        orderDetailsIntent.putExtra("height",height);
        orderDetailsIntent.putExtra("width",width);
        orderDetailsIntent.putExtra("length",length);
        orderDetailsIntent.putExtra("quantity",quantity);
        orderDetailsIntent.putExtra("type",type);
        orderDetailsIntent.putExtra("image",image);
        orderDetailsIntent.putExtra("pickupLocation",pickupLocation);
        orderDetailsIntent.putExtra("pickupLatitude",pickupLatitude);
        orderDetailsIntent.putExtra("pickupLongitude",pickupLongitude);
        orderDetailsIntent.putExtra("dropoffLocation",dropoffLocation);
        orderDetailsIntent.putExtra("dropoffLatitude",dropoffLatitude);
        orderDetailsIntent.putExtra("dropoffLongitude",dropoffLongitude);
        startActivity(orderDetailsIntent);
    }

    //If an option is selected in the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeMenu:
                Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                homeIntent.putExtra("username",username);
                startActivity(homeIntent);
                finish();
                return true;
            case R.id.myOrdersMenu:
                Intent myOrdersIntent = new Intent(getApplicationContext(), MyOrderActivity.class);
                myOrdersIntent.putExtra("username",username);
                startActivity(myOrdersIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onShareButtonClick(View v, int position) {
        Intent sharedIntent = new Intent(Intent.ACTION_SEND);
        sharedIntent.setType("text/plain");
        String body = "Share Order";
        sharedIntent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(sharedIntent, "Share Order"));
    }
}
