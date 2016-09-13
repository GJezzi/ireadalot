package com.example.android.ireadalot.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.ireadalot.R;
import com.example.android.ireadalot.activity.BaseActivity;
import com.example.android.ireadalot.activity.MainActivity;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    private String mUserEmail;
    private String mUserPassword;

    private boolean mGoogleIntentProgress;

    public static final int RC_GOOGLE_LOGIN = 1;

    GoogleSignInAccount mGoogleSignInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        initializeScreen();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(LOG_TAG, "onAuthStateChanged:signedIn: " + user.getUid());
                } else {
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
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

    public void initializeScreen() {
        mEditTextEmailInput = (EditText) findViewById(R.id.edit_text_email);
        mEditTextPasswordInput = (EditText) findViewById(R.id.edit_text_password);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mProgressDialog.setMessage(getString(R.string.progress_dialog_signing_in));
        mProgressDialog.setCancelable(false);

    }

    public void onSignUpPressed(View view) {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    public void onSignInPressed(View view) {
        mUserEmail = mEditTextEmailInput.getText().toString();
        mUserPassword = mEditTextPasswordInput.getText().toString();

        boolean validEmail = isEmailValid(mUserEmail);
        boolean networkAvailable = isNetworkAvailable(mContext);
        boolean emptyForm = isFormEmpty();
        boolean validPassword = isPasswordValid(FirebaseError.fromCode(FirebaseError.INVALID_PASSWORD), mUserPassword);
        boolean existUser = userDoesNotExist(FirebaseError.fromCode(FirebaseError.USER_DOES_NOT_EXIST));

        if(!validEmail || !networkAvailable || !emptyForm || !validPassword || !existUser) return;

        signInPassword();
    }

    public void signInPassword() {
        mProgressDialog.show();
        mFirebaseAuth.signInWithEmailAndPassword(mUserEmail, mUserPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "singInWithEmail:onComplete: " + task.isSuccessful());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                if (!task.isSuccessful()) {
                    Log.w(LOG_TAG, "signInWithEmail:failed", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.dismiss();
            }
        });
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
            Toast.makeText(LoginActivity.this, "No Internet Connection found!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean userDoesNotExist(FirebaseError firebaseError) {
        if (firebaseError.getCode() == FirebaseError.USER_DOES_NOT_EXIST) {
            Toast.makeText(LoginActivity.this, "User does not exist!", Toast.LENGTH_SHORT).show();
        } else {
            showErrorToast(firebaseError.getMessage());
        }
        return false;
    }

    private boolean isPasswordValid(FirebaseError firebaseError, String password) {
        if (firebaseError.getCode() == FirebaseError.INVALID_PASSWORD) {
            mEditTextPasswordInput.setError("Invalid Password!");
        } else {
            showErrorToast(firebaseError.getMessage());
        }
        return false;
    }

    private boolean isFormEmpty(){
        boolean valid = true;
        mUserEmail = mEditTextEmailInput.getText().toString();
        mUserPassword = mEditTextPasswordInput.getText().toString();

        if(TextUtils.isEmpty(mUserEmail)) {
            mEditTextEmailInput.setError("E-mail Required!");
            valid = false;
        } else {
            mEditTextEmailInput.setError(null);
        }

        if (TextUtils.isEmpty(mUserPassword)) {
            mEditTextPasswordInput.setError("Password Required!");
            valid = false;
        } else {
            mEditTextPasswordInput.setError(null);
        }
        return valid;
    }

    private void showErrorToast (String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}
