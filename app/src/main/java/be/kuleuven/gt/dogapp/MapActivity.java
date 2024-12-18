package be.kuleuven.gt.dogapp;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Timer;
import java.util.TimerTask;

import be.kuleuven.gt.dogapp.model.User;

public class MapActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private String accessToken = "sk.eyJ1IjoicmFub3NraW5nMTQiLCJhIjoiY2x1M3N2NHgyMWJwdDJrazFpcHdtMThhcyJ9.rqqc90kdSY2EWsJpgyeCUA";
    private ImageView mapImageView;
    private User user;

    private ImageView pointer;

    private boolean startOfWalk = false;

    private Double latEnd;
    private Double lonEnd;
    private Double lonStart;
    private Double latStart;

    private Double currentLon;
    private Double currentLat;
    private Double dist;
    private boolean first;

    private TextView disText;

    private TextView walk;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private static Handler handler;
    private Runnable distanceCalculatorRunnable;
    private final int UPDATE_INTERVAL = 10000; // Update interval in milliseconds
    private boolean walkInProgress;
    private boolean isDistanceCalculatorActive = false;
    private boolean activityActive = true; // Flag to track activity state
    private boolean activityVisible = false;
    private boolean isHandlerRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        dist =  0.0;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapImageView = findViewById(R.id.mapImageView);
        Button btnBack = findViewById(R.id.btnBack);
        user = (User) getIntent().getParcelableExtra("user");
        pointer = findViewById(R.id.pointer);

        pointer.setVisibility(View.INVISIBLE);
        first = true;

        disText = findViewById(R.id.distance);

        walk = findViewById(R.id.walkTime);
        //walkInProgress = false;

        getCurrentLocation();


        if(handler == null)
        {
            handler = new Handler();

        }

        distanceCalculatorRunnable = new Runnable() {
            @Override
            public void run() {
                if (activityActive && walkInProgress) {
                    getCurrentLocation();
                    Toast.makeText(MapActivity.this, "looper executed", Toast.LENGTH_SHORT).show();
                    // Schedule the next update after UPDATE_INTERVAL milliseconds
                    handler.postDelayed(this, UPDATE_INTERVAL);
                }
            }
        };








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
        handler.removeCallbacks(distanceCalculatorRunnable);
        Toast.makeText(MapActivity.this, "Walk cancelled! Please stay in this screen!", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onBackPressed() {
        // Do nothing to disable the back button
        super.onBackPressed();
        handler.removeCallbacks(distanceCalculatorRunnable);
        Toast.makeText(MapActivity.this, "Walk cancelled! Please stay in this screen!", Toast.LENGTH_SHORT).show();
    }



    public void onBtnStartWalk_Clicked(View Caller) {
        if (!walkInProgress) {
            startWalk();
        }

    }
    public void onBtnEndWalk_Clicked(View Caller) {
        if (walkInProgress) {
            endWalk();
        }
    }


    private  void startWalk() {
        if (!isHandlerRunning && !isDistanceCalculatorActive && activityVisible) {
            walkInProgress = true;
            isDistanceCalculatorActive = true;
            // Schedule periodic distance calculation using Handler
            handler.postDelayed(distanceCalculatorRunnable, 0); // Start immediately
            isHandlerRunning = true; // Set the flag to true
        }
    }

    private void endWalk() {
        walkInProgress = false;
        isDistanceCalculatorActive = false;
        getCurrentLocation();
        // Stop periodic distance calculation
        handler.removeCallbacks(distanceCalculatorRunnable);
        isHandlerRunning = false; // Reset the flag to false
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateMap(currentLat,currentLon);
        if(walkInProgress)
        {
            isHandlerRunning = true;
        }
        activityVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityVisible = false;
        if(walkInProgress)
        {
            isHandlerRunning = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any callbacks from the handler
        handler.removeCallbacksAndMessages(null);
        if(walkInProgress)
        {
            isHandlerRunning = true;
        }
    }

    public void onBtnResetWalk_Clicked(View Caller) {
        resetWalk();
    }

    private void resetWalk() {
        dist = 0.0;
        String formattedDist = String.format("%.1f", dist); // Format distance to two decimal places
        disText.setText("Distance Walked: " + formattedDist + " km");
    }



    // Method to stop the periodic distance calculation





    private void calcDistance() {
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
            dist += R * c;

            String formattedDist = String.format("%.1f", dist); // Format distance to two decimal places
            disText.setText("Distance Walked: " + formattedDist + " km");



        } else {
            Toast.makeText(MapActivity.this, "No current location!", Toast.LENGTH_SHORT).show();
        }
    }





    public void onBtnSeeDogWalkers_Clicked(View Caller) {

        openHireDogWalker();


    }

    private void openHireDogWalker() {
        Intent intent = new Intent(this,HireDogWalkersActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    private void getCurrentLocation() {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, get current location
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations, this can be null.
                            if (location != null) {
                                // Location object is not null, proceed with processing location
                                handleLocation(location);
                            } else {
                                // Location object is null, show a toast or handle accordingly
                                Toast.makeText(MapActivity.this, "Location is null", Toast.LENGTH_SHORT).show();
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

    private void handleLocation(Location location) {
        if(first)
        {   currentLat = location.getLatitude();
            currentLon = location.getLongitude();
            first = false;
        }

        else {

            currentLon = location.getLongitude();
            currentLat = location.getLatitude();


            if (!startOfWalk) {
                startOfWalk = true;
                lonStart = currentLon;
                latStart = currentLat;
                Toast.makeText(
                        MapActivity.this,
                        "Walk Started!",
                        Toast.LENGTH_LONG).show();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startTime = LocalDateTime.now();
                }
            }
            else {
                if (walkInProgress)
                {
                    lonEnd = currentLon;
                    latEnd = currentLat;
                }
                else {
                    lonEnd = currentLon;
                    latEnd = currentLat;

                    Toast.makeText(
                            MapActivity.this,
                            "Walk Finished!",
                            Toast.LENGTH_LONG).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        endTime = LocalDateTime.now();
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        Duration duration = Duration.between(startTime, endTime);
                        walk.setText("Time of walk: " + duration.toMinutes()+ " minutes");
                    }

                }
                calcDistance();


            }
            updateMap(currentLat, currentLon);
            updateDB(currentLat, currentLon);
            pointer.setVisibility(View.VISIBLE);

        }
    }

    private void updateDB(Double latitude, Double longitude)
    {
        String baseUrl = "https://studev.groept.be/api/a23PT106/updateMapDB";
        String urlCreate = baseUrl + "/" + longitude + "/" + latitude + "/" + user.getIdUser();


        ProgressDialog progressDialog = new ProgressDialog(MapActivity.this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(
                Request.Method.POST,
                urlCreate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        currentLat = latitude;
                        currentLon = longitude;
                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                MapActivity.this,
                                "Unable to update Location DB: " + error,
                                Toast.LENGTH_LONG).show();

                    }
                }
        ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
            @Override
            protected Map<String, String> getParams() {
                /* Map<String, String> with key value pairs as data load */
                Map<String, String> params = new HashMap<>();
                params.put("lon", String.valueOf(longitude));
                params.put("lat", String.valueOf(latitude));
                params.put("iduser", user.getIdUser());
                               return params;
            }
        };
        requestQueue.add(submitRequest);
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get current location
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateMap(double lat, double lon)
    {
        String url = "https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/" +
                lon + "," + lat + ",14/800x600?access_token=" + accessToken;

        RequestQueue queue = Volley.newRequestQueue(this);

        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Update ImageView with the downloaded image
                        mapImageView.setImageBitmap(response);
                    }
                },
                0, // Width
                0, // Height
                ImageView.ScaleType.CENTER_CROP, // ScaleType
                Bitmap.Config.RGB_565, // Config
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e("ImageRequest", "Error loading image");
                    }
                }
        );

        queue.add(imageRequest);


    }

}