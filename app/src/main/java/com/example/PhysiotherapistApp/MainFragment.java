package com.example.PhysiotherapistApp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PhysiotherapistApp.Model.UserState;
import com.example.PhysiotherapistApp.Network.RestClient;
import com.example.PhysiotherapistApp.Utility.Utility;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * Created by Amar on 2016-02-06.
 */
public class MainFragment extends Fragment {

    private View mProgressView;
    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String[] mainMenuItems;

        if(UserState.isPhysio()){
            // Physiotherapist Menu
            mainMenuItems = new String[]{
                "My Patients",
                        "Add Patient",
                        "Manage Videos",
                        "Messenger",
                        "Logout"

            };
        }
        else{
            // Patient Menu
            mainMenuItems = new String[]{
                "My Profile",
                        "Exercise Videos",
                        "Exercise Routine",
                        "Messenger",
                        "Logout"

            };
        }



        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_layout,
                R.id.list_item_menu_item,
                mainMenuItems);

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mProgressView = getActivity().findViewById(R.id.progressBar);

        ListView myListView = (ListView) rootView.findViewById(R.id.listView_menu);
        myListView.setAdapter(myArrayAdapter);
        //final PlaceholderFragment currentView = this;
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemText = (String) ((TextView) view).getText();

                if(UserState.isPhysio()) {
                    if (mainMenuItems[0].equalsIgnoreCase(itemText)) {

                        PatientRestClient patientRestClient = new PatientRestClient(RestClient.GET, getContext().getString(R.string.rest_client_uri_physiotherapist), view, 0);
                        patientRestClient.execute();

                    }
                    // Redirect to Add Patient
                    else if (mainMenuItems[1].equalsIgnoreCase(itemText)) {
                        Intent i = new Intent(view.getContext(), AddPatientActivity.class);
                        startActivity(i);
                        /*IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
                        scanIntegrator.initiateScan();*/
                    } else if (mainMenuItems[2].equalsIgnoreCase(itemText)) {
                        Intent i = new Intent(view.getContext(), VideosActivity.class);
                        startActivity(i);
                    }
                    // Redirect to a website
                    else if (mainMenuItems[3].equalsIgnoreCase(itemText)) {
                       /* Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                        startActivity(i);*/
                        Intent i = new Intent(view.getContext(), ExersiceActivity.class);
                        startActivity(i);
                    } else if (mainMenuItems[4].equalsIgnoreCase(itemText)) {
                        PatientRestClient patientRestClient = new PatientRestClient(RestClient.POST, getContext().getString(R.string.rest_client_uri_logout), view, 4);
                        patientRestClient.execute();
                        /*Intent i = new Intent(view.getContext(), AddPatientActivity.class);
                        startActivity(i);*/
                    }
                }
                else{
                    if (mainMenuItems[0].equalsIgnoreCase(itemText)) {

                        PatientRestClient patientRestClient = new PatientRestClient(RestClient.GET, getContext().getString(R.string.rest_client_uri_physiotherapist), view, 0);
                        patientRestClient.execute();

                    }
                    // Redirect to BarCode Scanner App
                    else if (mainMenuItems[1].equalsIgnoreCase(itemText)) {
                        Intent i = new Intent(view.getContext(), AddPatientActivity.class);
                        startActivity(i);
                        /*IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
                        scanIntegrator.initiateScan();*/
                    } else if (mainMenuItems[2].equalsIgnoreCase(itemText)) {
                        Intent i = new Intent(view.getContext(), VideosActivity.class);
                        startActivity(i);
                    }
                    // Redirect to a website
                    else if (mainMenuItems[3].equalsIgnoreCase(itemText)) {
                       /* Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                        startActivity(i);*/
                        Intent i = new Intent(view.getContext(), ExersiceActivity.class);
                        startActivity(i);
                    } else if (mainMenuItems[4].equalsIgnoreCase(itemText)) {
                        PatientRestClient patientRestClient = new PatientRestClient(RestClient.POST, getContext().getString(R.string.rest_client_uri_logout), view, 4);
                        patientRestClient.execute();
                        /*Intent i = new Intent(view.getContext(), AddPatientActivity.class);
                        startActivity(i);*/
                    }
                }
                    /*else if (mainMenuItems[5].equalsIgnoreCase(itemText)) {

                        // These two need to be declared outside the try/catch
                        // so that they can be closed in the finally block.
                        HttpURLConnection urlConnection = null;
                        BufferedReader reader = null;

                        // Will contain the raw JSON response as a string.
                        String coolClimateAPIJsonStr = null;
                        // Construct the URL for the OpenWeatherMap query
                        // Possible parameters are avaiable at OWM's forecast API page, at
                        // http://openweathermap.org/API#forecast
                        //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
                        String zipcode = MainActivity.profile.getPostalCode();
                        if (zipcode == null || zipcode.length() == 0) {
                            Toast successToast = Toast.makeText(view.getContext(), "Please enter Zipcode in Profile!", Toast.LENGTH_SHORT);
                            successToast.show();
                            return;
                        }
                        try {
                            URL url = new URL("https://apis.berkeley.edu:443/coolclimate/footprint-defaults?input_location=" + zipcode + "&input_income=1&input_location_mode=1&input_size=0");

                            // Create the request to OpenWeatherMap, and open the connection
                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("GET");
                            // String userCredentials = "username:password";
                            //String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
                            urlConnection.setRequestProperty("App_id", "1c59b0f9");
                            urlConnection.setRequestProperty("App_key", "d85b07f8673cb00694d6eab51fe3dd16");
                            // urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            //urlConnection.setRequestProperty("Content-Length", "" + Integer.toString(postData.getBytes().length));
                            //urlConnection.setRequestProperty("Content-Language", "en-US");
                            urlConnection.setUseCaches(false);
                            urlConnection.setDoInput(true);
                            urlConnection.setDoOutput(true);
                            urlConnection.connect();

                            // Read the input stream into a String
                            InputStream inputStream = urlConnection.getInputStream();
                            StringBuffer buffer = new StringBuffer();
                            if (inputStream == null) {
                                // Nothing to do.
                                return;
                            }
                            reader = new BufferedReader(new InputStreamReader(inputStream));

                            String line;
                            while ((line = reader.readLine()) != null) {
                                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                                // But it does make debugging a *lot* easier if you print out the completed
                                // buffer for debugging.
                                buffer.append(line + "\n");
                            }

                            if (buffer.length() == 0) {
                                // Stream was empty.  No point in parsing.
                                return;
                            }
                            coolClimateAPIJsonStr = buffer.toString();
                        } catch (IOException e) {
                            Log.e("PlaceholderFragment", "Error ", e);
                            // If the code didn't successfully get the weather data, there's no point in attemping
                            // to parse it.
                            return;
                        } finally {
                            if (urlConnection != null) {
                                urlConnection.disconnect();
                            }
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (final IOException e) {
                                    Log.e("PlaceholderFragment", "Error closing stream", e);
                                }
                            }
                        }
                    }*/
            }

            public void onActivityResult(int requestCode, int resultCode, Intent intent) {
                if (requestCode == 0) {
                    if (resultCode == Activity.RESULT_OK) {
                        String contents = intent.getStringExtra("SCAN_RESULT");
                        String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                        // Handle successful scan
                        Toast myToast = new Toast(getActivity());
                        myToast.setText("contents:" + contents + "|format:" + format);
                        myToast.show();
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        // Handle cancel
                    }
                }
            };
        });

        return rootView;
    }

    public class PatientRestClient extends AsyncTask<Void,Void,String> {
        RestClient restClient;
        View view;
        int intOptionNo;
        // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;



        public PatientRestClient(String strMethod, String strURL, View view, int intOptionNo){
            initClass(strMethod, strURL, new HashMap<String,String>(), new HashMap<String,String>(), new HashMap<String,String>(), view, intOptionNo);
        }


        public void initClass(String strMethod, String strURL, HashMap<String,String> params, HashMap<String,String> headers, HashMap<String,String> bodyParams, View view, int intOptionNo) {
            Utility.showProgress(mProgressView, getActivity().getBaseContext(), true);
            this.view = view;
            this.intOptionNo = intOptionNo;
            this.restClient = new RestClient(strMethod,strURL,params,headers, bodyParams, getContext());
        }

        @Override
        protected void onPostExecute(String s) {
            Intent i;
            Utility.showProgress(mProgressView, getActivity().getBaseContext(), false);
            if(UserState.isPhysio()) {
                switch (intOptionNo) {
                    case 0:
                        i = new Intent(view.getContext(), PhysioPatientsActivity.class);
                        Bundle b = new Bundle();
                        b.putString("response", s); //Passing response to new acitivity
                        i.putExtras(b);
                        startActivity(i);
                        break;
                    case 4:
                        i = new Intent(getContext().getApplicationContext(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        break;
                }
            }
            else{
                switch (intOptionNo) {
                    case 0:
                        i = new Intent(view.getContext(), PhysioPatientsActivity.class);
                        Bundle b = new Bundle();
                        b.putString("response", s); //Passing response to new acitivity
                        i.putExtras(b);
                        startActivity(i);
                        break;
                    case 4:
                        i = new Intent(getContext().getApplicationContext(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        break;
                }
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            return restClient.callRESTAPI();
        }
    }
}