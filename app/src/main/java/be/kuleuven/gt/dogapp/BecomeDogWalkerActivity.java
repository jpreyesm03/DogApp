package be.kuleuven.gt.dogapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

import be.kuleuven.gt.dogapp.model.User;

public class BecomeDogWalkerActivity extends AppCompatActivity {

    private Switch available;

    private User user;

    private Double currentLon;
    private Double currentLat;

    private boolean states = false;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private String accessToken = "sk.eyJ1IjoicmFub3NraW5nMTQiLCJhIjoiY2x1M3N2NHgyMWJwdDJrazFpcHdtMThhcyJ9.rqqc90kdSY2EWsJpgyeCUA";
    private EditText hourlyWage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_become_dog_walker);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        hourlyWage = findViewById(R.id.hourly_wage);
        Button btnBack = findViewById(R.id.btnBack);

        user = (User) getIntent().getParcelableExtra("user");

        addToDogWalkersTable();
        available = findViewById(R.id.available);


        available.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String baseUrl = "https://studev.groept.be/api/a23PT106/availableDogWalker";
                String urlCreate = baseUrl + "/" + (isChecked ? "1" : "0") + "/" + user.getIdUser();

                ProgressDialog progressDialog = new ProgressDialog(BecomeDogWalkerActivity.this);
                progressDialog.setMessage("Uploading, please wait...");
                progressDialog.show();
                RequestQueue requestQueue = Volley.newRequestQueue(BecomeDogWalkerActivity.this);
                StringRequest submitRequest = new StringRequest(
                        Request.Method.POST,
                        urlCreate,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                // Update the states variable
                                states = isChecked;
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                // Show error message
                                Toast.makeText(
                                        BecomeDogWalkerActivity.this,
                                        "Something went wrong [Become Dog Walker]..." + error,
                                        Toast.LENGTH_LONG
                                ).show();
                                // Reset the switch state if there is an error
                                available.setChecked(!isChecked);
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("available", isChecked ? "1" : "0");
                        params.put("iduser", user.getIdUser());
                        return params;
                    }
                };
                requestQueue.add(submitRequest);
            }
        });

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
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

    }

    public void onBtnSubmit_Clicked(View Caller) {
        getCurrentLocation();
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


    private void getCurrentLocation() {
        String wage = hourlyWage.getText().toString();
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ) {
            if(!wage.isEmpty())
            {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations, this can be null.
                                        if (location != null) {

                                            currentLon = location.getLongitude();
                                            currentLat = location.getLatitude();
                                            updateDB(currentLon, currentLat,wage);
                                        } else {
                                            Toast.makeText(BecomeDogWalkerActivity.this, "Could not retrieve location", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                        );
            }
            else {
                Toast.makeText(BecomeDogWalkerActivity.this, "Please declare your wage preferences!", Toast.LENGTH_SHORT).show();
            }


            // Permission already granted, get current location

        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void updateDB(Double lon, Double lat,String wag) {
        String baseUrl = "https://studev.groept.be/api/a23PT106/setDogWalkersLocations";
        String urlCreate = baseUrl + "/" + lon + "/" + lat +  "/" + wag + "/" + user.getUsername() +  "/" + user.getEmail() +  "/" + user.getIdUser() ;


        ProgressDialog progressDialog = new ProgressDialog(BecomeDogWalkerActivity.this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(
                Request.Method.POST,
                urlCreate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                BecomeDogWalkerActivity.this,
                                "Unable to update Location DB: " + error,
                                Toast.LENGTH_LONG).show();

                    }
                }
        ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
            @Override
            protected Map<String, String> getParams() {
                /* Map<String, String> with key value pairs as data load */
                Map<String, String> params = new HashMap<>();
                params.put("longitude", String.valueOf(lon));
                params.put("latitude", String.valueOf(lat));
                params.put("wage", wag);
                params.put("name", user.getUsername());
                params.put("email", user.getEmail());
                params.put("iduser", user.getIdUser());
                return params;
            }
        };
        requestQueue.add(submitRequest);
    }

    private void addToDogWalkersTable() {
        String baseUrl = "https://studev.groept.be/api/a23PT106/addDogWalkers";
        String urlCreate = baseUrl + "/" + user.getIdUser();


        ProgressDialog progressDialog = new ProgressDialog(BecomeDogWalkerActivity.this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(
                Request.Method.POST,
                urlCreate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                BecomeDogWalkerActivity.this,
                                "Unable to add dog: " + error,
                                Toast.LENGTH_LONG).show();

                    }
                }
        ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
            @Override
            protected Map<String, String> getParams() {
                /* Map<String, String> with key value pairs as data load */
                Map<String, String> params = new HashMap<>();
                params.put("userid", user.getIdUser());

                return params;
            }
        };
        requestQueue.add(submitRequest);
    }

    protected void onPause() {
        super.onPause();
        // Store switch state with user ID as part of the key
        SharedPreferences preferences = getSharedPreferences("SwitchState_" + user.getIdUser(), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("switchState", available.isChecked());
        editor.apply();
    }

    // Restore switch state in onResume
    @Override
    protected void onResume() {
        super.onResume();
        // Retrieve switch state using user ID as part of the key
        SharedPreferences preferences = getSharedPreferences("SwitchState_" + user.getIdUser(), MODE_PRIVATE);
        boolean switchState = preferences.getBoolean("switchState", false);
        available.setChecked(switchState);
        states = switchState;
    }
}