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

public class HomeActivity extends AppCompatActivity implements OrderAdapter.OnRowClickListener {

    //Initializing variables
    DatabaseHelper db;
    List<Order> allOrdersList = new ArrayList<>();
    RecyclerView allOrdersRecyclerView;
    ImageButton addNewOrderButton;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Find view by ID
        username = getIntent().getStringExtra("username");
        addNewOrderButton = findViewById(R.id.addOrderFromHomeButton);
        allOrdersRecyclerView = findViewById(R.id.allOrdersRecyclerView);

        //Fetch list of all orders
        db = new DatabaseHelper(this);
        allOrdersList = db.fetchAllOrdersforAll();

        OrderAdapter orderAdapter = new OrderAdapter(HomeActivity.this, allOrdersList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        allOrdersRecyclerView.setLayoutManager(layoutManager);
        allOrdersRecyclerView.setAdapter(orderAdapter);

        //If add new order button clicked
        addNewOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newOrderIntent = new Intent(getApplicationContext(), NewOrderActivity.class);
                newOrderIntent.putExtra("username", username);
                startActivity(newOrderIntent);
                finish();
            }
        });
    }

    //If an order in order list is clicked
    @Override
    public void onItemClick(int position) {
        String receivername = allOrdersList.get(position).getReceiverName();
        String dropofftime = allOrdersList.get(position).getDropoffTime();
        String pickuptime = allOrdersList.get(position).getPickupTime();
        String weight = allOrdersList.get(position).getWeight();
        String height = allOrdersList.get(position).getHeight();
        String width = allOrdersList.get(position).getWidth();
        String length = allOrdersList.get(position).getLength();
        String quantity = allOrdersList.get(position).getQuantity();
        String type = allOrdersList.get(position).getType();
        String pickupLocation = allOrdersList.get(position).getPickupLocation();
        double pickupLatitude = allOrdersList.get(position).getPickupLatitude();
        double pickupLongitude = allOrdersList.get(position).getPickupLongitude();
        String dropoffLocation = allOrdersList.get(position).getDropoffLocation();
        double dropoffLatitude = allOrdersList.get(position).getDropoffLatitude();
        double dropoffLongitude = allOrdersList.get(position).getDropoffLongitude();
        int image = allOrdersList.get(position).getImage();

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

    //If Share button is clicked
    @Override
    public void onShareButtonClick(View v, int position) {
        Intent sharedIntent = new Intent(Intent.ACTION_SEND);
        sharedIntent.setType("text/plain");
        String body = "Share Order";
        sharedIntent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(sharedIntent, "Share Order"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //If an option from the menu is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeMenu:
                Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
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
}
