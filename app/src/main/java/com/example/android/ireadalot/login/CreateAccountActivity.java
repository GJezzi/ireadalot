package com.example.android.ireadalot.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.ireadalot.R;
import com.example.android.ireadalot.activity.BaseActivity;
import com.example.android.ireadalot.utils.Constants;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by gjezzi on 08/09/16.
 */
public class CreateAccountActivity extends BaseActivity {

    private final static String LOG_TAG = "CreateAccountActivity";

    private Firebase mFirebase;

    private ProgressDialog mProgressDialog;
    private EditText mEditTextNameCreate;
    private EditText mEditTextPasswordCreate;
    private EditText mEditTextEmailCreate;

    private String mUserName;
    private String mUserPassword;
    private String mUserEmail;

    public final static Pattern EMAIL_PATTERN = Patterns.EMAIL_ADDRESS;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_account);
        mFirebase = new Firebase(Constants.FIREBASE_URL);
        initializeScreen();
    }

    public void initializeScreen () {
        mEditTextNameCreate = (EditText) findViewById(R.id.edit_text_username_create);
        mEditTextEmailCreate = (EditText) findViewById(R.id.edit_text_email_create);
        mEditTextPasswordCreate = (EditText) findViewById(R.id.edit_text_password_create);
        //LinearLayout linearLayoutCreateAccountActivity = (LinearLayout) findViewById(R.id.linear_layout_create_account_activity);
        //initializeBackground(linearLayoutCreateAccountActivity);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        mProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_signing_up));
        mProgressDialog.setCancelable(false);
    }


    public void onSignInPressed() {
        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void onCreateAccountPressed(View view) {
        mUserName = mEditTextNameCreate.getText().toString();
        mUserEmail = mEditTextEmailCreate.getText().toString();
        mUserPassword = mEditTextPasswordCreate.getText().toString();

        boolean validEmail = isEmailValid(mUserEmail);
        boolean validUserName = isUserNameValid(mUserName);
        boolean validPassword = isPasswordValid(mUserPassword);

        if(!validEmail || !validUserName || !validPassword) return;

        mProgressDialog.show();

        createFirebaseUserHelper(mUserEmail);

    }

    private void createFirebaseUserHelper(final String encodedMail) {
        mFirebase.createUser("ingle1933@fleckens.hu", "TestBook123", new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                mProgressDialog.dismiss();
                Log.i(LOG_TAG, "Successfully created user account with uid: " + result.get("uid"));
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Log.d(LOG_TAG, "Error: " + firebaseError);
                mProgressDialog.dismiss();

                if(firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
                    mEditTextEmailCreate.setError("Sorry! That E-mail is taken!");
                } else {
                    showErrorToast(firebaseError.getMessage());
                }
            }
        });
    }

    private boolean isEmailValid (String email) {
        return EMAIL_PATTERN.matcher(email).matches();

    }

    private boolean isUserNameValid (String userName) {
        if(userName.equals("")) {
            mEditTextNameCreate.setError("Email cannot be empty!");
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        if(password.length() < 6) {
            mEditTextPasswordCreate.setError("Password is not valid!");
        }
        return true;
    }

    private void showErrorToast (String message) {
        Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
