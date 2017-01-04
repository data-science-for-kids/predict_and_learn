package com.example.user.datascienceapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button story, collect;
    private ProgressBar progressBar;

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
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
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
        final View view=w;
        final DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        switch (w.getId()) {
            case R.id.story_button:

                progressBar.setVisibility(View.VISIBLE);
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
                        next.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        next.putExtra("Story",list);
                        progressBar.setVisibility(View.GONE);
                        story.removeEventListener(this);
                        startActivity(next);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                       // Snackbar.make(view, "Error While Connecting", Snackbar.LENGTH_LONG)
                         //       .setAction("RETRY", null).show();
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

                connectedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);
                        if (connected) {

                        } else {
                            progressBar.setVisibility(View.GONE);
                         //   Snackbar.make(view, "No Internet", Snackbar.LENGTH_LONG)
                           //         .setAction("RETRY", null).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
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

                //Starting a progress box
                progressBar.setVisibility(View.VISIBLE);

                //Creating reference to images in database
                StorageReference female = FirebaseStorage.getInstance().getReference().child("card/rating_pic_f.png");
                StorageReference male = FirebaseStorage.getInstance().getReference().child("card/rating_pic_m.png");

                //Adding listener to both
                final long ONE_MEGABYTE = 1024 * 1024;
                female.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                     @Override
                     public void onSuccess(byte[] bytes) {

                         }
                }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception exception) {
                       // Handle any errors
                       }
                });

                male.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
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

                        Intent next = new Intent(MainActivity.this, CollectDataExerciseActivity.class);
                        next.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        next.putExtra("Card",list);
                        progressBar.setVisibility(View.GONE);
                        cards.removeEventListener(this);
                        startActivity(next);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        /*Snackbar.make(view, "Error While Connecting", Snackbar.LENGTH_LONG)
                                .setAction("RETRY", null).show();*/
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
                connectedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);
                        if (connected) {
                            // System.out.println("connected");
                        } else {
                            progressBar.setVisibility(View.GONE);
                           // Snackbar.make(view, "No Internet", Snackbar.LENGTH_LONG)
                             //       .setAction("RETRY", null).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
                break;
        }
    }

}
