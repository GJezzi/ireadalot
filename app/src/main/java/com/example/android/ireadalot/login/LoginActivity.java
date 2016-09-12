package com.example.android.ireadalot.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.android.ireadalot.R;
import com.example.android.ireadalot.activity.BaseActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by gjezzi on 08/09/16.
 */
public class LoginActivity extends BaseActivity {

    private static final String LOG_TAG = "LoginActivity";

    private ProgressDialog mProgressDialog;
    private EditText mEditTextEmailInput;
    private EditText mEditTextPasswordInput;

    private boolean mGoogleIntentProgress;

    public static final int RC_GOOGLE_LOGIN = 1;

    GoogleSignInAccount googleSignInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
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
}
