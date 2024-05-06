package be.kuleuven.gt.dogapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import be.kuleuven.gt.dogapp.model.User;

public class ThreeBarsActivity extends AppCompatActivity {
    private User user;
    private ArrayList<String> dogNames;
    private ArrayList<String> dogIDs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_three_bars);
        dogNames = new ArrayList<>();
        dogIDs = new ArrayList<>();
        user = (User) getIntent().getParcelableExtra("user");
        loadDogData(user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ConstraintLayout btnMain = findViewById(R.id.messageSelect);
        ImageView btnMyDog = findViewById(R.id.imgMyDog);
        ImageView btnThreeBars = findViewById(R.id.btnThreeBars);
        ImageView btnCalendar = findViewById(R.id.btnCalendar);
        ImageView btnCalculator = findViewById(R.id.btnCalculator);
        ImageView btnBreeding = findViewById(R.id.btnBreeding);
        ImageView btnMap= findViewById(R.id.btnMap);
        ImageView btnTrainingVideos = findViewById(R.id.btnVideos);
        ImageView btnFirstAid = findViewById(R.id.imgFirstAid);
        ImageView btnSettings = findViewById(R.id.imgSettings);
        ImageView btnBreedInfo = findViewById(R.id.imgInfo);
        ImageView btnNotification = findViewById(R.id.btnNotifications);
        TextView txtInbox = findViewById(R.id.txtInbox);
        TextView txtFirstAid = findViewById(R.id.txtFirstAid);
        TextView txtSettings = findViewById(R.id.txtSettings);
        TextView txtBreedInfo= findViewById(R.id.txtBreedInformation);
        Button btnLogOut = findViewById(R.id.btnLogOut);

        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openInbox();
            }
        });

        txtInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openInbox();
            }
        });

        btnFirstAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openFirstAid();
            }
        });

        txtFirstAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openFirstAid();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openSettings();
            }
        });

        txtSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openSettings();
            }
        });

        btnBreedInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openBreedInfo();
            }
        });

        txtBreedInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openBreedInfo();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                logOut();
            }
        });

        btnThreeBars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openThreeBarsFunction();
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openCalendar();
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openMap();
            }
        });

        btnBreeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openBreeding();
            }
        });

        btnCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openFoodCalculator();
            }
        });

        btnTrainingVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openTrainingVideos();
            }
        });

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openThreeBarsFunction();
            }
        });

        btnMyDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openThreeBarsFunction();
            }
        });

    }

    private void openInbox() {
        // Implement your functionality here
        Intent intent = new Intent(this, MessageInbox.class);
        intent.putExtra("user", user);
        startActivity(intent);

    }

    private void logOut() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().clear().apply();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openFirstAid() {
        // Implement your functionality here
        Intent intent = new Intent(this, FirstAidActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void openSettings() {
        // Implement your functionality here
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void openBreedInfo() {
        // Implement your functionality here
        Intent intent = new Intent(this, BreedInformationActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }


    private void openThreeBarsFunction() {
        // Implement your functionality here
        Intent intent = new Intent(this, MyDogsActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void openCalendar() {
        // Implement your functionality here
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void openMap() {
        // Implement your functionality here
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void openBreeding() {
        // Implement your functionality here
        Intent intent = new Intent(this, BreedingActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void openTrainingVideos() {
        // Implement your functionality here
        Intent intent = new Intent(this, TrainingVideosActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void openFoodCalculator() {
        // Implement your functionality here
        Intent intent = new Intent(this, FoodCalculatorActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void loadDogData(User user) {
        dogNames.clear(); // Clear previous data
        dogIDs.clear(); // Clear previous data
        getDogs(user);
    }

    private void getDogs(User u)
    {
        String baseUrl = "https://studev.groept.be/api/a23PT106/user_dogs";
        String urlCreate = baseUrl + "/" + u.getIdUser();



        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlCreate,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        boolean match = false;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject o = response.getJSONObject(i);
                                String id = o.getString("idPet");
                                String dogName = o.getString("name");
                                dogIDs.add(id);
                                dogNames.add(dogName);



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

                        Toast.makeText(ThreeBarsActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(queueRequest);
    }

    private void updateSpinner() {
        // Get the spinner from the layout
        Spinner spinner = findViewById(R.id.spMyDogs);

        // Create an ArrayAdapter using the dog names ArrayList
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dogNames);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

}