package com.example.user.datascienceapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Aman Mathur on 1/11/2017.
 */

public class ResponseLoader implements ValueEventListener {
    MainActivity mainActivity;
    private ArrayList<Response> list;
    private int count=0;
    private Context context;
    public ResponseLoader(Context context){
        this.context=context;
         list = new ArrayList<Response>();
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {


        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Response response = ds.getValue(Response.class);
            list.add(response);

            Log.d("Response", response.toString());
        }
        count=1;
        Log.d("ListSize",list.size()+"");

        check(count);

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
    public void check(int count){
        if(count==1){
            Intent intent = new Intent(context,AnalysisPage.class);
            intent.putExtra("Response",list);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d("List Size Check",list.size()+"");
            context.startActivity(intent);
        }
    }





}
