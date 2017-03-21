package com.example.user.datascienceapp.Loaders;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.user.datascienceapp.Activities.CollectDataExerciseActivity;
import com.example.user.datascienceapp.Wrappers.DataBean;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Aman Mathur
 * This class is used for loading the information about the cards from database and open the activity when loading completes
 */

public class ExerciseLoader  implements RequestListener<StorageReference, GlideDrawable>,ValueEventListener {
    private ArrayList<DataBean> list;
    private int count=0;
    private Context context;
    public ExerciseLoader(Context context){
        this.context=context;
         list = new ArrayList<DataBean>();
    }

    /**
     * @param dataSnapshot (Objects list of DataBean Class)
     */

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        int c = 0;
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            DataBean dataBean = ds.getValue(DataBean.class);
            list.add(dataBean);
            c++;
            Log.d("Card", dataBean.toString() + " " + c);
        }
        count++;
        Log.d("ListSize",list.size()+"");
        Log.d("Data",count+"");
        check(count);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
    public void check(int count){
        /**
         * Count == 3 ie. Two images and one for all the text
         */
        if(count==3){
            Intent intent = new Intent(context,CollectDataExerciseActivity.class);
            intent.putExtra("Card",list);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d("List Size Check",list.size()+"");
            context.startActivity(intent);
        }
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

}
