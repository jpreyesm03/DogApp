package be.kuleuven.gt.dogapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.kuleuven.gt.dogapp.model.Reminder;
import be.kuleuven.gt.dogapp.model.User;

public class ReminderExpandActivity extends AppCompatActivity {

    private ArrayList<Reminder> reminderList;
    private ArrayList<String> reminderIDs;
    private User user;

    private EditText day;
    private EditText month;
    private EditText year;
    private EditText time;
    private EditText details;


    private int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reminder_expand);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        reminderList = getIntent().getParcelableArrayListExtra("reminders");
        reminderIDs = new ArrayList<>();
        user = getIntent().getParcelableExtra("user");

        day = findViewById(R.id.date);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
        time = findViewById(R.id.time);
        details = findViewById(R.id.details);

        updateSpinner();

    }


    private void updateSpinner() {
        Spinner spinner = findViewById(R.id.spinner);
        reminderIDs.clear();
        for (int i = 0; i<reminderList.size(); i++)
        {
            reminderIDs.add("ReminderID: " + reminderList.get(i).getId());
        }


        // Create an ArrayAdapter using the dog names ArrayList
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, reminderIDs);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // `position` parameter contains the position of the selected item in the dropdown
                // Now you can use `position` as needed\

                pos = position;

                Reminder currentReminder = reminderList.get(position);

                day.setText(currentReminder.getDate());
                month.setText(currentReminder.getMonth());
                year.setText(currentReminder.getYear());
                time.setText(currentReminder.getTime());
                details.setText(currentReminder.getDetails());




            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected (optional)
            }
        });
    }

    public void onBtnButtonSubmit_Clicked(View Caller) {
        fetchInfo(reminderList.get(pos).getId());
    }

    private void fetchInfo(String remid) {


        String day1 = day.getText().toString();
        String month1 = month.getText().toString();
        String year1 = year.getText().toString();
        String time1 = time.getText().toString();
        String details1 = details.getText().toString().replace(" ", "_");


        if (day1.isEmpty() || month1.isEmpty() || year1.isEmpty() || time1.isEmpty() || details1.isEmpty() ){
            Toast.makeText(
                    ReminderExpandActivity.this,
                    "Please fill all necessary info! Feel free to approximate magnitudes.",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (remid.equals("unknown")) {
                Toast.makeText(
                        ReminderExpandActivity.this,
                        "Error connecting - user unknown",
                        Toast.LENGTH_SHORT).show();
            } else {

                String baseUrl = "https://studev.groept.be/api/a23PT106/editReminders";



                ProgressDialog progressDialog = new ProgressDialog(ReminderExpandActivity.this);
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
                                        ReminderExpandActivity.this,
                                        "Reminder added!",
                                        Toast.LENGTH_SHORT).show();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(
                                        ReminderExpandActivity.this,
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
                        params.put("date", day1);
                        params.put("month", month1);
                        params.put("year", year1);
                        params.put("time", time1);
                        params.put("id", remid);

                        return params;
                    }
                };
                requestQueue.add(submitRequest);
            }
        }
    }

    public void onBtnButtonToday2_Clicked(View Caller) {

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


}