package edu.orangecoastcollege.cs272.taskr.controller;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.AsyncTask;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.calendar.model.Event;

import java.io.IOException;

import edu.orangecoastcollege.cs272.taskr.R;
import edu.orangecoastcollege.cs272.taskr.view.manager.ViewAllProjectsActivity;
import edu.orangecoastcollege.cs272.taskr.view.scheduler.SchedulerHome;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * <code>taskR</code> is an application that houses three features
 * scheduleR allows the user to quickly add events to their Google Calendar from user-made templates
 * manageR allows the user to create deadlines and ...
 * remindeR allows the user to create task lists and ...
 *
 * @author Vincent Hoang
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    /* DECLARATIONS */

    // Request Codes
    private static final int RC_SIGN_IN = 1000;
    static final int REQUEST_ACCOUNT_PICKER = 1001;
    static final int REQUEST_AUTHORIZATION = 1002;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1003;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1004;

    // Google Login
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String TAG = "GoogleLoginActivity";
    private GoogleApiClient mGoogleApiClient;
    private GoogleAccountCredential mCredential;
    private TextView mLoginStatus;
    private LinearLayout mLoginBar;

    // Calendar insertion
    Event mEvent;

    /**
     * <code>onCreate()</code>
     * Initializes the code below upon start up
     *  - Assigns button listeners to the buttons defined in the XML layout
     *  - Assigns a reference id to the TextViews and LinearLayouts as defined in the XML
     *  - Creates a series of tokens to authenticate with Google Play Services, including
     *      requesting permission from Google Calendar API
     *  - Checks if the user has already logged into Google Play Services prior and updates
     *      the UI if logged in is true
     *
     * @param savedInstanceState Prior state if returning from another activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Button Listeners */
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.ma_vincent_scheduler_button).setOnClickListener(this);
        findViewById(R.id.ma_derek_manager_button).setOnClickListener(this);


        /* //// [BEGIN] GOOGLE LOGIN //// */
        mLoginStatus = (TextView) findViewById(R.id.ma_vincent_login_text);
        mLoginBar = (LinearLayout) findViewById(R.id.ma_vincent_login_bar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/calendar"))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


        // Checks to see if the user has already logged in before
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone() && mCredential != null)
        {
            Log.d("TAG", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }

        /* //// [END] GOOGLE LOGIN //// */

    }

    @Override
    public void onClick(View v) {
        Intent changeActivity;
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.ma_vincent_scheduler_button:
                changeActivity = new Intent(this, SchedulerHome.class);
                startActivity(changeActivity);
                break;
            case R.id.ma_derek_manager_button:
                changeActivity = new Intent(this, ViewAllProjectsActivity.class);
                startActivity(changeActivity);
                break;
        }
    }

    /* Google Login Methods */

    /**
     * Logs the result of a failed connection attempt
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // GoogleSignInAccount acct = result.getSignInAccount();
            // Not used in this version. May revisit
            updateUI(true);
        } else {
            updateUI(false);
        }
    }

    /**
     * Updates the View if the user is signed in
     * @param signedIn boolean result from handleSignInResult()
     */
    protected void updateUI(boolean signedIn) {
        if (signedIn) {
            mLoginStatus.setText(R.string.ma_vincent_login_text_true);
            mLoginBar.setBackgroundColor(getResources().getColor(R.color.greenYellow));
            getResultsFromApi();
            //findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            //findViewById(R.id.sign_out).setVisibility(View.VISIBLE);
        } else {

            mLoginBar.setBackgroundColor(getResources().getColor(R.color.tomato));
            mLoginStatus.setText(R.string.ma_vincent_login_text_false);
            //findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out).setVisibility(View.GONE);
        }
    }

    /**
     * Signs the user into Google Play Services and returns a result code to be handled
     */
    protected void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential != null && mCredential.getSelectedAccountName() == null ) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            mLoginStatus.setText(R.string.ma_vincent_no_network);
        } else {
            mLoginStatus.setText("Success");
        }
    }

    /**
     * Posts an event to Google Calendar with an asynchronous task
     * @param event The event to be posted to calendar
     */
    public void postEvent(Event event){
        if (event != null) {
            mEvent = event;
            new MakeRequestTask(mCredential).execute();
        }
    }

    /**
     * Handles activity results from other methods in this class
     *
     * @param requestCode The request code sent with the activity
     * @param resultCode The result code returned from the activity
     * @param data Something pertaining to the user account from the account chooser
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    mLoginStatus.setText(R.string.ma_vincent_login_text_false);
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                //mCredential.setSelectedAccountName(accountName);
                //getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }


    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }


    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                MainActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, Boolean> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;
       MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }
       /**
         * Background task to call Google Calendar API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                insertEvent();
                return true;
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }
       private Boolean insertEvent() {
            try {
                    if (mEvent != null) {
                        Event event = mEvent;
                        mService.events().insert("primary", event).execute();
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
           return true;
        }
       @Override
        protected void onPreExecute() {
        }
       @Override
        protected void onPostExecute(Boolean output) {
        }
       @Override
        protected void onCancelled() {
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            MainActivity.REQUEST_AUTHORIZATION);
                } else {
                    mLoginStatus.setText("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                mLoginStatus.setText("Request cancelled.");
            }
        }
    }
}


