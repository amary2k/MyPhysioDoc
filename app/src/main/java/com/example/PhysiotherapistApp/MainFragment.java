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
import com.example.PhysiotherapistApp.Network.DefaultRestClient;
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
                "My Patients",      //Done
                "Add Patient",      //Done
                "Manage Videos",    //Done
                "Messenger",
                "Logout"            //Done

            };
        }
        else{
            // Patient Menu
            mainMenuItems = new String[]{
                "Exercise Routine", //Done
                    "My Profile",   //Done
                "Messenger",
                "Logout"            //Done

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
                        DefaultRestClient defaultRestClient = new DefaultRestClient(RestClient.GET, getContext().getString(R.string.rest_client_uri_physiotherapist), view, null, RestClient.APPLICATION_JSON, getContext()) {
                            @Override
                            protected void onPostExecute(String s) {
                                Intent i = new Intent(getContext(), PhysioPatientsActivity.class);
                                /*Bundle b = new Bundle();
                                b.putString("response", s); //Passing response to new acitivity
                                i.putExtras(b);*/
                                startActivity(i);
                            }
                        };
                        defaultRestClient.execute();

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
                       // TODO: Under Construction
                        /*Intent i = new Intent(view.getContext(), ExersiceActivity.class);
                        startActivity(i);*/
                    } else if (mainMenuItems[4].equalsIgnoreCase(itemText)) {

                        DefaultRestClient defaultRestClient = new DefaultRestClient(RestClient.POST, getContext().getString(R.string.rest_client_uri_logout), view, null, RestClient.APPLICATION_JSON, getContext()) {
                            @Override
                            protected void onPostExecute(String s) {
                                Intent i = new Intent(getContext().getApplicationContext(), LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        };
                        defaultRestClient.execute();
                    }
                }
                else{ // Patient part starts here
                    // Redirect to Exercise Patients
                    if (mainMenuItems[0].equalsIgnoreCase(itemText)) {
                        DefaultRestClient defaultRestClient = new DefaultRestClient(
                                RestClient.GET,
                                getContext().getString(R.string.rest_client_uri_patient),
                                view, null,
                                RestClient.APPLICATION_JSON,
                                getContext()) {
                            @Override
                            protected void onPostExecute(String s) {
                                Intent i = new Intent(getContext(), ExersiceActivity.class);
                                startActivity(i);
                            }
                        };
                        defaultRestClient.execute();
                    }
                    else if (mainMenuItems[1].equalsIgnoreCase(itemText)) {
                        DefaultRestClient defaultRestClient = new DefaultRestClient(RestClient.GET, getContext().getString(R.string.rest_client_uri_patient), view, null, RestClient.APPLICATION_JSON, getContext()) {
                            @Override
                            protected void onPostExecute(String s) {
                                Intent i = new Intent(getContext(), AddPatientActivity.class);
                                startActivity(i);
                            }
                        };
                        defaultRestClient.execute();
                    } /*else if (mainMenuItems[2].equalsIgnoreCase(itemText)) {
                        Intent i = new Intent(view.getContext(), VideosActivity.class);
                        startActivity(i);
                    }*/
                    // Redirect to a website
                    else if (mainMenuItems[2].equalsIgnoreCase(itemText)) {
                       /* Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                        startActivity(i);*/
                        Intent i = new Intent(view.getContext(), ExersiceActivity.class);
                        startActivity(i);
                    } else if (mainMenuItems[3].equalsIgnoreCase(itemText)) {
                        DefaultRestClient defaultRestClient = new DefaultRestClient(RestClient.POST, getContext().getString(R.string.rest_client_uri_logout), view, null, RestClient.APPLICATION_JSON, getContext()) {
                            @Override
                            protected void onPostExecute(String s) {
                                Intent i = new Intent(getContext().getApplicationContext(), LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        };
                        defaultRestClient.execute();
                    }
                }
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
}