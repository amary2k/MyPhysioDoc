package com.example.PhysiotherapistApp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.example.PhysiotherapistApp.Model.UserState;
import com.example.PhysiotherapistApp.Network.DefaultRestClient;
import com.example.PhysiotherapistApp.Network.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Amar on 2016-02-25.
 */
public class PhysioPatientsFragment extends Fragment {

        String response;
        ExpandableListView expandbleLis;
        SimpleExpandableListAdapter expListAdapter;
        ArrayList listGroupData = new ArrayList();
        ArrayList listChildGroupData = new ArrayList();
        public PhysioPatientsFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_physio_patients, container, false);

            expandbleLis = (ExpandableListView) rootView.findViewById(R.id.expandable_list_view);

            response = RestClient.response;
            //response = getArguments().getString("response");

            expListAdapter =
                    new SimpleExpandableListAdapter(
                            rootView.getContext(),
                            listGroupData,              // Creating group List.
                            R.layout.group_row,             // Group item layout XML.
                            new String[] { "Group Item" },  // the key of group item.
                            new int[] { R.id.row_name },    // ID of each group item.-Data under the key goes into this TextView.
                            listChildGroupData,              // childData describes second-level entries.
                            R.layout.child_row,             // Layout for sub-level entries(second level).
                            new String[] {"Child Item"},      // Keys in childData maps to display.
                            new int[] { R.id.grp_child}     // Data under the keys above go into these TextViews.
                    );
            if(response == null);
            //TODO: ADD code to handle null

            JSONObject physio = null;
            try {

                physio = new JSONObject(response);
                JSONArray jsonArray = (JSONArray) physio.get("patients");
                for (int i=0; i<jsonArray.length(); i++) {
                    // Hierarchy for name
                    String name = ((JSONObject) jsonArray.get(i)).getString("name");
                    // Group Data
                    HashMap m = new HashMap();
                    m.put("Group Item", name );
                    listGroupData.add(m);

                    // Child Group Data
                    ArrayList secList = new ArrayList();
                    HashMap cm  = new HashMap();
                    cm.put("Child Item", "Exercise Routine");
                    secList.add( cm );
                    cm = new HashMap();
                    cm.put("Child Item", "Rcommended Video");
                    secList.add( cm );
                    cm = new HashMap();
                    cm.put("Child Item", "Progress Summary");
                    secList.add( cm );
                    listChildGroupData.add(secList);
                }
                if(jsonArray.length() > 0)
                {
                    expandbleLis.setAdapter(expListAdapter);
                    /*expandbleLis.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                            ExpandableListAdapter mAdapter = parent.getExpandableListAdapter();
                            String groupName = (String) ((HashMap)mAdapter.getGroup(groupPosition)).get("Group Item");
                            PatientRestClient patientRestClient = new PatientRestClient(RestClient.GET, getContext().getString(R.string.rest_client_uri_physiotherapist), v, groupName);
                            patientRestClient.execute();
                            return true;
                        }
                    });*/
                    expandbleLis.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                        @Override
                        public boolean onChildClick(ExpandableListView parent, final View v, int groupPosition, int childPosition, long id) {

      /* You must make use of the View v, find the view by id and extract the text as below*/

                            ExpandableListAdapter mAdapter = parent.getExpandableListAdapter();
                            final String groupName = (String) ((HashMap)mAdapter.getGroup(groupPosition)).get("Group Item");
                            //TextView tv = (TextView) v.getParent();
                            //String data = tv.getText().toString();
                            //Log.i("My App", groupName);
                            if (childPosition == 0) {
                                DefaultRestClient defaultRestClient = new DefaultRestClient(RestClient.GET, getContext().getString(R.string.rest_client_uri_physiotherapist), v, null, RestClient.APPLICATION_JSON, getContext()) {
                                    @Override
                                    protected void onPostExecute(String s) {
                                        Intent i = new Intent(getContext(), ExersiceActivity.class);
                                        Bundle b = new Bundle();
                                        b.putString("patientName", groupName);
                                        i.putExtras(b);
                                        startActivity(i);
                                    }
                                };
                                defaultRestClient.execute();
                            }
                            return true;  // i missed this
                        }
                    });
                }
                else
                    expandbleLis.setEmptyView(rootView);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return rootView;
        }

   /* public class PatientRestClient extends AsyncTask<Void,Void,String> {
        RestClient restClient;
        View view;
        String strOption;
        // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;



        public PatientRestClient(String strMethod, String strURL, View view, String strOption){
            initClass(strMethod, strURL, new HashMap<String,String>(), new HashMap<String,String>(), new HashMap<String,String>(), view, strOption);
        }


        public void initClass(String strMethod, String strURL, HashMap<String,String> params, HashMap<String,String> headers, HashMap<String,String> bodyParams, View view, String strOption) {
            this.view = view;
            this.strOption = strOption;
            this.restClient = new RestClient(strMethod,strURL,params,headers, bodyParams, getContext());
        }

        @Override
        protected void onPostExecute(String s) {

            Intent i = new Intent(view.getContext(), ExersiceActivity.class);
            Bundle b = new Bundle();
            b.putString("response", s); //Passing response to new acitivity
            b.putString("patientName", strOption);
            i.putExtras(b);
            startActivity(i);
        }

        @Override
        protected String doInBackground(Void... params) {
            return restClient.callRESTAPI();
        }
    }*/
}
