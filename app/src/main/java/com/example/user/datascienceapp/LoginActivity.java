package com.example.user.datascienceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText fname,lname,school,grade;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        fname= (EditText) findViewById(R.id.fname);
        lname= (EditText) findViewById(R.id.lname);
        school= (EditText) findViewById(R.id.school);
        grade= (EditText) findViewById(R.id.grade);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(getBaseContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                    Log.d("Tag", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("Tag", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }
    @OnClick(R.id.sign_up_button)
    void login(View view){
        progressBar.setVisibility(View.VISIBLE);
        String f_name=fname.getText().toString();
        String l_name=lname.getText().toString();
        String school_name=school.getText().toString();
        String grade_name=grade.getText().toString();
        String email=f_name+l_name+"."+grade_name+"@"+school_name+".com";
        String password="password123";
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Tag", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                        }

                    }
                });

    }
    @Override
    public void onStop() {
        super.onStop();
       if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
        }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


}
