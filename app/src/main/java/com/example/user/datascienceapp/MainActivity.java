package com.example.user.datascienceapp;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button story, collect;

    static int counter = 0;
    ArrayList<DataBean> list1 = new ArrayList<DataBean>();
    // int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        story = (Button) findViewById(R.id.story_button);
        story.setOnClickListener(this);
        collect = (Button) findViewById(R.id.collect_button);
        collect.setOnClickListener(this);
        Drawable Background = findViewById(R.id.main1).getBackground();
        Background.setAlpha(80);

      /* FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("cards").child("card_1");
        list1 = create_databean_list.create_list();

        Iterator<DataBean> iterator=list1.iterator();
        int i=1;
        while(iterator.hasNext()){

            DataBean temp=iterator.next();
            if(i<=9)
                myRef.child("id_0"+temp.getId()).setValue(temp);
            else
                myRef.child("id_"+temp.getId()).setValue(temp);
            i++;
        }*/


    }

    public void onClick(View w) {
        switch (w.getId()) {
            case R.id.story_button:
                final ProgressDialog progressDialog=ProgressDialog.show(this,"Loading","Please Wait..",true);
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference story=database.getReference().child("story").child("story_1");
                story.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Story> list = new ArrayList<Story>();
                       for(DataSnapshot ds:dataSnapshot.getChildren()){
                           Story story = ds.getValue(Story.class);
                           list.add(story);
                       }
                        Intent next = new Intent(MainActivity.this, StoryActivity.class);
                        next.putExtra("Story",list);
                        progressDialog.dismiss();
                        story.removeEventListener(this);
                        startActivity(next);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                story.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;
            case R.id.collect_button:

               /* final Context context = this;
                final Dialog dialog = new Dialog(context);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogue_box_layout);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.getWindow().setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
                Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
                 //if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        create_databean_list a=new create_databean_list();
                        dialog.dismiss();
                   }
                });*/


                final ProgressDialog progressDialog1=ProgressDialog.show(this,"Loading","Please Wait..",true);
                final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                final DatabaseReference cards=database1.getReference().child("cards").child("card_1");
                cards.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<DataBean> list=new ArrayList<DataBean>();
                        int c=0;
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            DataBean dataBean=ds.getValue(DataBean.class);
                            list.add(dataBean);
                            c++;
                            Log.d("Card",dataBean.toString()+" "+c);
                        }

                        Intent next = new Intent(MainActivity.this, collect_data_activity.class);
                        next.putExtra("Card",list);
                        progressDialog1.dismiss();
                        cards.removeEventListener(this);
                        startActivity(next);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                cards.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
        }
    }
}
