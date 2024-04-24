package be.kuleuven.gt.dogapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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

import be.kuleuven.gt.dogapp.R;
import be.kuleuven.gt.dogapp.model.User;

public class MyDogsActivity extends AppCompatActivity {
    private User user;
    private ArrayList<String> dogNames;
    private ArrayList<String> dogIDs;

    @Override //as
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_dogs);
        dogNames = new ArrayList<>();
        dogIDs = new ArrayList<>();
        user = (User) getIntent().getParcelableExtra("user");
        loadDogData(user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView btnThreeBars = findViewById(R.id.btnThreeBars);
        ImageView btnCalendar = findViewById(R.id.btnCalendar);
        ImageView btnCalculator = findViewById(R.id.btnCalculator);
        ImageView btnBreeding = findViewById(R.id.btnBreeding);
        ImageView btnMap= findViewById(R.id.btnMap);
        ImageView btnTrainingVideos = findViewById(R.id.btnVideos);

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

    }

    private void openThreeBarsFunction() {
        // Implement your functionality here
        Intent intent = new Intent(this, ThreeBarsActivity.class);
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

    protected void onResume() {
        super.onResume();
        user = (User) getIntent().getParcelableExtra("user");
        loadDogData(user);
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

                        Toast.makeText(MyDogsActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(queueRequest);
    }
    private void getDogInfo()
    {}


}