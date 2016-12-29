package com.example.user.datascienceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
EditText e1,e2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        e1= (EditText) findViewById(R.id.et_firstName);
        e2= (EditText) findViewById(R.id.et_lasttName);

    }
    @OnClick(R.id.btn_login)
    void login(View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Users");



// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            public static final String TAG = "Tag";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.hasChild("Aman")){
                   String value = dataSnapshot.getValue(String.class);
                   Log.d(TAG, "Value is: " + value);
               }
                else{
                   myRef.setValue("Aman");
               }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
