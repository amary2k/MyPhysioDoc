package com.example.PhysiotherapistApp.Network;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.PhysiotherapistApp.Model.UserState;
import com.example.PhysiotherapistApp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestClient {
    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String APPLICATION_JSON = "application/json";
    private String strMethod;
    private String strURL;
    private HashMap<String,String> params;
    private HashMap<String,String> headers;
    private HashMap<String,String> bodyParams;
    private String body;
    private String contentType;

    static public String response;

    // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    Context context;
    ArrayList contolList;

    public RestClient(String strMethod, String strURL, HashMap<String,String> params, HashMap<String,String> headers, HashMap<String,String> bodyParams, Context context) {
        initClass(strMethod, strURL, params, headers, bodyParams, context, "", RestClient.APPLICATION_X_WWW_FORM_URLENCODED);
    }

    public RestClient(String strMethod, String strURL, HashMap<String,String> params, HashMap<String,String> headers, HashMap<String,String> bodyParams, Context context, String body, String contentType) {
        initClass(strMethod, strURL, params, headers, bodyParams, context, body, contentType);
    }

    public void initClass(String strMethod, String strURL, HashMap<String,String> params, HashMap<String,String> headers, HashMap<String,String> bodyParams, Context context, String body, String contentType) {
        this.strMethod = strMethod;
        this.strURL = context.getString(R.string.rest_client_uri) + strURL;
        this.params = params;
        this.headers = headers;
        this.context = context;
        this.bodyParams = bodyParams;
        this.body =  body;
        this.contentType = contentType;
        RestClient.response = null;
    }


    public String callRESTAPI() {

        String strResponse;
        int intResponseCode = HttpURLConnection.HTTP_UNAVAILABLE;

        try {

            if(params != null && params.size() > 0) {
                int i = 0;
                for(Map.Entry<String, String> entry : bodyParams.entrySet()) {
                    if(i==0)
                        strURL += "?" + entry.getKey() + "=" + entry.getValue();
                    else
                        strURL += "&" + entry.getKey() + "=" + entry.getValue();
                    i++;
                }
            }
            URL url = new URL(strURL);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(strMethod);
            // Header
            urlConnection.setRequestProperty("service_key", context.getString(R.string.rest_api_key));
            urlConnection.setRequestProperty("auth_token", UserState.getAuthToken());
            if(headers != null && headers.size() > 0)
            {
                for(Map.Entry<String, String> entry : headers.entrySet())
                    urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            if(strMethod.equals(RestClient.POST) || strMethod.equals(RestClient.PUT)) {
                StringBuilder postData = new StringBuilder();
                if(bodyParams != null && bodyParams.size() > 0) {
                    for (Map.Entry<String, String> param : bodyParams.entrySet()) {
                        if (postData.length() != 0) {
                            postData.append('&');
                        }
                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    }
                }
                postData.append(body);
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                urlConnection.setRequestProperty("Content-type", contentType);
                urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(postData.toString());
                writer.flush();
                writer.close();
                os.close();
            }

            /*if(bodyParams != null && bodyParams.size() > 0)
            {
                for(Map.Entry<String, String> entry : bodyParams.entrySet())
                    urlConnection.setRequestProperty(entry.getKey(),entry.getValue());
            }*/
            urlConnection.connect();
            intResponseCode = urlConnection.getResponseCode();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                strResponse = null;
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
                strResponse = null;
            }
            strResponse = buffer.toString();
           Log.v("LOG_TAG","API CALL RESULT:" + strResponse);
        } catch (IOException e) {
            if(intResponseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.v("RestClient", "HTTP_UNAUTHORIZED");
                //Toast.makeText(context, "Unauthorized Access", Toast.LENGTH_LONG).show();
            }
            else if(intResponseCode == HttpURLConnection.HTTP_UNAVAILABLE) {
                Log.v("RestClient", "HTTP_UNAVAILABLE");
               // Toast.makeText(context, "Server or Internet is down", Toast.LENGTH_LONG).show();
            }
            Log.e("RestClient", "Error Response Code:" + intResponseCode , e);
            strResponse = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("RestClient", "Error closing stream", e);
                }
            }
        }
        response = strResponse;
        return strResponse;
        //return null;
    }
   /* private ArrayList <NameValuePair> params;
    private ArrayList <NameValuePair> headers;

    private String url;

    private int responseCode;
    private String message;

    private String response;

    public String getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public RestClient(String url)
    {
        this.url = url;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
    }

    public void AddParam(String name, String value)
    {
        params.add(new BasicNameValuePair(name, value));
    }

    public void AddHeader(String name, String value)
    {
        headers.add(new BasicNameValuePair(name, value));
    }

    public void Execute(RequestMethod method) throws Exception
    {
        switch(method) {
            case GET:
            {
                //add parameters
                String combinedParams = "";
                if(!params.isEmpty()){
                    combinedParams += "?";
                    for(NameValuePair p : params)
                    {
                        String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),”UTF-8″);
                        if(combinedParams.length() > 1)
                        {
                            combinedParams  +=  "&" + paramString;
                        }
                        else
                        {
                            combinedParams += paramString;
                        }
                    }
                }

                HttpGet request = new HttpGet(url + combinedParams);

                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }

                executeRequest(request, url);
                break;
            }
            case POST:
            {
                HttpPost request = new HttpPost(url);

                //add headers
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }

                if(!params.isEmpty()){
                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                }

                executeRequest(request, url);
                break;
            }
        }
    }

    private void executeRequest(HttpUriRequest request, String url)
    {
        HttpClient client = new DefaultHttpClient();

        HttpResponse httpResponse;

        try {
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {

                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);

                // Closing the input stream will trigger connection release
                instream.close();
            }

        } catch (ClientProtocolException e)  {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (IOException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        }
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }*/
}