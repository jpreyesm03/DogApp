package be.kuleuven.gt.dogapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import be.kuleuven.gt.dogapp.model.User;

public class HireDogWalkersActivity extends AppCompatActivity {

    private User user;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private Double currentLat;
    private Double currentLon;
    private ArrayList<String> dogWalkerNames;
    private ArrayList<Double> distances;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hire_dog_walkers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        user = (User) getIntent().getParcelableExtra("user");
        dogWalkerNames = new ArrayList<>();
        distances = new ArrayList<>();

        Button btnBack = findViewById(R.id.btnBack);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openPrevious();
            }
        });
    }

    private void openPrevious() {
        // Implement your functionality here
        Intent intent = new Intent(this, MyDogsActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

    }

    private void getCurrentLocation() {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, get current location
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations, this can be null.
                            if (location != null) {

                                 currentLat = location.getLatitude();
                                 currentLon = location.getLongitude();




                                    currentLon = location.getLongitude();
                                    currentLat = location.getLatitude();







                            } else {
                                Toast.makeText(HireDogWalkersActivity.this, "Could not retrieve location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private Double calcDistance(Double lonStart, Double latStart,Double lonEnd,Double latEnd) {
        Double dist = 0.0;
        if (lonStart != null && latStart != null && lonEnd != null && latEnd != null) {
            final double R = 6371.0; // Radius of the Earth in kilometers

            // Convert latitude and longitude from degrees to radians
            double lonS = Math.toRadians(lonStart);
            double latS = Math.toRadians(latStart);
            double lonE = Math.toRadians(lonEnd);
            double latE = Math.toRadians(latEnd);

            // Difference in latitudes and longitudes
            double dlon = lonE - lonS;
            double dlat = latE - latS;

            // Haversine formula
            double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(latS) * Math.cos(latE) * Math.pow(Math.sin(dlon / 2), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            dist = R * c;




        } else {
            Toast.makeText(HireDogWalkersActivity.this, "No current location!", Toast.LENGTH_SHORT).show();
        }
        return dist;
    }



}