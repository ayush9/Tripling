package com.example.geek.zersey;

import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MyActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
  private static final int RC_SIGN_IN = 0;
  private static final String TAG = "Success";
  Boolean isInternetPresent = false;

  /* Client used to interact with Google APIs. */
  private GoogleApiClient mGoogleApiClient;
  /* Is there a ConnectionResult resolution in progress? */
  private boolean mIsResolving = false;

  /* Should we automatically resolve ConnectionResults when possible? */
  private boolean mShouldResolve = false;
  private Bundle bundle;
  private SignInButton btnSignIn;
  private TextView username;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//        checkC();
//        gpsCheck();
    setContentView(R.layout.activity_my);
    btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
    btnSignIn.setOnClickListener(this);
    mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Plus.API)
            .addScope(new Scope(Scopes.PROFILE))
            .build();
  }

  protected void onStart() {
    super.onStart();
    mGoogleApiClient.connect();
  }

  protected void onStop() {
    super.onStop();
    if (mGoogleApiClient.isConnected()) {
      mGoogleApiClient.disconnect();
    }
  }
  protected void onDestroy()
  {
    super.onDestroy();
  }
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == RC_SIGN_IN) {
      // If the error resolution was not successful we should not resolve further.
      if (resultCode != RESULT_OK) {
        mShouldResolve = false;
      }

      mIsResolving = false;
      mGoogleApiClient.connect();
    }
  }

  @Override
  public void onSaveInstanceState(Bundle savedState) {
    super.onSaveInstanceState(savedState);
  }
  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {

    Log.d(TAG, "onConnectionFailed:" + connectionResult);

    if (!mIsResolving && mShouldResolve) {
      if (connectionResult.hasResolution()) {
        try {
          connectionResult.startResolutionForResult(this, RC_SIGN_IN);
          mIsResolving = true;
        } catch (IntentSender.SendIntentException e) {
          Log.e(TAG, "Could not resolve ConnectionResult.", e);
          mIsResolving = false;
          mGoogleApiClient.connect();
        }
      } else {
        // Could not resolve the connection result, show the user an
        // error dialog.
        showErrorDialog(connectionResult);
      }
    }
  }
  private void showErrorDialog(ConnectionResult connectionResult) {
  }
  @Override
  public void onClick(View v) {
    // ...
    if (v.getId() == R.id.btn_sign_in) {
      onSignInClicked();
    }


  }
  private void onSignInClicked() {
    // User clicked the sign-in button, so begin the sign-in process and automatically
    // attempt to resolve any errors that occur.
    mShouldResolve = true;
    mGoogleApiClient.connect();

    // Show a message to the user that we are signing in.
    //mStatusTextView.setText(R.string.signing_in);
  }
  @Override
  public void onConnected(Bundle bundle) {
    // onConnected indicates that an account was selected on the device, that the selected
    // account has granted any requested permissions to our app and that we were able to
    // establish a service connection to Google Play services.
    Log.d(TAG, "onConnected:" + bundle);
    mShouldResolve = false;

    // Show the signed-in UI
    showSignedInUI();
  }
  private void showSignedInUI() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
  }

  @Override
  public void onConnectionSuspended(int i) {

  }

}
