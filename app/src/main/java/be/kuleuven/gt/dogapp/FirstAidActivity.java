package be.kuleuven.gt.dogapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Objects;

import be.kuleuven.gt.dogapp.model.User;

public class FirstAidActivity extends AppCompatActivity {
    private User user;
    private String name;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_aid);
        user = (User) getIntent().getParcelableExtra("user");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = getIntent().getStringExtra("name");
        position = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("position")));

        Button btnBack = findViewById(R.id.btnBackFirstAid);
        Button btnNotMove = findViewById(R.id.btnNotMoving);
        Button btnNotEating = findViewById(R.id.btnNotEating);
        Button btnNotStopEating = findViewById(R.id.btnNotStopEating);
        Button btnSymptomChecker = findViewById(R.id.btnSymptomChecker);
        Button btnScratching = findViewById(R.id.btnScratching);
        Button btnEmergencyContact = findViewById(R.id.btnEmergencyContact);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openPrevious();
            }
        });

        btnNotMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openNotMove();
            }
        });

        btnNotEating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openNotEating();
            }
        });

        btnNotStopEating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openNotStopEating();
            }
        });

        btnScratching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openScratching();
            }
        });

        btnSymptomChecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openSymptomChecker();
            }
        });

        btnEmergencyContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openEmergencyContact();
            }
        });

    }

    private void openNotMove() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://wagwalking.com/symptom/why-is-my-dog-unable-to-move"));

        // Start the activity with the Intent
        startActivity(intent);
    }

    private void openNotEating() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.petmd.com/dog/symptoms/why-my-dog-not-eating#whattodowhenyourdogisnoteating"));

        // Start the activity with the Intent
        startActivity(intent);
    }

    private void openNotStopEating() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.petmd.com/dog/symptoms/why-my-dog-always-hungry#treatment"));

        // Start the activity with the Intent
        startActivity(intent);
    }

    private void openScratching() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.akc.org/expert-advice/health/why-is-my-dog-so-itchy/"));

        // Start the activity with the Intent
        startActivity(intent);
    }

    private void openSymptomChecker() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.petmd.com/symptom-checker"));

        // Start the activity with the Intent
        startActivity(intent);

    }

    private void openEmergencyContact() {
        String query = "Emergency Veterinarian Near Me";

        // Create the Intent with the ACTION_VIEW action
        // and a Uri with the search query
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/search?q=" + query));

        // Start the activity with the Intent
        startActivity(intent);
    }



    private void openPrevious() {
        // Implement your functionality here
        Intent intent = new Intent(this, ThreeBarsActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("name", name);
        intent.putExtra("position", String.valueOf(position));
        startActivity(intent);

    }
}