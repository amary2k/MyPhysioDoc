package com.example.PhysiotherapistApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PhysiotherapistApp.Model.Exercise;
import com.example.PhysiotherapistApp.Model.Patient;
import com.example.PhysiotherapistApp.Model.Physiotherapist;
import com.example.PhysiotherapistApp.Model.Schedule;
import com.example.PhysiotherapistApp.Model.UserState;
import com.example.PhysiotherapistApp.Network.DefaultRestClient;
import com.example.PhysiotherapistApp.Network.RestClient;
import com.example.PhysiotherapistApp.Utility.Utility;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        private ProgressDialog pDialog;
        //private View mProgressView;
        //private View mButtonView;
        private Patient patient;
        //private Collection<Exercise> exerciseList = new ArrayList<>();
        private CalendarView calendarView;
        private View rootView;
        private  LayoutInflater finalInflater;
        int calDayOfMonth;
        int calMonth;
        int calYear;
        Calendar calDate;
        Date selectedDate;

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
            calendarView = (CalendarView) rootView.findViewById(R.id.calendarView);
            selectedDate = new Date(calendarView.getDate());

            finalInflater = inflater;
            //mButtonView = rootView.findViewById(R.id.addExerciseButton);
            //mProgressView = getActivity().findViewById(R.id.progressBar);

            expandbleLis = (ExpandableListView) rootView.findViewById(R.id.exercise_expandableListView);

            //response = RestClient.response;
            response = RestClient.getResponse(this.getActivity());

            //response = getArguments().getString("response");
            if(UserState.isPhysio()) {
                String patientName = getArguments().getString("patientName");
                String strSelectedDate = getArguments().getString("date");
                if(strSelectedDate != null && strSelectedDate.length() > 0) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                    try {
                        selectedDate = formatter.parse(strSelectedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }


                getActivity().setTitle(patientName.substring(0, 1).toUpperCase() + patientName.substring(1));

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
                for (Patient patientElement : physio.getPatients()) {
                    if (patientElement.getName().equals(patientName)) {
                        patient = patientElement;
                        break;
                    } else {
                    }// TODO: instead of return show message patitnt has no Exercises at all
                }
            }
            else
            {
                Gson gson = new Gson();
                patient = gson.fromJson(response, Patient.class);
            }



            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month,
                                                int dayOfMonth) {
                    calDate = new GregorianCalendar(year, month, dayOfMonth);
                    calDayOfMonth = dayOfMonth;
                    calMonth = month;
                    calYear = year;
                    reloadExercises();
                }
            });

            Calendar cal = Calendar.getInstance();
            cal.setTime(selectedDate);
            calendarView.setDate(selectedDate.getTime());
            calDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            calMonth = cal.get(Calendar.MONTH);
            calYear = cal.get(Calendar.YEAR);
            calDate = new GregorianCalendar( calYear, calMonth, calDayOfMonth );

            Button addButton = (Button) rootView.findViewById(R.id.addExerciseButton);

            addButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Show Progress Bar
                    //Utility.showProgress(mProgressView, mButtonView, getActivity().getBaseContext(), true);
                    pDialog = Utility.showTranslucentProgressDialog(getActivity());
                    DefaultRestClient profiletRestClient = new DefaultRestClient(RestClient.GET, getContext().getString(R.string.rest_client_uri_exercise), null, RestClient.APPLICATION_JSON, getContext()) {
                        @Override
                        protected void onPostExecute(String s) {
                            // Hide Progress Bar
                            pDialog.dismiss();
                            //Utility.showProgress(mProgressView, mButtonView, getActivity().getBaseContext(), false);
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

                    //Utility.showProgress(mProgressView, mButtonView, getActivity().getBaseContext(), true);
                    pDialog = Utility.showTranslucentProgressDialog(getActivity());
                    DefaultRestClient profiletRestClient = new DefaultRestClient(RestClient.PUT, getContext().getString(R.string.rest_client_uri_patient), patient.getJSON(), RestClient.APPLICATION_JSON, getContext()) {
                        @Override
                        protected void onPostExecute(String s) {
                            // Hide Progress Bar
                            pDialog.dismiss();
                            //Utility.showProgress(mProgressView, mButtonView, getActivity().getBaseContext(), false);
                            if (s == null || s.length() < 1)
                                Toast.makeText(getActivity().getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity().getApplicationContext(), "Schedule Saved", Toast.LENGTH_SHORT).show();
                        }
                    };
                    profiletRestClient.execute();
                }
            });


            if(!UserState.isPhysio())
                rootView.findViewById(R.id.addExerciseButton).setVisibility(View.GONE);

            /*expandbleLis.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    final Collection<Schedule> schedules = patient.getSchedule();
                    Iterator<Schedule> i = schedules.iterator();
                    while (i.hasNext()) {
                        Calendar cal = Calendar.getInstance();
                        Schedule schedule = i.next();
                        cal.setTime(schedule.getExercise_date());
                        int exerciseDay = cal.get(Calendar.DAY_OF_MONTH);
                        int exerciseMonth = cal.get(Calendar.MONTH);
                        int exerciseYear = cal.get(Calendar.YEAR);

                        if (exerciseDay == calDayOfMonth && exerciseMonth == calMonth && exerciseYear == calYear) {
                            int count = 0;
                            for (Exercise exercise : schedule.getExercises()) {
                                if (groupPosition == count) {
                                    if(exercise.getVideoLink() == null || exercise.getVideoLink().length() == 0) {
                                        Toast.makeText(getActivity().getApplicationContext(), "No Videos for this Exercise", Toast.LENGTH_LONG).show();
                                        return false;
                                    }else {
                                        String uri = getContext().getString(R.string.rest_client_uri) + getContext().getString(R.string.rest_client_uri_exercise_videos) + "/" + exercise.getVideoLink();
                                        Intent newIntent = new Intent(getContext(), VideosActivity.class);
                                        newIntent.putExtra("videoURI", uri);
                                        startActivity(newIntent);
                                        return true;
                                    }

                                } else
                                    count++;
                            }
                        }
                    }
                    return false;

                }

            });*/

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
                    // TODO: Change to name with backend changes
                    group.put(NAME, exercise.getExercisePK().getTitle());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        group.put(IMAGE, getResources().getDrawable(getResources().getIdentifier(exercise.getImageName(), "drawable", getActivity().getPackageName()), null));
                    }
                    group.put("isComplete", exercise.getExercisePK().isComplete());

                    groupData.add(group);

                    // Add Respective Child
                    final ArrayList<HashMap<String, String>> group1data = new ArrayList<HashMap<String, String>>();
                    final HashMap<String, String> child = new HashMap<String, String>();
                    child.put(NAME, exercise.getDescription());
                    String strPainLvl = exercise.getExercisePK().getPainLevel()==0? "":String.valueOf(exercise.getExercisePK().getPainLevel());
                    child.put("PainLvl", strPainLvl);
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
                                            public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                                                final View v = super.getGroupView(groupPosition, isExpanded, convertView, parent);

                                                // Populate your custom view here
                                                ((TextView) v.findViewById(R.id.exerciseRowName)).setText((String) ((Map<String, Object>) getGroup(groupPosition)).get(NAME));
                                                // need to overlay
                                                ((ImageView) v.findViewById(R.id.exerciseIcon)).setImageDrawable((Drawable) ((Map<String, Object>) getGroup(groupPosition)).get(IMAGE));

                                                // For CheckMark
                                                if((boolean) ((Map<String, Object>) getGroup(groupPosition)).get("isComplete"))
                                                     v.findViewById(R.id.exerciseCheckMark).setAlpha(1.00f);
                                                else
                                                    v.findViewById(R.id.exerciseCheckMark).setAlpha(0.00f);


                                                ImageButton imageButton = (ImageButton) v.findViewById(R.id.exerciseDelButton);
                                                if (UserState.isPhysio())
                                                {
                                                    imageButton.setOnClickListener(new ImageButton.OnClickListener() {

                                                        @Override
                                                        public void onClick(View v) {
                                                            final Collection<Schedule> schedules = patient.getSchedule();
                                                            //Toast.makeText(getApplicationContext(), "" + calDayOfMonth, 0).show();// TODO Auto-generated method stub
                                                            Iterator<Schedule> i = schedules.iterator();
                                                            while (i.hasNext()) {
                                                                Calendar cal = Calendar.getInstance();
                                                                Schedule schedule = i.next();
                                                                cal.setTime(schedule.getExercise_date());
                                                                int exerciseDay = cal.get(Calendar.DAY_OF_MONTH);
                                                                int exerciseMonth = cal.get(Calendar.MONTH);
                                                                int exerciseYear = cal.get(Calendar.YEAR);

                                                                if (exerciseDay == calDayOfMonth && exerciseMonth == calMonth && exerciseYear == calYear) {
                                                                    int count = 0;
                                                                    for (Exercise exercise : schedule.getExercises()) {
                                                                        if (groupPosition == count) {
                                                                            schedule.getExercises().remove(exercise);
                                                                            break;
                                                                        } else
                                                                            count++;
                                                                    }
                                                                    reloadExercises();
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                                else
                                                    imageButton.setVisibility(View.GONE);

                                                v.findViewById(R.id.exerciseCheckMark).setOnClickListener(new ImageButton.OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        if(UserState.isPhysio())
                                                        {
                                                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    checkExerciseAsCompleteHandler(groupPosition);
                                                                    /*switch (which){
                                                                        case DialogInterface.BUTTON_POSITIVE:
                                                                            checkExerciseAsCompleteHandler(groupPosition);
                                                                            break;

                                                                        case DialogInterface.BUTTON_NEGATIVE:
                                                                            //No button clicked
                                                                            return;
                                                                    }*/
                                                                }
                                                            };

                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                            builder.setMessage("Mark exercise as completed?").setPositiveButton("Yes", dialogClickListener)
                                                                    .setNegativeButton("No", dialogClickListener).show();
                                                        }
                                                        else
                                                            checkExerciseAsCompleteHandler(groupPosition);
                                                    }
                                                });
                                                return v;
                                            }

                                            @Override
                                            public View newGroupView(boolean isExpanded, ViewGroup parent) {
                                                return layoutInflater.inflate(R.layout.imagetextimage_group_row, null, false);
                                            }

                                           public void checkExerciseAsCompleteHandler(int groupPosition){
                                               final Collection<Schedule> schedules = patient.getSchedule();
                                               //Toast.makeText(getApplicationContext(), "" + calDayOfMonth, 0).show();// TODO Auto-generated method stub
                                               final Iterator<Schedule> i = schedules.iterator();
                                               while (i.hasNext()) {
                                                   Calendar cal = Calendar.getInstance();
                                                   Schedule schedule = i.next();
                                                   cal.setTime(schedule.getExercise_date());
                                                   int exerciseDay = cal.get(Calendar.DAY_OF_MONTH);
                                                   int exerciseMonth = cal.get(Calendar.MONTH);
                                                   int exerciseYear = cal.get(Calendar.YEAR);

                                                   if (exerciseDay == calDayOfMonth && exerciseMonth == calMonth && exerciseYear == calYear) {
                                                       int count = 0;
                                                       for (final Exercise exercise : schedule.getExercises()) {
                                                           if (groupPosition == count) {

                                                               boolean isComplete = exercise.getExercisePK().isComplete();
                                                               if(!isComplete) {
                                                                   AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                                                                   final CharSequence items[] = new CharSequence[]{"1", "2", "3", "4", "5"};
                                                                   adb.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

                                                                       @Override
                                                                       public void onClick(DialogInterface d, int n) {
                                                                           exercise.getExercisePK().setPainLevel(Integer.parseInt(items[n].toString()));
                                                                           reloadExercises();
                                                                           d.dismiss();
                                                                       }

                                                                   });
                                                                   adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(DialogInterface dialog, int which) {
                                                                           dialog.cancel();
                                                                       }
                                                                   });
                                                                   adb.setTitle("Rate the pain from 1 (low) - 5 (high)");
                                                                   adb.show();
                                                               }
                                                               else
                                                                   exercise.getExercisePK().setPainLevel(0);
                                                               exercise.getExercisePK().setComplete(!isComplete);
                                                               break;
                                                           } else
                                                               count++;
                                                       }
                                                       reloadExercises();
                                                   }
                                               }
                                           }
                                        @Override
                                        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                                            final View v = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);

                                            // Populate your custom view here
                                            ((TextView) v.findViewById(R.id.exerciseDescription)).setText((String) ((Map<String, Object>) getChild(groupPosition, childPosition)).get(NAME));
                                            ((TextView) v.findViewById(R.id.exercisePainLvl)).setText((String) ((Map<String, Object>) getChild(groupPosition, childPosition)).get("PainLvl"));

                                            v.findViewById(R.id.exercisePlayVideoBtn).setOnClickListener(new ImageButton.OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                    final Collection<Schedule> schedules = patient.getSchedule();
                                                    //Toast.makeText(getApplicationContext(), "" + calDayOfMonth, 0).show();// TODO Auto-generated method stub
                                                    Iterator<Schedule> i = schedules.iterator();
                                                    while (i.hasNext()) {
                                                        Calendar cal = Calendar.getInstance();
                                                        Schedule schedule = i.next();
                                                        cal.setTime(schedule.getExercise_date());
                                                        int exerciseDay = cal.get(Calendar.DAY_OF_MONTH);
                                                        int exerciseMonth = cal.get(Calendar.MONTH);
                                                        int exerciseYear = cal.get(Calendar.YEAR);

                                                        if (exerciseDay == calDayOfMonth && exerciseMonth == calMonth && exerciseYear == calYear) {
                                                            int count = 0;
                                                            for (Exercise exercise : schedule.getExercises()) {
                                                                if (groupPosition == count) {
                                                                    if (exercise.getVideoLink() == null || exercise.getVideoLink().length() == 0) {
                                                                        Toast.makeText(getActivity().getApplicationContext(), "No Videos for this Exercise", Toast.LENGTH_LONG).show();
                                                                    } else {
                                                                        String uri = getContext().getString(R.string.rest_client_uri) + getContext().getString(R.string.rest_client_uri_exercise_videos) + "/" + exercise.getVideoLink();
                                                                        Intent newIntent = new Intent(getContext(), VideosActivity.class);
                                                                        newIntent.putExtra("videoURI", uri);
                                                                        startActivity(newIntent);
                                                                    }
                                                                    break;
                                                                } else
                                                                    count++;
                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                            return v;
                                        }

                                        @Override
                                        public View newChildView(boolean isLastChild, ViewGroup parent) {
                                            return layoutInflater.inflate(R.layout.imagetextimage_child_row, null, false);
                                        }
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
                        for(Exercise existingExercise : schedule.getExercises()){
                            // TODO: in future need to have a check for proper id
                            if(existingExercise.getDescription().equals(exercise.getDescription())) {
                                Toast.makeText(getActivity().getApplicationContext(), "Exercise already exists!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

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
