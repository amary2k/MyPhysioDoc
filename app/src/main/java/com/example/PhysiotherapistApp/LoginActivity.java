package com.example.PhysiotherapistApp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/*
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
*/

import com.example.PhysiotherapistApp.GCM.QuickstartPreferences;
import com.example.PhysiotherapistApp.GCM.RegistrationIntentService;
import com.example.PhysiotherapistApp.Model.Profile;
import com.example.PhysiotherapistApp.Model.UserState;
import com.example.PhysiotherapistApp.Network.RestClient;
import com.example.PhysiotherapistApp.Utility.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // GCM Related
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    //private ProgressBar mRegistrationProgressBar;



    private UserLoginTask mAuthTask = null;

    private ProgressDialog pDialog;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    //private View mProgressView;
    //private View mLoginFormView;
    private Intent mainMenu;

    public static Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mainMenu = new Intent(this,MainActivity.class);
        UserState.setPhoneRegisterationId("");



        pDialog = Utility.showTranslucentProgressDialog(LoginActivity.this);
        //mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    //Toast.makeText(getApplicationContext(), R.string.gcm_send_message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.token_error_message, Toast.LENGTH_SHORT).show();
                }

                // Set up the login form.
                mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
                populateAutoComplete();

                mPasswordView = (EditText) findViewById(R.id.password);
                mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                        if (id == R.id.login || id == EditorInfo.IME_NULL) {
                            attemptLogin();
                            return true;
                        }
                        return false;
                    }
                });

                Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
                mEmailSignInButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attemptLogin();
                    }
                });
                pDialog.dismiss();
            }
        };

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        if(UserState.getPhoneRegisterationId().length() == 0) {
            Log.v(LoginActivity.class.getName(),"Waiting for token to be received from GCM");
            return;
        }
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            // Temporarily unchecked
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            pDialog = Utility.showTranslucentProgressDialog(LoginActivity.this);
            //Utility.showProgress(mProgressView, mLoginFormView, getBaseContext(), true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute();
            //mAuthTask.onPostExecute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(this.getClass().getName(), "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String response = null;
            HashMap<String,String> userNamePass = new HashMap<String,String>();
            userNamePass.put("username",mEmail);
            userNamePass.put("password",mPassword);
            userNamePass.put("phoneRegId",UserState.getPhoneRegisterationId());
            RestClient restClient = new RestClient(RestClient.POST,getString(R.string.rest_client_uri_login),new HashMap<String,String>(),new HashMap<String,String>(), userNamePass, getBaseContext());

            try {
                // Simulate network access.
                //Thread.sleep(2000);
                response = restClient.callRESTAPI();
            } catch (Exception e) {
                Log.e("My App", "Login Attempt Failed");
                return false;
            }

            JSONObject responseJSON = null;
            String strAuthToken = null;
            Boolean isPhysio = false;
            try {

                responseJSON = new JSONObject(response);
                strAuthToken = (String) responseJSON.get("auth_token");
                isPhysio = (boolean) responseJSON.get("is_physio");
                Log.d("My App", response.toString());

            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                return false;
            }

            if(responseJSON == null || strAuthToken == null || strAuthToken.length() < 1 )
                return false;


            UserState.setAuthToken(strAuthToken);
            UserState.setIsPhysio(isPhysio);
            UserState.setUserName(mEmail);

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            pDialog.dismiss();
            //Utility.showProgress(mProgressView, mLoginFormView, getBaseContext(), false);

            if (success) {

                startActivity(mainMenu);
               //finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            pDialog.dismiss();
            //Utility.showProgress(mProgressView, mLoginFormView, getBaseContext(), false);
        }
    }
}



