package com.example.myprivatesharingapplication;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.myprivatesharingapplication.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.google.maps.android.PolyUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Initializing variables
    private GoogleMap googleMap;
    DirectionsResult directionsResult;
    String pickupLocation, dropOffLocation, distance, travelTime;
    double pickupLat, pickupLong, dropOffLat, dropOffLong, fare;
    TextView pickUpLocationText, dropOffLocationText, fareText, travelTimeText;
    Button bookButton, callButton;
    private ActivityMapsBinding binding;
    private static final int REQUEST_PHONE_CALL = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("(client-id-here)");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Find view by ID
        pickUpLocationText = findViewById(R.id.pickUpLocationText);
        dropOffLocationText = findViewById(R.id.dropOffLocation);
        fareText = findViewById(R.id.fareText);
        travelTimeText = findViewById(R.id.travelTimeText);
        bookButton = findViewById(R.id.bookButton);
        callButton = findViewById(R.id.callButton);

        //Get data from previous intent
        pickupLocation = getIntent().getStringExtra("pickupLocation");
        pickupLat = getIntent().getDoubleExtra("pickupLatitude",0);
        pickupLong = getIntent().getDoubleExtra("pickupLongitude",0);
        dropOffLocation = getIntent().getStringExtra("dropOffLocation");
        dropOffLat = getIntent().getDoubleExtra("dropOffLatitude",0);
        dropOffLong = getIntent().getDoubleExtra("dropOffLongitude",0);
        pickUpLocationText.setText("Pickup Location: "+ pickupLocation);
        dropOffLocationText.setText("Drop-off Location: "+ dropOffLocation);

        //If book button is clicked
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(fare), "AUD", "Fee",
                        PayPalPayment.PAYMENT_INTENT_SALE);
                Intent paymentIntent = new Intent(getApplicationContext(), PaymentActivity.class);
                paymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                paymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                startActivity(paymentIntent);
            }
        });

        //If call button is clicked
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:0377778888"));

                if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                }
                else
                {
                    startActivity(callIntent);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng pickUpLng = new LatLng(pickupLat, pickupLong);
        LatLng dropOffLng = new LatLng(dropOffLat, dropOffLong);

        GeoApiContext geoApiContext = new GeoApiContext.Builder()
            .apiKey("(api-key-here)")
            .build();

        try {
            directionsResult = DirectionsApi.newRequest(geoApiContext).mode(TravelMode.DRIVING).origin(new com.google.maps.model.LatLng(pickUpLng.latitude, pickUpLng.longitude))
                    .destination(new com.google.maps.model.LatLng(dropOffLng.latitude, dropOffLng.longitude))
                    .transitMode()
                    .await();
            List<LatLng> decodedPath = PolyUtil.decode(directionsResult.routes[0].overviewPolyline.getEncodedPath());
            this.googleMap.addPolyline(new PolylineOptions().addAll(decodedPath));
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        distance =  directionsResult.routes[0].legs[0].distance.toString();
        travelTime =  directionsResult.routes[0].legs[0].duration.toString();
        String distanceText = distance.substring(0,distance.length()-3).trim();
        double distanceNum = Double.parseDouble(distanceText);
        fare = distanceNum * 5;
        String fareText = String.valueOf(fare);
        this.fareText.setText("Approx. Fare: $" + fareText);
        travelTimeText.setText("Approx. Travel time: " + travelTime);

        //Add markers to map
        this.googleMap.addMarker(new MarkerOptions().position(pickUpLng));
        this.googleMap.addMarker(new MarkerOptions().position(dropOffLng));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pickUpLng,15));
    }

}

