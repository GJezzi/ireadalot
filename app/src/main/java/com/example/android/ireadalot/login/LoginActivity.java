package com.example.android.ireadalot.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.ireadalot.R;
import com.example.android.ireadalot.activity.BaseActivity;
import com.example.android.ireadalot.activity.MainActivity;
import com.example.android.ireadalot.utils.Constants;
import com.example.android.ireadalot.utils.Utils;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by gjezzi on 08/09/16.
 */
public class LoginActivity extends BaseActivity {

    private static final String LOG_TAG = "LoginActivity";

    private Context mContext;
    private ProgressDialog mProgressDialog;
    private EditText mEditTextEmailInput;
    private EditText mEditTextPasswordInput;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Firebase mFirebaseRef;

    private String mUserEmail;
    private String mUserPassword;
    private String mEncodedEmail;

    private boolean mGoogleIntentProgress;

    public static final int RC_GOOGLE_LOGIN = 1;

    GoogleSignInAccount mGoogleAccount;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseError mFirebaseError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        initializeScreen();

        /**
         * Call signInPassword() when user taps "Done" keyboard action
         */
        mEditTextPasswordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    signInPassword(mUserEmail, mUserPassword);
                }
                return true;
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(LOG_TAG, "onAuthStateChanged:signedIn: " + user.getUid());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void onSignUpPressed(View view) {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    public void onSignInPressed(View view) {
        mUserEmail = mEditTextEmailInput.getText().toString();
        mUserPassword = mEditTextPasswordInput.getText().toString();

//        boolean validEmail = isEmailValid(mUserEmail);
//        boolean networkAvailable = isNetworkAvailable(mContext);
//        boolean validPassword = isPasswordValid(mUserPassword);
//        boolean userDoesNotExist = userDoesNotExist(mUserEmail);


        //if(!validEmail || !networkAvailable || !validPassword || userDoesNotExist) return;

        //onAuthenticationError(mUserEmail, mUserPassword);
        signInPassword(mUserEmail, mUserPassword);
    }

    public void initializeScreen() {
        mEditTextEmailInput = (EditText) findViewById(R.id.edit_text_email);
        mEditTextPasswordInput = (EditText) findViewById(R.id.edit_text_password);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mProgressDialog.setMessage(getString(R.string.progress_dialog_signing_in));
        mProgressDialog.setCancelable(false);

        setUpGoogleSignIn();
    }

    public void signInPassword(String email, String Password) {

        Log.d(LOG_TAG, "signIn: " + email);

        if(!validateForm()) {
            return;
        }

        mProgressDialog.show();
        mFirebaseAuth.signInWithEmailAndPassword(mUserEmail, mUserPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "singInWithEmail:onComplete: " + task.isSuccessful());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mProgressDialog.dismiss();
                    getUserInfo();
                    startActivity(intent);
                    finish();
                }

                if (!task.isSuccessful()) {
                    Log.w(LOG_TAG, "signInWithEmail:failed", task.getException());
                    mProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_GOOGLE_LOGIN){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(googleSignInResult.isSuccess()){
                GoogleSignInAccount account = googleSignInResult.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                if (googleSignInResult.getStatus().getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
                    showErrorToast("The sign in was cancelled. Make sure you'e connected to the internet and try again.");
                } else {
                    showErrorToast("Error Handling the sign in: " + googleSignInResult.getStatus().getStatusMessage());
                }
                mProgressDialog.dismiss();
            }
        }
    }

    public void firebaseAuthWithGoogle(final GoogleSignInAccount account){
        Log.d(LOG_TAG, "firebaseAuthWithGoogle: " + account.getId());
        Log.d(LOG_TAG, "firebaseAuthWithGoogle: " + account.getEmail());
        mProgressDialog.show();

        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "singInWithEmail:onComplete: " + task.isSuccessful());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mProgressDialog.dismiss();
                    getGoogleUserInfo(account);
                    startActivity(intent);
                    finish();
                }

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.w(LOG_TAG, "signInWithCredential", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setUpGoogleSignIn(){
        SignInButton signInButton = (SignInButton) findViewById(R.id.login_with_google);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGoogleSignInPressed(view);
            }
        });
    }

    private void onGoogleSignInPressed (View view) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);

    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEditTextEmailInput.getText().toString();
        if(TextUtils.isEmpty(email)) {
            mEditTextEmailInput.setError("E-mail Required.");
            valid = false;
        } else {
            mEditTextEmailInput.setError(null);
        }

        String password = mEditTextPasswordInput.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mEditTextPasswordInput.setError("Password Required.");
            valid = false;
        } else {
            mEditTextPasswordInput.setError(null);
        }
        return valid;
    }

    private boolean isEmailValid(String email) {
        boolean isGoodEmail = (email != null & Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if(!isGoodEmail){
            mEditTextEmailInput.setError("E-mail not Valid!");
            return false;
        }
        return isGoodEmail;

    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(LoginActivity.this, "No network connection!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean userDoesNotExist(String userEmail) {
        if (mFirebaseError.getCode() == FirebaseError.USER_DOES_NOT_EXIST) {
            Toast.makeText(LoginActivity.this, "User does not exist!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            showErrorToast(mFirebaseError.getMessage());
        }
        return true;
    }

    private boolean isPasswordValid(String password) {

        if (mFirebaseError.getCode() == FirebaseError.INVALID_PASSWORD) {
            mEditTextPasswordInput.setError("Invalid Password!");
            Toast.makeText(LoginActivity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
        } else {
            showErrorToast(mFirebaseError.getMessage());
        }
        return true;
    }

    private void showErrorToast (String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void getUserInfo() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String name = firebaseUser.getDisplayName();
            String email = firebaseUser.getEmail();
            String provider = firebaseUser.getProviderId();

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor spe = sp.edit();

            spe.putString(Constants.KEY_PROVIDER, provider).apply();
            spe.putString(Constants.KEY_ENCODED_MAIL, email).apply();
        }
    }

    private void getGoogleUserInfo(GoogleSignInAccount account) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor spe = sp.edit();

        String unprocessedEmail;

        if (mGoogleApiClient.isConnected()) {
            unprocessedEmail = account.getEmail().toLowerCase();
            spe.putString(Constants.KEY_GOOGLE_EMAIL, unprocessedEmail).apply();
        } else {
            unprocessedEmail = sp.getString(Constants.KEY_GOOGLE_EMAIL, null);
        }
        mEncodedEmail = Utils.encodeEmail(unprocessedEmail);
    }

}
