package com.example.PhysiotherapistApp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.PhysiotherapistApp.Model.Exercise;
import com.example.PhysiotherapistApp.Model.Patient;
import com.example.PhysiotherapistApp.Network.RestClient;
import com.example.PhysiotherapistApp.Utility.ImageTextAdapter;
import com.example.PhysiotherapistApp.Utility.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExerciseListActivity extends AppCompatActivity {

    List<Exercise> exerciseList;
    private ProgressDialog pDialog;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();

        Gson gson = new Gson();
        exerciseList = gson.fromJson(b.getString("response"), new TypeToken<List<Exercise>>(){}.getType());

        if(exerciseList.size() <= 0)
        {
            //TODO: Set List View Empty
        }
        ListView listView = (ListView) findViewById(R.id.exerciseList);
        List<Drawable> imageList = new ArrayList<>();
        List<String> textItems = new ArrayList<>();

        Iterator<Exercise> listIterator = exerciseList.iterator();
        pDialog = Utility.showTranslucentProgressDialog(this);
        while(listIterator.hasNext()){
            Exercise exercise = listIterator.next();

           /* try {
                imageList.add(new BitmapDrawable(getResources(),
                        drawable_from_url(this.getBaseContext().getString(R.string.rest_client_uri) + this.getBaseContext().getString(R.string.rest_client_uri_images) + "/" + exercise.getImageName())));
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            int resourceId = getResources().getIdentifier(exercise.getImageName(), "drawable", getPackageName());
            imageList.add(getResources().getDrawable(resourceId, null ));
            textItems.add(exercise.getExercisePK().getTitle());
        }
        pDialog.dismiss();


        ImageTextAdapter imageTextAdapter = new ImageTextAdapter(this,textItems,imageList);
        listView.setAdapter(imageTextAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", exerciseList.get(position));
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        });

        Button cancelButton = (Button) findViewById(R.id.exerciseListCancel);
        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

    /*Bitmap drawable_from_url(String url) throws java.net.MalformedURLException, java.io.IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection)new URL(url) .openConnection();
        connection.setRequestProperty("User-agent", "Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return x;
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
