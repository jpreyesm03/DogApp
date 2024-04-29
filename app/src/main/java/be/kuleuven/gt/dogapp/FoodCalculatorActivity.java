package be.kuleuven.gt.dogapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import be.kuleuven.gt.dogapp.model.User;

public class FoodCalculatorActivity extends AppCompatActivity {
    private User user;
    private double kg;
    private double price;
    private int days;
    private ArrayList<String> selectedDogs = new ArrayList<>();

    private ArrayList<String> dogNames = new ArrayList<>();
    private ArrayList<String> dogIDs = new ArrayList<>();

    private ArrayList<String> dogsWeight = new ArrayList<>();
    private ArrayList<String> dogsBreed = new ArrayList<>();
    private MultiAutoCompleteTextView selectDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_calculator);
        user = (User) getIntent().getParcelableExtra("user");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        selectDropdown = findViewById(R.id.spSelectDogs);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        getDogs();


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                calculatePrice();
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

    private void getDogs() {
        {
            String baseUrl = "https://studev.groept.be/api/a23PT106/user_dogs";
            String urlCreate = baseUrl + "/" + user.getIdUser();



            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonArrayRequest queueRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    urlCreate,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            dogsBreed.clear();
                            dogsWeight.clear();
                            dogIDs.clear();
                            dogNames.clear();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    // This if statement IDK why is needed. If its removed then every dog is added twice.
                                    if (dogNames.size() < response.length()) {
                                        JSONObject o = response.getJSONObject(i);
                                        System.out.println(response.length());
                                        System.out.print(response);
                                        String dogBreed = o.getString("breed");
                                        String dogWeight = o.getString("weight");
                                        String dogName = o.getString("name");
                                        String dogID = o.getString("idPet");
                                        dogsBreed.add(dogBreed);
                                        dogNames.add(dogName);
                                        dogsWeight.add(dogWeight);
                                        dogIDs.add(dogID);
                                    }





                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            for (String dog: dogNames) {
                                System.out.println(dog);
                            }
                            updateMultitext();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(FoodCalculatorActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            requestQueue.add(queueRequest);
        }
    }

    private void updateMultitext() {
        System.out.println(dogNames);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, dogNames);
        selectDropdown.setAdapter(adapter);
        selectDropdown.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }



    private void calculatePrice() {
        String getTextFromMultitext = selectDropdown.getText().toString();
        System.out.println(getTextFromMultitext);
        if (getTextFromMultitext.equals("all") || getTextFromMultitext.equals("ALL") || getTextFromMultitext.equals("All")) {
            System.out.println("getting costs!");
            getCosts(dogsWeight);
        }
        else if (getTextFromMultitext.isEmpty() || (getTextFromMultitext == null)) {
            Toast.makeText(
                    FoodCalculatorActivity.this,
                    "Please fill all the necessary fields.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            // Split the text based on the delimiter (comma in this case)
            String[] items = getTextFromMultitext.split(",");
            System.out.println("Items: " + items.toString());
            for (int i = 0; i < items.length - 1; i++) {
                System.out.println("Item untrimmed: " + items[i]);
                items[i] = items[i].trim();
            }
            for (int i = 0; i < items.length - 1; i++) {
                System.out.println("Item trimmed: " + items[i]);
                selectedDogs.add(items[i]);
            }


            ArrayList<String> selectedWeights = new ArrayList<>();

            for (String name : selectedDogs) {
                selectedWeights.add(dogsWeight.get(dogNames.indexOf(name)));
            }
            getCosts(selectedWeights);
        }
    }

    private void getCosts(ArrayList<String> selectedWeights) {
        double kgCounter = 0.0;
        System.out.println(selectedWeights);

        for (String weight: selectedWeights) {
            System.out.println("Inside getCosts");
            if (Integer.parseInt(weight) < 9) {
                kgCounter += 0.1;
                System.out.println("Inside firstIf");
            }
            else if (Integer.parseInt(weight) < 23) {
                System.out.println("Inside elif");
                kgCounter += 0.2;
            }
            else {
                System.out.println("Inside else");
                kgCounter += 0.4;
            }

        }
        TextView txtNumberOfDays = findViewById(R.id.txtNumberOfDays);
        days = Integer.parseInt(txtNumberOfDays.getText().toString());

        String kgNew1 = String.valueOf(days*kgCounter);
        String kgNew2 = kgNew1.substring(0, (kgNew1.indexOf('.') + 2));
        kg = Double.parseDouble(kgNew2);

        String pNew1 = String.valueOf(days*kgCounter*6);
        String pNew2 = pNew1.substring(0, (pNew1.indexOf('.') + 2));
        price = Double.parseDouble(pNew2);
        displayText();

    }

    private void displayText() {
        TextView textToDisplay = findViewById(R.id.txtPriceAndKg);
        textToDisplay.setText("Price: €" + price + "\n" + "Kilograms: " + kg + "kg");
    }

    private void openPrevious() {
        // Implement your functionality here
        Intent intent = new Intent(this, MyDogsActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

    }
}