package be.kuleuven.gt.dogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import be.kuleuven.gt.dogapp.model.User;

public class AddRemindersActivity extends AppCompatActivity {

    private User user;

    private EditText day;
    private EditText month;
    private EditText year;
    private EditText time;
    private EditText details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_reminders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnBack = findViewById(R.id.btnBack);

        day = findViewById(R.id.date);
        month = findViewById(R.id.month);
        year = findViewById(R.id.time);
        time = findViewById(R.id.year);
        details = findViewById(R.id.details);

        user = getIntent().getParcelableExtra("user");

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
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

    }


    public void onBtnButtonSubmit_Clicked(View Caller) {
        fetchInfo();
    }
    public void onBtnButtonToday_Clicked(View Caller) {

        LocalDate today = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            today = LocalDate.now();
            int year2 = today.getYear();
            int month2 = today.getMonthValue(); // Months are 0-based
            int dayOfMonth = today.getDayOfMonth();
            day.setText(String.valueOf(dayOfMonth)); // Convert to String before setting
            month.setText(String.valueOf(month2)); // Convert to String before setting
            year.setText(String.valueOf(year2));


            LocalTime currentTime = LocalTime.now();
            String hour = String.format("%02d", currentTime.getHour());
            String minute = String.format("%02d", currentTime.getMinute());
            time.setText(hour + ":" + minute);

        }

    }


    private void fetchInfo() {



        String id = user.getIdUser();

        String day1 = day.getText().toString();
        String month1 = month.getText().toString();
        String year1 = year.getText().toString();
        String time1 = time.getText().toString();
        String details1 = details.getText().toString().replace(" ", "_");


        if (day1.isEmpty() || month1.isEmpty() || year1.isEmpty() || time1.isEmpty() || details1.isEmpty() ){
            Toast.makeText(
                    AddRemindersActivity.this,
                    "Please fill all necessary info! Feel free to approximate magnitudes.",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (id.equals("unknown")) {
                Toast.makeText(
                        AddRemindersActivity.this,
                        "Error connecting - user unknown",
                        Toast.LENGTH_SHORT).show();
            } else {

                String baseUrl = "https://studev.groept.be/api/a23PT106/addReminders";



                ProgressDialog progressDialog = new ProgressDialog(AddRemindersActivity.this);
                progressDialog.setMessage("Uploading, please wait...");
                progressDialog.show();
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                StringRequest submitRequest = new StringRequest(
                        Request.Method.POST,
                        baseUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                Toast.makeText(
                                        AddRemindersActivity.this,
                                        "Reminder added!",
                                        Toast.LENGTH_SHORT).show();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(
                                        AddRemindersActivity.this,
                                        "Unable to add reminder: " + error,
                                        Toast.LENGTH_LONG).show();

                            }
                        }
                ) { //NOTE THIS PART: here we are passing the POST parameters to the webservice
                    @Override
                    protected Map<String, String> getParams() {
                        /* Map<String, String> with key value pairs as data load */
                        Map<String, String> params = new HashMap<>();
                        params.put("details", details1);
                        params.put("day", day1);
                        params.put("month", month1);
                        params.put("year", year1);
                        params.put("time", time1);
                        params.put("iduser", id);

                        return params;
                    }
                };
                requestQueue.add(submitRequest);
            }
        }
    }
}