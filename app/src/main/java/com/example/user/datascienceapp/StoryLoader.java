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

public class StoryLoader implements RequestListener<StorageReference, GlideDrawable>,ValueEventListener {
    private ArrayList<Story> list;
    private int count=0;
    private boolean text;
    private Context context;
    public StoryLoader(Context context){
        this.context=context;
        text=false;

    }

    @Override
    public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        count++;
        Log.d("Image",count+"");
        check(count);
        return false;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        list= new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Story story = ds.getValue(Story.class);
            list.add(story);
        }
        text=true;
        Log.d("ListSize",list.size()+"");
        Log.d("Data",count+"");
        check(count);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
    public void check(int count){
        if(count>=7&&text){
            Intent intent = new Intent(context,StoryActivity.class);
            intent.putExtra("Story",list);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Log.d("List Size Check",list.size()+"");
            context.startActivity(intent);
        }

    }

}
