package com.example.user.datascienceapp;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class StoryLoader implements RequestListener<StorageReference, GlideDrawable>/*,ValueEventListener*/,OnSuccessListener<byte[]> {
    private ArrayList<Story> list;
    private int count=0;
    private boolean text;
    private Context context;
    private JSONObject JSONtext;
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
/*
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        list= new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Story story = ds.getValue(Story.class);
            list.add(story);
        }
        text=true;
        Log.d("ListSize",list.size()+"");
        check(count);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }*/
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

    @Override
    public void onSuccess(byte[] bytes) {
        list=new ArrayList<>();
        try {
            JSONObject j1=new JSONObject(new String(bytes));
            JSONObject j2=j1.getJSONObject("story_1");
            for(int i=1;i<=19;i++){
                JSONObject jsonObject;

                if(i<=9)
                    jsonObject=j2.getJSONObject("page_0"+i);
                else
                    jsonObject=j2.getJSONObject("page_"+i);

                Story story=new Story();
                if(jsonObject.has("text"))
                    story.setText(jsonObject.getString("text"));
                else
                    story.setText("");
                    story.imagePresent(jsonObject.getBoolean("image"));
              //  Log.d(i+"",story.toString());
                list.add(story);
            }

            //Log.d("list",list.size()+"");
            //Log.d("JSON",j2.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        text=true;
        check(count);
    }
}
