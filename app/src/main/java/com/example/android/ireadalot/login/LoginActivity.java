package com.example.android.ireadalot.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.ireadalot.R;
import com.example.android.ireadalot.activity.BaseActivity;
import com.example.android.ireadalot.activity.MainActivity;
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

    private void showErrorToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private boolean isEmailValid(String email) {
        return true;
    }

    private boolean hasNoConnection() {
        return true;
    }

    private boolean userDoesNotExist() {
        return true;
    }

    private boolean isPasswordValid(String password) {
        return true;
    }

    private boolean isEmailEmpty(){
        return true;
    }

    private boolean isPasswordEmpty() {
        return true;
    }

}
