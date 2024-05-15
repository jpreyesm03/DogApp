package be.kuleuven.gt.dogapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
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
import java.util.Objects;

import be.kuleuven.gt.dogapp.model.User;

public class ThreeBarsActivity extends AppCompatActivity {
    private User user;
    private String name;
    private int position;
    private ImageView btnMyDog;
    private ArrayList<String> image = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_three_bars);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = (User) getIntent().getParcelableExtra("user");
        name = getIntent().getStringExtra("name");
        position = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("position")));

        ConstraintLayout btnMain = findViewById(R.id.messageSelect);
        btnMyDog = findViewById(R.id.imgMyDogThreeBars);
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
        getImage(user);

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
                openThreeBarsFunction();
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openThreeBarsFunction();
            }
        });

        btnBreeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openThreeBarsFunction();
            }
        });

        btnCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openThreeBarsFunction();
            }
        });

        btnTrainingVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openThreeBarsFunction();
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
        Spinner spinner = findViewById(R.id.spMyDogs);
        ArrayList<String> items = new ArrayList<>();
        items.add(name);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setEnabled(false);
    }

    private void loadImage() {
        if (image.get(0).isEmpty() || image.get(0).equals("null")) {
            btnMyDog.setImageResource(R.drawable.dogs);
        }
        else {
            btnMyDog.setImageBitmap(imageDecode(image.get(0)));
        }
    }

    private Bitmap imageDecode(String petImage) {
        byte[] decodedBytes = Base64.decode(petImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void getImage(User u)
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
                        image.clear();
                        boolean match = false;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject o = response.getJSONObject(i);
                                String petImage = o.getString("petimage");

                                if (i == position) { image.add(petImage); }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        loadImage();
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
        intent.putExtra("name", name);
        intent.putExtra("position", String.valueOf(position));
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
        intent.putExtra("name", name);
        intent.putExtra("position", String.valueOf(position));
        startActivity(intent);
    }


    private void openThreeBarsFunction() {
        // Implement your functionality here
        Intent intent = new Intent(this, MyDogsActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("name", name);
        intent.putExtra("position", String.valueOf(position));
        startActivity(intent);
        finish();
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


}