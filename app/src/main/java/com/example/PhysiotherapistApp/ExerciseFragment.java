package com.example.PhysiotherapistApp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PhysiotherapistApp.Model.Exercise;
import com.example.PhysiotherapistApp.Model.Patient;
import com.example.PhysiotherapistApp.Model.Physiotherapist;
import com.example.PhysiotherapistApp.Model.Schedule;
import com.example.PhysiotherapistApp.Network.DefaultRestClient;
import com.example.PhysiotherapistApp.Network.RestClient;
import com.example.PhysiotherapistApp.Utility.Utility;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Amar on 2016-02-25.
 */
public class ExerciseFragment extends Fragment {

        private View mProgressView;
        private View mButtonView;
        private Patient patient;
        //private Collection<Exercise> exerciseList = new ArrayList<>();
        private CalendarView calendarView;
        private View rootView;
        private  LayoutInflater finalInflater;
        int calDayOfMonth;
        int calMonth;
        int calYear;
        Calendar calDate;

        String response;
        ExpandableListView expandbleLis;
        SimpleExpandableListAdapter expListAdapter;
        ArrayList listGroupData = new ArrayList();
        ArrayList listChildGroupData = new ArrayList();
        public ExerciseFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_exersice, container, false);

            finalInflater = inflater;
            mButtonView = rootView.findViewById(R.id.addExerciseButton);
            mProgressView = getActivity().findViewById(R.id.progressBar);

            expandbleLis = (ExpandableListView) rootView.findViewById(R.id.exercise_expandableListView);

            response = getArguments().getString("response");
            String patientName = getArguments().getString("patientName");



                /*physio = new JSONObject(response);
                JSONArray jsonArray = (JSONArray) physio.get("patients");
                JSONObject patientJSON;
                for (int i=0; i<jsonArray.length(); i++) {
                    if(patientName.equals(((JSONObject) jsonArray.get(i)).getString("name")))
                    {
                        patientJSON = ((JSONObject) jsonArray.get(i));
                    }
                }*/
            Gson gson = new Gson();
            Physiotherapist physio = gson.fromJson(response, Physiotherapist.class);
            patient = null;
            //Iterator<Patient> i = physio.getPatients().iterator().;
            for(Patient patientElement:physio.getPatients()){
                if(patientElement.getName().equals(patientName)){
                    patient = patientElement;
                    break;
                }
                else return rootView; // TODO: instead of return show message patitnt has no Exercises at all
            }



            calendarView = (CalendarView) rootView.findViewById(R.id.calendarView);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month,
                                                int dayOfMonth) {
                    calDate = new GregorianCalendar( year, month, dayOfMonth );
                    calDayOfMonth = dayOfMonth;
                    calMonth = month;
                    calYear = year;
                    reloadExercises();
                }
            });

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(calendarView.getDate()));
            calDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            calMonth = cal.get(Calendar.MONTH);
            calYear = cal.get(Calendar.YEAR);
            calDate = new GregorianCalendar( calYear, calMonth, calDayOfMonth );

            Button addButton = (Button) rootView.findViewById(R.id.addExerciseButton);

            addButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Show Progress Bar
                    Utility.showProgress(mProgressView, mButtonView, getActivity().getBaseContext(), true);
                    DefaultRestClient profiletRestClient = new DefaultRestClient(RestClient.GET, getContext().getString(R.string.rest_client_uri_exercise), v, null, RestClient.APPLICATION_JSON, getContext()) {
                        @Override
                        protected void onPostExecute(String s) {
                            // Hide Progress Bar

                            Utility.showProgress(mProgressView, mButtonView, getActivity().getBaseContext(), false);
                            Intent i = new Intent(getContext(), ExerciseListActivity.class);
                            Bundle b = new Bundle();
                            b.putString("response", s); //Passing response to new acitivity
                            i.putExtras(b);
                            startActivityForResult(i, 1);
                        }
                    };
                    profiletRestClient.execute();
                }
            });

            Button saveButton = (Button) rootView.findViewById(R.id.saveExerciseSchedule);
            saveButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Utility.showProgress(mProgressView, mButtonView, getActivity().getBaseContext(), true);
                    DefaultRestClient profiletRestClient = new DefaultRestClient(RestClient.PUT, getContext().getString(R.string.rest_client_uri_patient), v, patient.getJSON(), RestClient.APPLICATION_JSON, getContext()) {
                        @Override
                        protected void onPostExecute(String s) {
                            // Hide Progress Bar
                            Utility.showProgress(mProgressView, mButtonView, getActivity().getBaseContext(), false);
                            if(s == null || s.length() < 1)
                                Toast.makeText(getActivity().getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity().getApplicationContext(), "Schedule Saved", Toast.LENGTH_SHORT).show();
                        }
                    };
                    profiletRestClient.execute();
                }
            });

            reloadExercises();
            return rootView;
        }

    private void reloadExercises(){

        expandbleLis.setAdapter( new SimpleExpandableListAdapter(
                rootView.getContext(),
                new ArrayList<Map<String, ?>>(),
                R.layout.imagetextimage_group_row,
                new String[] {},            // the name of the field data
                new int[] { R.id.exerciseRowName }, // the text field to populate with the field data
                new ArrayList<List<? extends Map<String, ?>>>(),
                0,
                null,
                new int[] {}));

        final Collection<Schedule> schedules = patient.getSchedule();
        //Toast.makeText(getApplicationContext(), "" + calDayOfMonth, 0).show();// TODO Auto-generated method stub
        Iterator<Schedule> i = schedules.iterator();
        while(i.hasNext())
        {
            Calendar cal = Calendar.getInstance();
            Schedule schedule = i.next();
            cal.setTime(schedule.getExercise_date());
            int exerciseDay = cal.get(Calendar.DAY_OF_MONTH);
            int exerciseMonth = cal.get(Calendar.MONTH);
            int exerciseYear = cal.get(Calendar.YEAR);

            if(exerciseDay == calDayOfMonth && exerciseMonth == calMonth && exerciseYear == calYear)
            {
                Collection<Exercise> exerciseList = schedule.getExercises();
                final String NAME = "exerciseRowName";
                final String IMAGE = "exerciseIcon";
                final LayoutInflater layoutInflater = finalInflater;

                ArrayList<HashMap<String, Object>> groupData = new ArrayList<HashMap<String, Object>>();
                ArrayList<ArrayList<HashMap<String, String>>> childData = new ArrayList<ArrayList<HashMap<String, String>>>();

                for(Exercise exercise:exerciseList) {
                    // Add Group
                    final HashMap<String, Object> group = new HashMap<String, Object>();
                    byte[] b = exercise.getImage();
                    // TODO: Change to name with backend changes
                    group.put(NAME, exercise.getDescription());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        group.put(IMAGE, new BitmapDrawable(BitmapFactory.decodeByteArray(b, 0, b.length)));
                    }
                    groupData.add(group);

                    // Add Respective Child
                    final ArrayList<HashMap<String, String>> group1data = new ArrayList<HashMap<String, String>>();
                    final HashMap<String, String> child = new HashMap<String, String>();
                    child.put(NAME, exercise.getDescription());
                    group1data.add(child);
                    childData.add(group1data);
                }


                expandbleLis.setAdapter(new SimpleExpandableListAdapter(
                                                rootView.getContext(),
                                                groupData,                         // Data for Group
                                                R.layout.imagetextimage_group_row,        // Layout of Group
                                                new String[]{NAME},                 // the name of the field data
                                                new int[]{R.id.exerciseRowName},   // the text field to populate with the field data
                                                childData,                          // Data for child
                                                R.layout.imagetextimage_child_row,        // Layout of Child
                                                new String[]{NAME},                 // the name of the child field data
                                                new int[]{R.id.exerciseDescription} // the text field to populate with the child field data
                                        ) {
                                            @Override
                                            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                                                final View v = super.getGroupView(groupPosition, isExpanded, convertView, parent);

                                                // Populate your custom view here
                                                ((TextView) v.findViewById(R.id.exerciseRowName)).setText((String) ((Map<String, Object>) getGroup(groupPosition)).get(NAME));
                                                ((ImageView) v.findViewById(R.id.exerciseIcon)).setImageDrawable((Drawable) ((Map<String, Object>) getGroup(groupPosition)).get(IMAGE));

                                                return v;
                                            }

                                            @Override
                                            public View newGroupView(boolean isExpanded, ViewGroup parent) {
                                                return layoutInflater.inflate(R.layout.imagetextimage_group_row, null, false);
                                            }

                                        /*@Override
                                        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                                            final View v = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);

                                            // Populate your custom view here
                                            ((TextView) v.findViewById(R.id.exerciseRowName)).setText((String) ((Map<String, Object>) getChild(groupPosition, childPosition)).get(NAME));
                                            ((ImageView) v.findViewById(R.id.exerciseIcon)).setImageDrawable((Drawable) ((Map<String, Object>) getChild(groupPosition, childPosition)).get(IMAGE));

                                            return v;
                                        }

                                        @Override
                                        public View newChildView(boolean isLastChild, ViewGroup parent) {
                                            return layoutInflater.inflate(R.layout.imagetextimage_group_row, null, false);
                                        }*/
                                        }
                );
                break;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Exercise exercise = (Exercise) data.getSerializableExtra("result");
                for(Schedule schedule :patient.getSchedule())
                {
                    if(calDate.getTime().getTime() == schedule.getExercise_date().getTime())
                    {
                        schedule.getExercises().add(exercise);
                        reloadExercises();
                        return;
                    }
                }
                Collection<Exercise> exerciseList = new ArrayList<Exercise>();
                exerciseList.add(exercise);
                patient.getSchedule().add(
                        new Schedule(exerciseList,new Date(calDate.getTime().getTime())));
                reloadExercises();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity().getApplicationContext(), "No exercises were added", Toast.LENGTH_SHORT).show();
            }
        }
    }//onActivityResult

}