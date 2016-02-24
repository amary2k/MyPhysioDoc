package com.example.PhysiotherapistApp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PhysiotherapistApp.Model.Profile;
import com.google.zxing.integration.android.IntentIntegrator;


public class MainActivity extends ActionBarActivity {

    public static Profile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            // Physiotherapist Menu
            final String mainMenuItems[] = {
                    "My Patients",
                    "Add Patient",
                    "Manage Videos",
                    "Messenger",
                    "Logout"

            };
            ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    R.layout.list_item_layout,
                    R.id.list_item_menu_item,
                    mainMenuItems);

            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ListView myListView = (ListView) rootView.findViewById(R.id.listView_menu);
            myListView.setAdapter(myArrayAdapter);
            //final PlaceholderFragment currentView = this;
            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String itemText = (String) ((TextView) view).getText();

                    if(mainMenuItems[0].equalsIgnoreCase(itemText)) {
                        Intent i = new Intent(view.getContext(), PhysioPatientsActivity.class);
                        startActivity(i);
                    }
                    // Redirect to BarCode Scanner App
                    else if(mainMenuItems[1].equalsIgnoreCase(itemText)) {
                        IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
                        scanIntegrator.initiateScan();
                    }
                    // Redirect to a website
                    else if(mainMenuItems[2].equalsIgnoreCase(itemText))
                    {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                        startActivity(i);
                    }
                    else if(mainMenuItems[3].equalsIgnoreCase(itemText)) {
                        Intent i = new Intent(view.getContext(), StatisticsActivity.class);
                        startActivity(i);
                    }
                    else if(mainMenuItems[4].equalsIgnoreCase(itemText)) {
                        Intent i = new Intent(view.getContext(), AddPatientActivity.class);
                        startActivity(i);
                    }
                }

                public void onActivityResult(int requestCode, int resultCode, Intent intent) {
                    if (requestCode == 0) {
                        if (resultCode == RESULT_OK) {
                            String contents = intent.getStringExtra("SCAN_RESULT");
                            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                            // Handle successful scan
                            Toast myToast = new Toast(getActivity());
                            myToast.setText("contents:" + contents + "|format:" + format);
                            myToast.show();
                        } else if (resultCode == RESULT_CANCELED) {
                            // Handle cancel
                        }
                    }
                };
            });

            return rootView;
        }
    }*/
}
