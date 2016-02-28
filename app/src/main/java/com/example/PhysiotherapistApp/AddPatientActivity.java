package com.example.PhysiotherapistApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.PhysiotherapistApp.Model.Address;
import com.example.PhysiotherapistApp.Model.Patient;
import com.example.PhysiotherapistApp.Model.Physiotherapist;
import com.example.PhysiotherapistApp.Model.UserState;
import com.example.PhysiotherapistApp.Network.DefaultRestClient;
import com.example.PhysiotherapistApp.Network.RestClient;
import com.example.PhysiotherapistApp.Utility.Utility;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.HashMap;


public class AddPatientActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    //private View mProgressView;
    //private View mFormView;
    private Gson gson = new Gson();
    private Patient existingPatient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText editTextName;
        final EditText editTextCity;
        final EditText editTextPinCode;
        final EditText editTextBranch;
        final EditText editTextState;
        final EditText editTextHospitalName;
        final EditText editTextStreet;
        final EditText editTextEmail;

        Button saveButton = (Button) findViewById(R.id.saveButton);
        editTextEmail = ((EditText) (findViewById(R.id.editTextEmail)));
        editTextName = ((EditText) (findViewById(R.id.editTextName)));
        editTextCity = ((EditText) (findViewById(R.id.editTextCity)));
        editTextPinCode = ((EditText) (findViewById(R.id.editTextPinCode)));
        editTextBranch = ((EditText) (findViewById(R.id.editTextBranch)));
        editTextState = ((EditText) (findViewById(R.id.editTextState)));
        editTextHospitalName = ((EditText) (findViewById(R.id.editTextHospitalName)));
        editTextStreet = ((EditText) (findViewById(R.id.editTextStreet)));

        if(!UserState.isPhysio()) {
            existingPatient = gson.fromJson(RestClient.response,Patient.class);
            editTextEmail.setText(existingPatient.getEmail());
            editTextName.setText(existingPatient.getName());
            editTextBranch.setText(existingPatient.getBranch());
            editTextHospitalName.setText(existingPatient.getHospital_name());
        }

        saveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Patient patient;
                String methdoCall;
                if(UserState.isPhysio()) {
                    methdoCall = RestClient.POST;
                    patient = new Patient(editTextName.getText().toString(),
                            editTextBranch.getText().toString(),
                            editTextHospitalName.getText().toString(),
                            editTextEmail.getText().toString());
                }
                else
                {
                    methdoCall = RestClient.PUT;
                    existingPatient.setName(editTextName.getText().toString());
                    existingPatient.setBranch(editTextBranch.getText().toString());
                    existingPatient.setHospital_name(editTextHospitalName.getText().toString());
                    existingPatient.setEmail(editTextEmail.getText().toString());
                    patient = existingPatient;
                }
                pDialog = Utility.showTranslucentProgressDialog(AddPatientActivity.this);


                String body = gson.toJson(patient);


                DefaultRestClient defaultRestClient = new DefaultRestClient(methdoCall, getBaseContext().getString(R.string.rest_client_uri_patient), v, body, RestClient.APPLICATION_JSON, getBaseContext()) {
                    @Override
                    protected void onPostExecute(String s) {
                        Toast.makeText(getApplicationContext(), "Saved Succesfuly", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                        finish();
                    }
                };
                defaultRestClient.execute();

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
