package com.example.user.datascienceapp;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class StoryLoader implements RequestListener<StorageReference, GlideDrawable>,OnSuccessListener<byte[]> {
    private ArrayList<Story> list;
    private int count=0;
    private boolean text;
    private Context context;
    private long start,finish;
    public StoryLoader(Context context){
        this.context = context;
        text = false;
        start=System.currentTimeMillis();

    }
    public StoryLoader(){

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
    public void check(int count){
        if( count >= 7&& text ){
            Intent intent = new Intent(context,StoryActivity.class);
            intent.putExtra("Story",list);//sending story text
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Log.d("List Size Check",list.size()+"");
            finish=System.currentTimeMillis();
            Log.d("Time",Long.toString(finish - start));
            Log.d("finish",Long.toString(finish ));
            Log.d("start",Long.toString(start));
            context.startActivity(intent);
        }

    }
    @Override
    public void onSuccess(byte[] bytes) {
        list=new ArrayList<>();
        try {
            JSONObject j1 = new JSONObject(new String(bytes));
            JSONObject j2 = j1.getJSONObject("story_1");
            for(int i=1;i<=19;i++){
                JSONObject jsonObject;

                if(i<=9)
                    jsonObject = j2.getJSONObject("page_0"+i);// fetching json objects
                else
                    jsonObject = j2.getJSONObject("page_"+i);

                Story story = new Story();

                if(jsonObject.has("text"))
                    story.setText(jsonObject.getString("text"));
                else
                    story.setText("");

                    story.imagePresent(jsonObject.getBoolean("image"));
                list.add(story);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        text=true;
        check(count);
    }
}
