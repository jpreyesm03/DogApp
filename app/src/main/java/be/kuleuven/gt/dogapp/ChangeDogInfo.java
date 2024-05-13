package be.kuleuven.gt.dogapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import be.kuleuven.gt.dogapp.model.User;

public class ChangeDogInfo extends AppCompatActivity {
    private User user;
    private ArrayList<String> dogNames = new ArrayList<>();
    private ArrayList<String> dogIDs = new ArrayList<>();
    private int positionOfSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_dog_info);
        user = (User) getIntent().getParcelableExtra("user");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnBack = findViewById(R.id.btnBackChangeDog);
        Button btnSubmit = findViewById(R.id.btnSubmitChangeDog);
        Button btnChangeImage = findViewById(R.id.btnChangeDogImage);
        Switch switchBreedable = findViewById(R.id.schBreedableChange);
        TextView txtChangeName = findViewById(R.id.txtChangeName);
        TextView txtChangeBreed = findViewById(R.id.txtChangeBreed);
        TextView txtChangeWeight = findViewById(R.id.txtChangeWeight);
        TextView txtChangeHeight = findViewById(R.id.txtChangeHeight);
        TextView txtChangeMedicalConditions = findViewById(R.id.txtChangeMedCond);
        getDogs();


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openPrevious();
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
                                        dogNames.add(dogName);
                                        dogIDs.add(dogID);
                                    }





                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            for (String dog: dogNames) {
                                System.out.println(dog);
                            }
//                            updateSpinner();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(ChangeDogInfo.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            requestQueue.add(queueRequest);
        }
    }




    private void openPrevious() {
        // Implement your functionality here
        Intent intent = new Intent(this, MyDogsActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

    }

//    private void updateSpinner() {
//        Spinner spinner = findViewById(R.id.spMyDogs);
//
//
//        // Create an ArrayAdapter using the dog names ArrayList
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dogNames);
//
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Apply the adapter to the spinner
//        spinner.setAdapter(adapter);
//
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // `position` parameter contains the position of the selected item in the dropdown
//                // Now you can use `position` as needed
//                positionOfSpinner = position;
//                System.out.println(dogIDs.get(positionOfSpinner).toString());
//                System.out.println("before");
//                System.out.println(imageDecode(petImages.get(positionOfSpinner)));
//
//                if (petImages.get(positionOfSpinner).equals("null") || petImages.get(positionOfSpinner).isEmpty()) {
//                    btnMyDog.setImageResource(R.drawable.dogs);
//                    System.out.println("if");
//                    System.out.println(imageDecode(petImages.get(positionOfSpinner)));
//                }
//                else {
//                    btnMyDog.setImageBitmap(imageDecode(petImages.get(positionOfSpinner)));
//                    System.out.println("else");
//                    System.out.println(imageDecode(petImages.get(positionOfSpinner)));
//                }
//
//                schBreedable.setChecked(dogsBreedingState.get(positionOfSpinner));
//                int txtToChangeAge = 0;
//                String txtToChangeBreed = "";
//                if (!dogsAge.get(positionOfSpinner).equals(" ") && !dogsAge.get(positionOfSpinner).equals("_") && !(dogsAge.get(positionOfSpinner) == null)) {
//                    try {
//                        Integer.parseInt(dogsAge.get(positionOfSpinner));
//                        txtAge.setText("Age: " + dogsAge.get(positionOfSpinner));
//                    }
//                    catch (NumberFormatException e){
//                        System.out.println("Not a number!");
//                        txtAge.setText("Age is not specified.");
//                    }
//                }
//                else {
//                    txtAge.setText("Age is not specified.");
//                }
//                if (!dogsBreed.get(positionOfSpinner).equals(" ") && !dogsBreed.get(positionOfSpinner).equals("_") && !(dogsBreed.get(positionOfSpinner) == null)) {
//                    txtBreed.setText("Breed: " + dogsBreed.get(positionOfSpinner));
//                }
//                else {
//                    txtBreed.setText("Breed is not specified.");
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // Handle case when nothing is selected (optional)
//            }
//        });
//    }
}