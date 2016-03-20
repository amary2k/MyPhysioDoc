package com.example.PhysiotherapistApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;

import com.example.PhysiotherapistApp.Network.DefaultRestClient;
import com.example.PhysiotherapistApp.Network.RestClient;
import com.example.PhysiotherapistApp.Utility.Utility;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ViewPortHandler;
//import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private BarChart mBarChart;
    private PieChart mChart;
    private boolean painLvlRequired = false;
    ProgressDialog pDialog;
    private String patientName;
    //private SeekBar mSeekBarX, mSeekBarY;
    //private TextView tvX, tvY;

    ArrayList<Entry> completedyVals = new ArrayList<Entry>();
    ArrayList<Entry> completedDetailedyVals = new ArrayList<Entry>();

    protected String[] mCompleted = new String[] {
            "Completed", "Pending"
    };

    protected List<String> mCompletedWithPain = new ArrayList<String>();

    private Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_summary);

        mCompletedWithPain.add("Pending");
        mCompletedWithPain.add("No Pain");
        mCompletedWithPain.add("Low Pain");
        mCompletedWithPain.add("Medium Pain");
        mCompletedWithPain.add("High Pain");
        mCompletedWithPain.add("Very High Pain");

        Bundle b = getIntent().getExtras();
        patientName = b.getString("patientName");

       /* tvX = (TextView) findViewById(R.id.tvXMax);
        tvY = (TextView) findViewById(R.id.tvYMax);*/

        /*mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
        mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);

        mSeekBarY.setProgress(10);

        mSeekBarX.setOnSeekBarChangeListener(this);
        mSeekBarY.setOnSeekBarChangeListener(this);*/
        final Switch detailSwitch = (Switch) findViewById(R.id.detailSwitch);
        //set the switch to ON
        detailSwitch.setChecked(false);
        //attach a listener to check for changes in state
        detailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    painLvlRequired = true;
                    setData(completedDetailedyVals);
                }else{
                    painLvlRequired = false;
                    setData(completedyVals);
                }

            }
        });
        String response = RestClient.getResponse(this);
        JSONArray jsonArray = null;


        LinkedHashMap<String, List<Float>> dateWisePain = new LinkedHashMap<>();

        int comletedPainLvl[] = new int[6];
        int isCompleteCount = 0;
        int totalExercises = 0;
        try {
            jsonArray = new JSONArray(response);
            totalExercises = jsonArray.length();
            for(int i=0; i < jsonArray.length(); i++)
            {
                JSONArray innerArray = (JSONArray) jsonArray.get(i);

                Date date = null;
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                try {
                    date = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss aaa").parse(innerArray.getString(3));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String strDate = formatter.format(date);
                if(dateWisePain.containsKey(strDate))
                {
                    List<Float> painList = dateWisePain.get(strDate);
                    painList.set(0, new Float(painList.get(0).floatValue() + (int)innerArray.get(1)));
                    //painList.add(new Float((int) innerArray.get(1)));
                    //Collections.sort(painList);
                }
                else {
                    List<Float> painList = new ArrayList<>();
                    painList.add(new Float((int) innerArray.get(1)));

                    dateWisePain.put(strDate, painList);
                }

                if(innerArray.get(0) == true)
                    isCompleteCount++;
                switch((int)innerArray.get(1)){
                    case 0:
                        comletedPainLvl[0]++;
                        break;
                    case 1:
                        comletedPainLvl[1]++;
                        break;
                    case 2:
                        comletedPainLvl[2]++;
                        break;
                    case 3:
                        comletedPainLvl[3]++;
                        break;
                    case 4:
                        comletedPainLvl[4]++;
                        break;
                    case 5:
                        comletedPainLvl[5]++;
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i=0, j=0; i < comletedPainLvl.length; i++,j++) {
         if(comletedPainLvl[i] == 0)
             mCompletedWithPain.remove(j--);
         else
             completedDetailedyVals.add(new Entry(comletedPainLvl[i], i));
        }

        completedyVals.add(new Entry(isCompleteCount, 0));
        completedyVals.add(new Entry(totalExercises - isCompleteCount, 1));

        /*try {
            JSONObject responseJSON = new JSONObject(response);

        JSONArray jsonArray = (JSONArray) responseJSON.get("patients");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        // Bar Chart ==================================================================
        mBarChart = (BarChart) findViewById(R.id.barchart);
        mBarChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mBarChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mBarChart.setPinchZoom(false);

        mBarChart.setDrawGridBackground(false);
        mBarChart.setDrawBarShadow(false);

        mBarChart.setDrawValueAboveBar(false);

        // change the position of the y-labels
        YAxis leftAxis = mBarChart.getAxisLeft();
        //leftAxis.setValueFormatter(new MyYAxisValueFormatter());
        leftAxis.setAxisMinValue(0); // this replaces setStartAtZero(true)
        mBarChart.getAxisRight().setEnabled(false);

        XAxis xLabels = mBarChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.TOP);

        // mBarChart.setDrawXLabels(false);
        // mBarChart.setDrawYLabels(false);

        // setting data
        /*mSeekBarX.setProgress(12);
        mSeekBarY.setProgress(100);*/

        Legend barChartl = mBarChart.getLegend();
        barChartl.setPosition(LegendPosition.BELOW_CHART_RIGHT);
        barChartl.setFormSize(8f);
        barChartl.setFormToTextSpace(4f);
        barChartl.setXEntrySpace(6f);

        /*List<String> yVals = null;
        if(painLvlRequired)
            yVals = mCompletedWithPain;
        else
            yVals = Arrays.asList(mCompleted);*/

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        /*for (int i = 0; i < mSeekBarX.getProgress() + 1; i++) {
            xVals.add(mMonths[i % mMonths.length]);
        }*/
        int xIndex = 0;
        for(Map.Entry<String, List<Float>> entry:dateWisePain.entrySet()){
            xVals.add(entry.getKey());
            List<Float> floatList = entry.getValue();
            float[] floatArray = new float[entry.getValue().size()];
            int i = 0;
            for (Float f : floatList) {
                floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
            }
            yVals1.add(new BarEntry(floatArray,xIndex));
            xIndex++;
        }



        /*for (int i = 0; i < mSeekBarX.getProgress() + 1; i++) {
            float mult = (mSeekBarY.getProgress() + 1);
            float val1 = (float) (Math.random() * mult) + mult / 3;
            float val2 = (float) (Math.random() * mult) + mult / 3;
            float val3 = (float) (Math.random() * mult) + mult / 3;

            yVals1.add(new BarEntry(new float[] { val1, val2, val3 }, i));
        }*/
        /*for(int i = 0; i < mSeekBarX.getProgress() + 1; i++) {
            yVals1.add(new BarEntry(new float[] { val1, val2, val3 }, i));
        }*/

        BarDataSet set1 = new BarDataSet(yVals1, "Pain Level");
        set1.setColors(getColors());
        set1.setStackLabels(mCompletedWithPain.toArray(new String[mCompletedWithPain.size()]));

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        //data.setValueFormatter(new MyValueFormatter());

        mBarChart.setData(data);
        mBarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(final Entry e, int dataSetIndex, Highlight h) {

                AlertDialog.Builder adb = new AlertDialog.Builder(SummaryActivity.this);
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (e == null)
                            return;

                        BarData set = mBarChart.getBarData();
                        final String date = set.getXVals().get(e.getXIndex());
                        RectF bounds = mBarChart.getBarBounds((BarEntry) e);
                        PointF position = mBarChart.getPosition(e, YAxis.AxisDependency.LEFT);

                        pDialog = Utility.showTranslucentProgressDialog(SummaryActivity.this);
                        DefaultRestClient defaultRestClient = new DefaultRestClient(RestClient.GET, getApplicationContext().getString(R.string.rest_client_uri_physiotherapist), null, RestClient.APPLICATION_JSON, getApplicationContext()) {
                            @Override
                            protected void onPostExecute(String s) {
                                pDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), ExersiceActivity.class);
                                Bundle b = new Bundle();
                                b.putString("patientName", patientName);
                                b.putString("date", date);
                                i.putExtras(b);
                                startActivity(i);
                            }
                        };
                        defaultRestClient.execute();
                    }
                });
                adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                adb.setTitle("View exercise details?");
                adb.show();

            }

            @Override
            public void onNothingSelected() {

            }
        });
        //mBarChart.invalidate();
        // Pie chart ==================================================================
        mChart = (PieChart) findViewById(R.id.chart1);
        //mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        //mChart.setTransparentCircleColor(Color.WHITE);
        //mChart.setTransparentCircleAlpha(110);


        mChart.setHoleRadius(58f);
        //mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        //setData(3, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        if(painLvlRequired)
            setData(completedDetailedyVals);
        else
            setData(completedyVals);
    }

    private ArrayList<Integer> getColors() {
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        return colors;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.pie, menu);
        return true;
    }

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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        /*tvX.setText("" + (mSeekBarX.getProgress() + 1));
        tvY.setText("" + (mSeekBarY.getProgress()));*/

        //setData(mSeekBarX.getProgress(), mSeekBarY.getProgress());
    }

    private void setData(ArrayList<Entry> yVals1) {


        /*ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        yVals1.add(new Entry(isCompleteCount, 0));
        yVals1.add(new Entry(totalExercises - isCompleteCount, 1));*/

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        /*for (int i = 0; i < jsonArray.length(); i++) {

                int no = jsonArray.get(i)==true? 1:0;*/


        //}
        List<String> xVals = null;
        if(painLvlRequired)
            xVals = mCompletedWithPain;
        else
            xVals = Arrays.asList(mCompleted);
        //xVals = Collections.;
        /*for (int i = 0; i < count + 1; i++)
            xVals.add(mCompleted[i % mCompleted.length]);*/

        PieDataSet dataSet = new PieDataSet(yVals1, "Progress Summary");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors



        dataSet.setColors(getColors());
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
       // data.setValueFormatter(new PercentFormatter());
        data.setValueFormatter(new ValueFormatter() {

            private DecimalFormat mFormat = new DecimalFormat("###,###,###");


            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

                if(value == 0) return "";

                return mFormat.format(value);
            }
        });
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        //SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        String title = "Exercises";
        String subHeading = "completed";
        int titleLengthPlusOne = title.length() + 1;
        SpannableString s = new SpannableString(title + "\n" + subHeading);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, title.length(), 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), title.length(), s.length() - titleLengthPlusOne, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), title.length(), s.length() - titleLengthPlusOne, 0);
        s.setSpan(new RelativeSizeSpan(.8f), title.length(), s.length() - titleLengthPlusOne, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - title.length(), s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - title.length(), s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }
}