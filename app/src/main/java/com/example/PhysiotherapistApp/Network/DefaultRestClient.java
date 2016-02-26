package com.example.PhysiotherapistApp.Network;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.example.PhysiotherapistApp.Utility.Utility;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * Created by Amar on 2016-02-25.
 */
public abstract class DefaultRestClient extends AsyncTask<Void,Void,String> {
    RestClient restClient;
    View view;
    // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;



    public DefaultRestClient(String strMethod, String strURL, View view, String body, String contentType, Context context){
        initClass(strMethod, strURL, new HashMap<String,String>(), new HashMap<String,String>(), new HashMap<String,String>(), view, body, contentType, context);
    }


    public void initClass(String strMethod, String strURL, HashMap<String,String> params, HashMap<String,String> headers, HashMap<String,String> bodyParams, View view, String body, String contentType, Context context) {
        this.view = view;
        this.restClient = new RestClient(strMethod,strURL,params,headers, bodyParams, context, body, contentType);
    }

    @Override
    protected abstract void onPostExecute(String s);

    @Override
    protected String doInBackground(Void... params) {
        return restClient.callRESTAPI();
    }
}