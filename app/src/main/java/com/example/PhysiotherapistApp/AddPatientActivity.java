package com.example.PhysiotherapistApp;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.PhysiotherapistApp.Model.Address;
import com.example.PhysiotherapistApp.Model.Patient;
import com.example.PhysiotherapistApp.Model.Physiotherapist;
import com.example.PhysiotherapistApp.Network.RestClient;
import com.example.PhysiotherapistApp.Utility.Utility;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.HashMap;


public class AddPatientActivity extends ActionBarActivity {

    private View mProgressView;
    private View mFormView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        mFormView = findViewById(R.id.saveButton);
        mProgressView = findViewById(R.id.progressBar);
       /* if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
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

        saveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Patient patient = new Patient(editTextName.getText().toString(),
                        editTextBranch.getText().toString(),
                        editTextHospitalName.getText().toString(),
                        editTextEmail.getText().toString());

                Utility.showProgress(mProgressView, mFormView, getBaseContext(), true);

                Gson gson = new Gson();
                String body = gson.toJson(patient);



                ProfiletRestClient profiletRestClient = new ProfiletRestClient(RestClient.POST, getBaseContext().getString(R.string.rest_client_uri_patient), v, body, RestClient.APPLICATION_JSON);
                profiletRestClient.execute();
                    /*MainActivity.profile = new Profile();

                    MainActivity.profile.setName(editTextName.getText().toString());
                    MainActivity.profile.setAddress(editTextAddress.getText().toString());
                    //MainActivity.profile.setNumberOfDevices(Integer.valueOf(editTextNoOfDevices.getText().tring()));
                    MainActivity.profile.setPhoneNo(Integer.valueOf(editTextPhoneNo.getText().toString()));
                    MainActivity.profile.setBranch(editTextBranch.getText().toString());
                    MainActivity.profile.setsIN(Integer.valueOf(editTextSIN.getText().toString()));
                    MainActivity.profile.setHistory(editTextHistory.getText().toString());
                    MainActivity.profile.setCurrentDiag(editTextCurrentDiag.getText().toString());
                    MainActivity.profile.setExercisePeriod(editTextExercisePeriod.getText().toString());*/



                // TODO: Fix Move back to Main Activity
                /*if (getActivity().findViewById(R.id.container) != null){
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.popBackStackImmediate();
                }
                else{getActivity().*///finish();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /*public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        EditText editTextName;
        EditText editTextAddress;
        EditText editTextPhoneNo;
        EditText editTextBranch;
        EditText editTextSIN;
        EditText editTextHistory;
        EditText editTextCurrentDiag;
        EditText editTextExercisePeriod;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_patient, container, false);

            Button saveButton = (Button) rootView.findViewById(R.id.saveButton);
            editTextName = ((EditText) (rootView.findViewById(R.id.editTextName)));
            editTextAddress = ((EditText) (rootView.findViewById(R.id.editTextAddress)));
            editTextPhoneNo = ((EditText) (rootView.findViewById(R.id.editTextPhoneNo)));
            editTextBranch = ((EditText) (rootView.findViewById(R.id.editTextBranch)));
            editTextSIN = ((EditText) (rootView.findViewById(R.id.editTextSIN)));
            editTextHistory = ((EditText) (rootView.findViewById(R.id.editTextHistory)));
            editTextCurrentDiag = ((EditText) (rootView.findViewById(R.id.editTextCurrentDiag)));
            editTextExercisePeriod = ((EditText) (rootView.findViewById(R.id.editTextExercisePeriod)));
            saveButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Physiotherapist physio = new Physiotherapist();
                    physio.setBranch("Oliver St");
                    physio.setHospital_name("Thunder Bay Hospital");
                    physio.setName("David");
                    Address addr = new Address();
                    addr.setCity("Thunder Bay");
                    addr.setPincode("P7B3G4");
                    addr.setState("Ontario");
                    addr.setStreet("Ontario Street");
                    physio.setAddress(addr);
                    physio.addPatients(new Patient("Bhavesh", "Markham", "Stoufville Hospital", new Date(), addr, addr));
                    physio.setJoining_date(new Date());

                    Gson gson = new Gson();
                    gson.toJson(physio);

                    ProfiletRestClient profiletRestClient = new ProfiletRestClient(RestClient.POST, getContext().getString(R.string.rest_client_uri_logout), v);
                    profiletRestClient.execute();
                    *//*MainActivity.profile = new Profile();

                    MainActivity.profile.setName(editTextName.getText().toString());
                    MainActivity.profile.setAddress(editTextAddress.getText().toString());
                    //MainActivity.profile.setNumberOfDevices(Integer.valueOf(editTextNoOfDevices.getText().toString()));
                    MainActivity.profile.setPhoneNo(Integer.valueOf(editTextPhoneNo.getText().toString()));
                    MainActivity.profile.setBranch(editTextBranch.getText().toString());
                    MainActivity.profile.setsIN(Integer.valueOf(editTextSIN.getText().toString()));
                    MainActivity.profile.setHistory(editTextHistory.getText().toString());
                    MainActivity.profile.setCurrentDiag(editTextCurrentDiag.getText().toString());
                    MainActivity.profile.setExercisePeriod(editTextExercisePeriod.getText().toString());*//*



                    // TODO: Fix Move back to Main Activity
                    if (getActivity().findViewById(R.id.container) != null){
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStackImmediate();
                    }
                    else{getActivity().finish();}

                }
            });

            return rootView;
        }

    }*/
    public class ProfiletRestClient extends AsyncTask<Void,Void,String> {
        RestClient restClient;
        View view;
        // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;



        public ProfiletRestClient(String strMethod, String strURL, View view, String body, String contentType){
            initClass(strMethod, strURL, new HashMap<String,String>(), new HashMap<String,String>(), new HashMap<String,String>(), view, body, contentType);
        }


        public void initClass(String strMethod, String strURL, HashMap<String,String> params, HashMap<String,String> headers, HashMap<String,String> bodyParams, View view, String body, String contentType) {
            this.view = view;
            this.restClient = new RestClient(strMethod,strURL,params,headers, bodyParams, getBaseContext(), body, contentType);
        }

        @Override
        protected void onPostExecute(String s) {
            /*Toast successToast = Toast.makeText(view.getContext(), "Profile Saved Successfully!", LENGTH_SHORT);
            successToast.show();*/
            Toast.makeText(getApplicationContext(), "Saved Succesfuly", Toast.LENGTH_SHORT).show();
            Utility.showProgress(mProgressView, mFormView, getBaseContext(), false);
            finish();

            /*if (getActivity().findViewById(R.id.container) != null){
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStackImmediate();
            }
            else{getActivity().finish();}*/
            /*Intent i;
            switch (intOptionNo) {
                case 0:
                    i = new Intent(view.getContext(), PhysioPatientsActivity.class);
                    Bundle b = new Bundle();
                    b.putString("response", s); //Passing response to new acitivity
                    i.putExtras(b);
                    startActivity(i);
                    break;
                case 4:
                    i = new Intent(getBaseContext().getApplicationContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("EXIT", true);
                    startActivity(i);
                    break;
            }*/
        }

        @Override
        protected String doInBackground(Void... params) {
            return restClient.callRESTAPI();
        }
    }
}
