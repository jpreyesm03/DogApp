package be.kuleuven.gt.dogapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import be.kuleuven.gt.dogapp.model.User;

public class TrainingVideosActivity extends AppCompatActivity {
    private User user;
    private ArrayList<String> videoIDs = new ArrayList<>();
    private int positionOfVideo;
    private WebView youtubeVideo;
    private boolean firstTime;
    private Button btnNextVideo;
    private Button btnPreviousVideo;
    private TextView txtFraction;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_training_videos);
        user = (User) getIntent().getParcelableExtra("user");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.messageSelect), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnBack = findViewById(R.id.btnBackTrainingVideos);
        btnNextVideo = findViewById(R.id.btnNextVideo);
        btnPreviousVideo = findViewById(R.id.btnPreviousVideo);
        youtubeVideo = findViewById(R.id.webView);
        txtFraction = findViewById(R.id.txtFraction);
        videoIDs.add("8DHyOAvGwGw?si=Irc2LL96JQcmkHfI");
        videoIDs.add("CRoDTUkzVpU?si=gRTlEMFmEUQyyFi");
        videoIDs.add("NMRRLUyAIyw?si=_Q97tGOou13SGQAS");
        videoIDs.add("Qszx6laEzoU?si=BottiZy9u8_k4bs8");
        videoIDs.add("AIFOZsn-bFE?si=gOIHxPHllscSfrjT");
        videoIDs.add("2BT8lYjXhYw?si=HzLEvnGSq1aIf_Gc");
        videoIDs.add("hK3F8ht0bpQ?si=Z_iMrZRNsrzJSr5K");
        videoIDs.add("l72Hpfcs0JA?si=kfkqZ9oVhg6NLJQl");
        videoIDs.add("MBNN2JOuP8I?si=e42ORrDj2ZCLgMfR");
        videoIDs.add("4HOa2IUKWR8?si=L4GXTywfqU42xUPB");
        videoIDs.add("-fcVPfkbYIM?si=I4Nc-G5QlEbzgK-4");
        videoIDs.add("Y5IF1aymG1I?si=UshB1yR0UhX0TSw1");
        videoIDs.add("SGmdB03zGqQ?si=gwR-pyNnr7HHkEDz");
        videoIDs.add("nF1Wb_94_ec?si=0C9wlrV4pvY1-Gm2");
        videoIDs.add("JNNNy8iuwEs?si=Q-kXg5pgg3_FuDoF");
        videoIDs.add("UNFPljFzJq0?si=EsuArEPnJ9qqeUru");
        firstTime = true;
        loadVideo();





        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                openPrevious();
            }
        });

        btnNextVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                positionOfVideo ++;
                loadVideo();
            }
        });
        btnPreviousVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the desired function
                positionOfVideo --;
                loadVideo();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void loadVideo() {
        if (firstTime) {
            positionOfVideo = 0;
            firstTime = false;
        }
        if (positionOfVideo < 0) {
            positionOfVideo = videoIDs.size() - 1;
            Toast.makeText(TrainingVideosActivity.this, "Going to last video.", Toast.LENGTH_SHORT).show();
        }
        else if (positionOfVideo == videoIDs.size()) {
            positionOfVideo = 0;
            Toast.makeText(TrainingVideosActivity.this, "Going to first video.", Toast.LENGTH_SHORT).show();
        }
        txtFraction.setText("Current Video: " + String.valueOf(positionOfVideo + 1) + "/16");
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoIDs.get(positionOfVideo).toString() + " title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        youtubeVideo.loadData(video, "text/html","utf-8");
        youtubeVideo.getSettings().setJavaScriptEnabled(true);
        youtubeVideo.setWebChromeClient(new WebChromeClient());
    }

    private void openPrevious() {
        // Implement your functionality here
        Intent intent = new Intent(this, MyDogsActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

    }

}