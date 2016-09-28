package com.example.android.ireadalot.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.ireadalot.R;
import com.example.android.ireadalot.activity.BaseActivity;
import com.example.android.ireadalot.model.User;
import com.example.android.ireadalot.utils.Constants;
import com.example.android.ireadalot.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by gjezzi on 08/09/16.
 */
public class CreateAccountActivity extends BaseActivity {

    private final static String LOG_TAG = "CreateAccountActivity";

    private Firebase mFirebase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

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


    public void onSignInPressed(View view) {
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
        final boolean takenEmail = isEmailTaken(FirebaseError.fromCode(FirebaseError.EMAIL_TAKEN), mUserEmail);

        if(!validEmail || !validUserName || !validPassword || takenEmail) return;

        mProgressDialog.show();

        mFirebaseAuth.createUserWithEmailAndPassword(mUserEmail, mUserPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(LOG_TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                Toast.makeText(CreateAccountActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

                createUserInFirebaseHelper();

                if (!task.isSuccessful()) {
                    Toast.makeText(CreateAccountActivity.this, getString(R.string.email_account_creation_error), Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.dismiss();
            }
        });
    }

    /**
     * Creates a new user in Firebase from the Java POJO
     */
    private void createUserInFirebaseHelper() {

        final String encodedEmail = Utils.encodeEmail(mUserEmail);

        final Firebase userLocation = new Firebase(Constants.FIREBASE_URL_USERS).child(encodedEmail);

        userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    HashMap<String, Object> timestampJoined = new HashMap<>();
                    timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

                    User newUser = new User(encodedEmail, mUserName, timestampJoined);
                    userLocation.push().setValue(newUser);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(LOG_TAG, getString(R.string.email_account_creation_error) + firebaseError.getMessage());
            }
        });
    }

    private boolean isEmailTaken (FirebaseError firebaseError, String email) {
        Log.d(LOG_TAG, getString(R.string.email_account_creation_error) + firebaseError);
        mProgressDialog.dismiss();

        if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
            mEditTextEmailCreate.setError("E-mail Taken!");
        } else {
            showErrorToast(firebaseError.getMessage());
        }
        return false;
    }

    private boolean isEmailValid (String email) {
        boolean isGoodEmail = (email != null & Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if(!isGoodEmail){
            mEditTextEmailCreate.setError("E-mail not Valid!");
            return false;
        }
        return isGoodEmail;
    }

    private boolean isUserNameValid (String userName) {
        if(userName.equals("")) {
            mEditTextNameCreate.setError("E-mail cannot be empty!");
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        if(password.length() < 6) {
            mEditTextPasswordCreate.setError("Password is not valid!");
            return false;
        }
        return true;
    }

    private void showErrorToast (String message) {
        Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
