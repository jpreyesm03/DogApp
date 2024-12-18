package be.kuleuven.gt.dogapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import be.kuleuven.gt.dogapp.model.User;

public class HomeScreenActivity extends AppCompatActivity {

    private TextView txtInfo;
    private ImageView imageUser;
    private User user;
    private Button b1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        user = (User) getIntent().getParcelableExtra("user");

        b1 = (Button) findViewById(R.id.seeDogs);
        Button btnChangeDogInfo = findViewById(R.id.btnChangeDogInformation);
        TextView btnChangeProfileInfo = findViewById(R.id.btnChangeProfileInfo);

        imageUser = findViewById(R.id.imageUser);
        txtInfo = (TextView) findViewById(R.id.btnChangeProfileInfo);
        txtInfo.setText("Hey " + user.getUsername() + ", click here to update your profile information.");
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnChangeDogInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openChangeDogInformation();
            }
        });

        imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openChangeProfileInformation();
            }
        });

        btnChangeProfileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openChangeProfileInformation();
            }
        });

    }

    private void openChangeDogInformation() {
        Intent intent = new Intent(this, ChangeDogInfoActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    private void openChangeProfileInformation() {
        Intent intent = new Intent(this,ChangeProfileInfoActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }


    public void onBtnAddDogs_Clicked(View Caller)
    {

        Intent intent = new Intent(this,AddDogActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
        finish();
    }

    public void onBtnSeeMyDogs_Clicked(View Caller)
    {
        Intent intent = new Intent(this,MyDogsActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    public void onBtnIAmADogWalker_Clicked(View Caller)
    {
        Intent intent = new Intent(this,BecomeDogWalkerActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    public void onBtnLogout_Clicked(View Caller)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().clear().apply();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}