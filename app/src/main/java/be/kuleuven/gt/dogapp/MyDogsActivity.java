package be.kuleuven.gt.dogapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.kuleuven.gt.dogapp.R;
import be.kuleuven.gt.dogapp.model.User;

public class MyDogsActivity extends AppCompatActivity {
    private User user;
    private ArrayList<String> dogNames;
    private ArrayList<String> dogIDs;

    private ArrayList<String> dogsAge = new ArrayList<>();
    private ArrayList<String> dogsBreed = new ArrayList<>();
    private ArrayList<Boolean> dogsBreedingState;
    private int positionOfSpinner;
    private Switch schBreedable;
    private TextView txtAge;
    private TextView txtBreed;

    @Override //as
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_dogs);
        dogNames = new ArrayList<>();
        dogIDs = new ArrayList<>();
        dogsBreedingState = new ArrayList<>();
        user = (User) getIntent().getParcelableExtra("user");
        loadDogData(user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtAge = findViewById(R.id.txtAge);
        txtBreed = findViewById(R.id.txtBreed);
        ImageView btnThreeBars = findViewById(R.id.btnThreeBars);
        ImageView btnCalendar = findViewById(R.id.btnCalendar);
        ImageView btnCalculator = findViewById(R.id.btnCalculator);
        ImageView btnBreeding = findViewById(R.id.btnBreeding);
        ImageView btnMap= findViewById(R.id.btnMap);
        ImageView btnTrainingVideos = findViewById(R.id.btnVideos);
        schBreedable = findViewById(R.id.schBreedable);

        schBreedable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String currentDogID = dogIDs.get(positionOfSpinner).toString();
                if (isChecked) {
                    // Switch is ON
                    // Do something when the switch is on
                    System.out.println("Turned On");
                    String baseUrl = "https://studev.groept.be/api/a23PT106/breedable";
                    String urlCreate = baseUrl + "/" + "1" + "/" + currentDogID + "/";


                    ProgressDialog progressDialog = new ProgressDialog(MyDogsActivity.this);
                    progressDialog.setMessage("Uploading, please wait...");
                    progressDialog.show();
                    RequestQueue requestQueue = Volley.newRequestQueue(MyDogsActivity.this);
                    StringRequest submitRequest = new StringRequest(
                            Request.Method.POST,
                            urlCreate,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    dogsBreedingState.set(positionOfSpinner, true);
//                                    Toast.makeText(
//                                            MyDogsActivity.this,
//                                            "Set to Breedable!",
//                                            Toast.LENGTH_SHORT).show();

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(
                                            MyDogsActivity.this,
                                            "Something went wrong [Breedable]..." + error,
                                            Toast.LENGTH_LONG).show();

                                }
                            }
                    ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
                        @Override
                        protected Map<String, String> getParams() {
                            /* Map<String, String> with key value pairs as data load */
                            Map<String, String> params = new HashMap<>();
                            params.put("breedable", "1");
                            params.put("idpet", currentDogID);


                            return params;
                        }
                    };
                    requestQueue.add(submitRequest);

                } else {
                    // Switch is OFF
                    // Do something when the switch is off
                    System.out.println("Turned Off");
                    String baseUrl = "https://studev.groept.be/api/a23PT106/breedable";
                    String urlCreate = baseUrl + "/" + "0" + "/" + currentDogID + "/";


                    ProgressDialog progressDialog = new ProgressDialog(MyDogsActivity.this);
                    progressDialog.setMessage("Uploading, please wait...");
                    progressDialog.show();
                    RequestQueue requestQueue = Volley.newRequestQueue(MyDogsActivity.this);
                    StringRequest submitRequest = new StringRequest(
                            Request.Method.POST,
                            urlCreate,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    dogsBreedingState.set(positionOfSpinner, false);
//                                    Toast.makeText(
//                                            MyDogsActivity.this,
//                                            "Set to Not Breedable!",
//                                            Toast.LENGTH_SHORT).show();

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(
                                            MyDogsActivity.this,
                                            "Something went wrong [Breedable]..." + error,
                                            Toast.LENGTH_LONG).show();

                                }
                            }
                    ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
                        @Override
                        protected Map<String, String> getParams() {
                            /* Map<String, String> with key value pairs as data load */
                            Map<String, String> params = new HashMap<>();
                            params.put("breedable", "0");
                            params.put("idpet", currentDogID);


                            return params;
                        }
                    };
                    requestQueue.add(submitRequest);
                }
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
        Spinner spinner = findViewById(R.id.spMyDogs);


        // Create an ArrayAdapter using the dog names ArrayList
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dogNames);

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
                System.out.println(dogIDs.get(positionOfSpinner).toString());
                schBreedable.setChecked(dogsBreedingState.get(positionOfSpinner));
                int txtToChangeAge = 0;
                String txtToChangeBreed = "";
                if (!dogsAge.get(positionOfSpinner).equals(" ") && !dogsAge.get(positionOfSpinner).equals("_") && !(dogsAge.get(positionOfSpinner) == null)) {
                    try {
                        Integer.parseInt(dogsAge.get(positionOfSpinner));
                        txtAge.setText("Age: " + dogsAge.get(positionOfSpinner));
                    }
                    catch (NumberFormatException e){
                        System.out.println("Not a number!");
                        txtAge.setText("Age is not specified.");
                    }
                }
                else {
                    txtAge.setText("Age is not specified.");
                }
                if (!dogsBreed.get(positionOfSpinner).equals(" ") && !dogsBreed.get(positionOfSpinner).equals("_") && !(dogsBreed.get(positionOfSpinner) == null)) {
                    txtBreed.setText("Breed: " + dogsBreed.get(positionOfSpinner));
                }
                else {
                    txtBreed.setText("Breed is not specified.");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected (optional)
            }
        });
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
                                String dogBreedingState = o.getString("breedable");
                                String dogAge = o.getString("age");
                                String dogBreed = o.getString("breed");
                                dogIDs.add(id);
                                dogNames.add(dogName);
                                dogsAge.add(dogAge);
                                dogsBreed.add(dogBreed);

                                if (dogBreedingState.equals("0")) { dogsBreedingState.add(false); }
                                else { dogsBreedingState.add(true); }



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