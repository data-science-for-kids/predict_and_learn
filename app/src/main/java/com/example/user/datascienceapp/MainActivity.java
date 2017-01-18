package com.example.user.datascienceapp;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button story, collect, signout,analyze;
    private ProgressBar progressBar;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        story = (Button) findViewById(R.id.story_button);
        story.setOnClickListener(this);
        collect = (Button) findViewById(R.id.collect_button);
        collect.setOnClickListener(this);
        signout = (Button) findViewById(R.id.signout);
        signout.setOnClickListener(this);
        analyze= (Button) findViewById(R.id.analyze_button);
        analyze.setOnClickListener(this);
        Drawable Background = findViewById(R.id.main1).getBackground();
        Background.setAlpha(80);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        intent = new Intent(MainActivity.this, CollectDataExerciseActivity.class);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_story, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onResume(){
        super.onResume();
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signout:

        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View w) {
        final View view = w;
        final DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        switch (w.getId()) {
            case R.id.story_button:

                progressBar.setVisibility(View.VISIBLE);
                StoryLoader storyLoader=new StoryLoader(getApplicationContext());
                int image[]={2,3,5,6,8,10,12,13,14,15,16,18};
                for(int i=0;i<12;i++){
                    StorageReference storyImg = FirebaseStorage.getInstance().getReference().child("story1/slide"+image[i]+".jpg");
                    if(i==2)
                    {
                        Glide.with(this)
                                .using(new FirebaseImageLoader())
                                .load(storyImg)
                                .listener(storyLoader).fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .preload(700,1200);
                    }
                    else {
                        Glide.with(this)
                                .using(new FirebaseImageLoader())
                                .load(storyImg)
                                .listener(storyLoader).fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .preload();
                    }
                }
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference story = database.getReference().child("story").child("story_1");

                story.addListenerForSingleValueEvent(storyLoader);
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

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
                break;
            case R.id.collect_button:
                final Context context = this;
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
                        ExerciseLoader exerciseLoader=new ExerciseLoader(getApplicationContext());
                        StorageReference female = FirebaseStorage.getInstance().getReference().child("card/rating_pic_f.png");
                        StorageReference male = FirebaseStorage.getInstance().getReference().child("card/rating_pic_m.png");
                        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                        DatabaseReference cards = database1.getReference().child("cards").child("card_1");

                        Glide.with(getBaseContext())
                                .using(new FirebaseImageLoader())
                                .load(female)
                                .listener(exerciseLoader).fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .preload(600,1000);
                        Glide.with(getBaseContext())
                                .using(new FirebaseImageLoader())
                                .load(male)
                                .listener(exerciseLoader)
                                .fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
                                .preload(600,1000);

                        cards.addListenerForSingleValueEvent(exerciseLoader);

                        connectedRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                boolean connected = snapshot.getValue(Boolean.class);

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                            }
                        });
                        dialog.dismiss();
                   }
                });
                dialog.show();
                //Starting a progress box
                progressBar.setVisibility(View.VISIBLE);

                break;
            case R.id.signout:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                // this listener will be called when there is change in firebase user session
                FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        //   Log.d("Here","Logout");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            // user auth state is changed - user is null
                            // launch login activity
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                };
                break;
            case R.id.analyze_button:
                progressBar.setVisibility(View.VISIBLE);
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference responses = database1.getReference().child("response").child("resid_Cr09A8pr4lb0Rem1qFirQe9sQH43").child("ses_01");
                ResponseLoader responseLoader=new ResponseLoader(getApplicationContext());
                responses.addValueEventListener(responseLoader);
                Log.d("analyze","button");
                break;
        }
    }

}
