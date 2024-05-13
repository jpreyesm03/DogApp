package be.kuleuven.gt.dogapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.util.Calendar;

import be.kuleuven.gt.dogapp.model.User;

public class CalendarActivity extends AppCompatActivity {
    private User user;
    private CalendarView calendar;
    private TextView date_view;

    private LocalDate today;

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

    public void onBtnToday_Clicked(View Caller) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int year = today.getYear();
            int month = today.getMonthValue() - 1; // Months are 0-based
            int dayOfMonth = today.getDayOfMonth();
            Calendar calendarInstance = Calendar.getInstance();
            calendarInstance.set(year, month, dayOfMonth); // Set calendar to today

            long todayMillis = calendarInstance.getTimeInMillis(); // Get milliseconds since Jan 1, 1970
            calendar.setDate(todayMillis);
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