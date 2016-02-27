package com.example.PhysiotherapistApp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import java.io.IOException;

public class VideosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        Bundle extras = getIntent().getExtras();
        String vidAddress = extras.getString("videoURI");

        VideoView vidView = (VideoView)findViewById(R.id.videoView);
        //String vid = "http://nihseniorhealth.gov/hipreplacement/faq/video/physicaltherapy_hi_8.flv";
        //String vidAddress = "http://10.100.58.118:5926/videos/physicaltherapy_hi_8.3gp";
        Uri vidUri = Uri.parse(vidAddress);

        vidView.setVideoURI(vidUri);
        vidView.start();
    }
}
