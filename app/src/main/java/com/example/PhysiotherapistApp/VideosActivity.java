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

        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource("http://www.cyberpt.com/cptqtws16HS.mov");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
        /*VideoView vidView = (VideoView)findViewById(R.id.videoView);

        String vidAddress = "http://nihseniorhealth.gov/nihsh/hipreplacement/faq/video/physicaltherapy_hi_8.flv";
        Uri vidUri = Uri.parse(vidAddress);

        vidView.setVideoURI(vidUri);
        vidView.start();*/
    }
}
