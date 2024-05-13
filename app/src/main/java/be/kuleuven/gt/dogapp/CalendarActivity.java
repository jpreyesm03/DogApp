package be.kuleuven.gt.dogapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import be.kuleuven.gt.dogapp.model.Reminder;
import be.kuleuven.gt.dogapp.model.ReminderAdapter;
import be.kuleuven.gt.dogapp.model.User;

public class CalendarActivity extends AppCompatActivity {
    private User user;
    private CalendarView calendar;
    private TextView date_view;

    private LocalDate today;

    private RecyclerView reminderView;

    private List<Reminder> reminderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar);
        user = (User) getIntent().getParcelableExtra("user");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        reminderList = new ArrayList<>();

        reminderView = findViewById( R.id.reminderRecycler );
        ReminderAdapter adapter = new ReminderAdapter( reminderList );
        reminderView.setAdapter( adapter );
        reminderView.setLayoutManager( new LinearLayoutManager( this ));



        Button btnBack = findViewById(R.id.btnBack);
        calendar = (CalendarView)
                findViewById(R.id.calendar);
        date_view = (TextView)
                findViewById(R.id.date_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            today = LocalDate.now();
            int year = today.getYear();
            int month = today.getMonthValue() - 1; // Months are 0-based
            int dayOfMonth = today.getDayOfMonth();
            date_view.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
            getReminders(dayOfMonth, month, year);
        }




        calendar
                .setOnDateChangeListener(
                        new CalendarView
                                .OnDateChangeListener() {
                            @Override

                            // In this Listener have one method
                            // and in this method we will
                            // get the value of DAYS, MONTH, YEARS
                            public void onSelectedDayChange(
                                    @NonNull CalendarView view,
                                    int year,
                                    int month,
                                    int dayOfMonth)
                            {

                                // Store the value of date with
                                // format in String type Variable
                                // Add 1 in month because month
                                // index is start with 0

                                getReminders(dayOfMonth,month,year);
                                String Date
                                        = dayOfMonth + "-"
                                        + (month + 1) + "-" + year;

                                // set this date in TextView for Display
                                date_view.setText(Date);
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

    private void getReminders(int day, int month, int year) {

        month++;

        String baseUrl = "https://studev.groept.be/api/a23PT106/getReminders";
        String urlCreate = baseUrl + "/" + user.getIdUser() + "/" + day + "/" + month + "/" + year;



        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest queueRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlCreate,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        reminderList.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject o = response.getJSONObject(i);
                                String details = o.getString("details");
                                String date = o.getString("date");
                                String month = o.getString("month");
                                String year = o.getString("year");
                                String time = o.getString("time");

                                Reminder reminder = new Reminder(details,date,month,year,time);
                                reminderList.add(reminder);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        reminderView.getAdapter().notifyDataSetChanged();



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(CalendarActivity.this, "An error occurred. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(queueRequest);
    }

    public void onBtnToday_Clicked(View Caller) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int year = today.getYear();
            int month = today.getMonthValue() - 1; // Months are 0-based
            int dayOfMonth = today.getDayOfMonth();
            Calendar calendarInstance = Calendar.getInstance();
            calendarInstance.set(year, month, dayOfMonth); // Set calendar to today

            long todayMillis = calendarInstance.getTimeInMillis(); // Get milliseconds since Jan 1, 1970
            calendar.setDate(todayMillis);
            getReminders(dayOfMonth, month, year);
            date_view.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
        }
    }



    private void openPrevious() {
        // Implement your functionality here
        Intent intent = new Intent(this, MyDogsActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

    }
}