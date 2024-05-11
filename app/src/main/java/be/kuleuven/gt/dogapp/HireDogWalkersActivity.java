package be.kuleuven.gt.dogapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractCollection;
import java.util.ArrayList;

import be.kuleuven.gt.dogapp.model.User;

public class HireDogWalkersActivity extends AppCompatActivity {

    private User user;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private Double currentLat;
    private Double currentLon;
    private TextView wage;
    private TextView distanceFromWalker;
    private TextView emailWalker;



    private ArrayList<String> dogWalkersLongitude;
    private ArrayList<String> dogWalkersLatitude;
    private ArrayList<String> dogWalkersWage;
    private ArrayList<String> dogWalkersEmail;

    private ArrayList<String> dogWalkers;
    private int positionOfSpinner;


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
        dogWalkers= new ArrayList<>();
        dogWalkersLatitude= new ArrayList<>();
        dogWalkersLongitude= new ArrayList<>();
        dogWalkersEmail= new ArrayList<>();
        dogWalkersWage = new ArrayList<>();
        wage = findViewById(R.id.requiredWage);
        emailWalker = findViewById(R.id.dogWalkersEmail);
        distanceFromWalker = findViewById(R.id.distanceFromWalker);




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

    public void onBtnRefresh_Clicked(View Caller) {
        getWalkers();
    }

    public void onBtnSendMessage_Clicked(View Caller) {
        Intent intent = new Intent(this, SendMessages.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
    public void onBtnSeeAllMessages_Clicked(View Caller) {
        Intent intent = new Intent(this, MessageInbox.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }


    private void getWalkers() {
        String url = "https://studev.groept.be/api/a23PT106/showDogWalkers"; // Assuming this endpoint provides message sender emails


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        dogWalkers.clear(); // Clear previous data
                        dogWalkersWage.clear();
                        dogWalkersLatitude.clear();
                        dogWalkersLongitude.clear();
                        dogWalkersEmail.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                dogWalkers.add("Username: " + jsonObject.getString("name"));
                                dogWalkersWage.add(jsonObject.getString("hourly_wage"));
                                dogWalkersLongitude.add(jsonObject.getString("longitude"));
                                dogWalkersLatitude.add(jsonObject.getString("latitude"));
                                dogWalkersEmail.add(jsonObject.getString("email"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        updateSpinner();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HireDogWalkersActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void updateSpinner() {
        Spinner spinner = findViewById(R.id.dogWalkersNames);


        // Create an ArrayAdapter using the dog names ArrayList
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dogWalkers);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // `position` parameter contains the position of the selected item in the dropdown
                // Now you can use `position` as needed
                positionOfSpinner = position;

                wage.setText("Price: " + dogWalkersWage.get(positionOfSpinner) + "€/h");
                emailWalker.setText("Email: " + dogWalkersEmail.get(positionOfSpinner));
                getCurrentLocation(Double.valueOf(dogWalkersLatitude.get(positionOfSpinner)),Double.valueOf(dogWalkersLongitude.get(positionOfSpinner)));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected (optional)
            }
        });
    }

    private void getCurrentLocation(Double walkerLatitude, Double walkerLongitude) {
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
                                 calcDistance(walkerLatitude,walkerLongitude,currentLat,currentLon);


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

    private Double calcDistance(Double walkerLatitude, Double walkerLongitude,Double userLatitude, Double userLongitude) {
        Double dist = 0.0;
        if (walkerLongitude != null && walkerLatitude != null && userLongitude != null && userLatitude != null) {
            final double R = 6371.0; // Radius of the Earth in kilometers

            // Convert latitude and longitude from degrees to radians
            double lonS = Math.toRadians(walkerLongitude);
            double latS = Math.toRadians(walkerLatitude);
            double lonE = Math.toRadians(userLongitude);
            double latE = Math.toRadians(userLatitude);

            // Difference in latitudes and longitudes
            double dlon = lonE - lonS;
            double dlat = latE - latS;

            // Haversine formula
            double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(latS) * Math.cos(latE) * Math.pow(Math.sin(dlon / 2), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            dist = R * c;
            distanceFromWalker.setText("Distance from walker: " + dist + "km");






        } else {
            Toast.makeText(HireDogWalkersActivity.this, "No current location!", Toast.LENGTH_SHORT).show();
        }
        return dist;
    }



}