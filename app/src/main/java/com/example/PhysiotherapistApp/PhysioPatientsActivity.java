package com.example.PhysiotherapistApp;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class PhysioPatientsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlaceholderFragment placeholderFragment = new PlaceholderFragment();
        Bundle b = getIntent().getExtras();
        // Passing response from main menu to fragment
        placeholderFragment.setArguments(b);

        setContentView(R.layout.activity_physio_patients);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, placeholderFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_solutions, menu);
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

    public static class PlaceholderFragment extends Fragment {

        String response;
        ExpandableListView expandbleLis;
        SimpleExpandableListAdapter expListAdapter;
        ArrayList listGroupData = new ArrayList();
        ArrayList listChildGroupData = new ArrayList();
        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_physio_patients, container, false);

            expandbleLis = (ExpandableListView) rootView.findViewById(R.id.expandable_list_view);

            response = getArguments().getString("response");

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
                    expandbleLis.setAdapter(expListAdapter);
                else
                    expandbleLis.setEmptyView(rootView);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return rootView;
        }

    }

}
