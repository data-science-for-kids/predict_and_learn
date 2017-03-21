package com.example.user.datascienceapp.Loaders;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.user.datascienceapp.Activities.PredictActivity;
import com.example.user.datascienceapp.Wrappers.Response;
import com.example.user.datascienceapp.Wrappers.Session;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Aman Mathur.
 * This class is used for loading the responses uses submitted last open the activity when loading completes
 */

public class ResponseLoader implements ValueEventListener {
    private ArrayList<Response> list;
    private int count=0;
    private Context context;
    public ResponseLoader(Context context){
        this.context=context;
         list = new ArrayList<>();
    }

    /**
     *
     * @param dataSnapshot (Objects list of Response Class)
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // Event listener to notify when responses are loaded
        if(!dataSnapshot.exists()){
            Log.d("No","Data");
        }
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Response response = ds.getValue(Response.class);
            list.add(response);
            Log.d("Response", response.toString());
        }
        count=1;
        Log.d("ListSize ",list.size()+"");

        check(count);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
    public void check(int count){
        if(count == 1){

            String uid="";
            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser() != null) {
                FirebaseUser user = mAuth.getCurrentUser();
                uid = user.getUid();
            }

            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            final DatabaseReference cardRef = firebaseDatabase.getReference().child("session").child(uid); //getting the session number
            cardRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Session s=dataSnapshot.getValue(Session.class);
                    int i = s.getPage();
                    Log.d("Page Send",""+i);
                    Intent intent = new Intent(context,PredictActivity.class);
                    intent.putExtra("Response",list);
                    intent.putExtra("Page",i);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Log.d("List Size Check",list.size()+"");
                    cardRef.removeEventListener(this);
                    context.startActivity(intent);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }
    }





}
