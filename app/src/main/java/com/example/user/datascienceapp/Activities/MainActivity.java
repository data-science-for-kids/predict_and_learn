package com.example.user.datascienceapp.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.user.datascienceapp.Loaders.ExerciseLoader;
import com.example.user.datascienceapp.Loaders.ResponseLoader;
import com.example.user.datascienceapp.Loaders.StoryLoader;
import com.example.user.datascienceapp.R;
import com.example.user.datascienceapp.Wrappers.Session;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * Main Activity to display buttons for the three activities
 * -> Story Activity
 * -> Collect Data Activity
 * -> Predict Activity
 * Main Activity asynchronously downloads images from Firebase Database for Story Activity
 */


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button story, collect, signout,analyze;
    private ProgressBar progressBar;
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
       // progressBar.setVisibility(View.GONE);
        FloatingActionButton floatingActionButton= (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Data Science",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("Main","onResume");
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Main","onPause");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onPause();
    }

    /**
     *
     * @param w - contains the view of the activity being called
     */
    public void onClick(View w) {
        final DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        switch (w.getId()) {
            case R.id.story_button:

                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                StoryLoader storyLoader=new StoryLoader(getApplicationContext());
                StorageReference storyText=FirebaseStorage.getInstance().getReference().child("datasciencekids-master-story-export.json");
                final long ONE_MEGABYTE = 1024 * 1024;
                storyText.getBytes(ONE_MEGABYTE).addOnSuccessListener(storyLoader).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

                int[] image={2,3,5,6,8,10,12,13,14,15,16,18}; //image ids

                /**
                 * Using the loader class images are downloaded from Firebase.
                 * When the files are loaded the loader class calls the Story Activity and finishes the Main Activity
                 */

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

                break;
            case R.id.collect_button:

                Context context = this;
                final Dialog dialog = new Dialog(context);
                //Initiating the dialog box
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogue_box_layout);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.getWindow().setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);

                Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Exercise Loader is a listener that calls the CollectData Activity when the images and corresponding data is laoded
                        ExerciseLoader exerciseLoader=new ExerciseLoader(getApplicationContext());

                        //Lazy Loading for image of boy and girl
                        StorageReference female = FirebaseStorage.getInstance().getReference().child("card/rating_pic_f.png");
                        StorageReference male = FirebaseStorage.getInstance().getReference().child("card/rating_pic_m.png");

                        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                        DatabaseReference cards = database1.getReference().child("cards").child("card_2");

                        progressBar.setVisibility(View.VISIBLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

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
                                if(!connected) {

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                            }
                        });
                        dialog.dismiss();
                   }
                });
                dialog.show();
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {    //close the dialog
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        progressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });
                //Starting a progress box


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
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            // user auth state is changed - user is null
                            // launch login activity
                            Log.d("Signout","Button");
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                };
                break;
            case R.id.analyze_button:


                String uid="";
                progressBar.setVisibility(View.VISIBLE);
                final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    uid = user.getUid();
                }

                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference cardRef = firebaseDatabase.getReference().child("session").child(uid);
                cardRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       // progressBar.setVisibility(View.GONE);
                        Session s = dataSnapshot.getValue(Session.class);
                        if(s != null) {
                            int card = s.getCard();

                            long ses = (long) dataSnapshot.child("ses").getValue();

                            Log.d("Session",ses+"");
                            Log.d("Card",card+"");

                            ses = (ses == 1) ? 1 : ses-1;

                            if(card == -1){
                                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                String uid="";
                                if (mAuth.getCurrentUser() != null) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    uid = user.getUid();
                                }
                                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                                DatabaseReference responses = database1.getReference().child("response").child("resid_"+uid).child("ses_0"+ses);
                                ResponseLoader responseLoader=new ResponseLoader(getApplicationContext());
                                responses.addValueEventListener(responseLoader);
                                Log.d("analyze","button");
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                showDialog();
                            }


                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            showDialog();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        showDialog();
                    }

                    public void showDialog(){

                        progressBar.setVisibility(View.GONE);
                        final Dialog dialog1=new Dialog(MainActivity.this);
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.setContentView(R.layout.complete_exercise_dialog_box);
                        dialog1.setCanceledOnTouchOutside(false);
                        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog1.getWindow().setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
                        Button declineButton1 = (Button) dialog1.findViewById(R.id.declineButton);
                        declineButton1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                            }
                        });
                        dialog1.show();
                    }
                });


                break;
        }
    }


}
