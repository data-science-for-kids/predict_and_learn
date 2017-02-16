package com.example.user.datascienceapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4500;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar01);
        Drawable loginActivityBackground = findViewById(R.id.splash).getBackground();
        loginActivityBackground.setAlpha(100);

        progress.setIndeterminate(true);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
             @Override
             public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                  // User is signed in
                  Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                 } else {
                  // User is signed out
                  Log.d(TAG, "onAuthStateChanged:signed_out");
                 }
                  }
             };

        mAuth.signInAnonymously()
         .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
            Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());
               load();
               Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
               startActivity(i);
               finish();

            if (!task.isSuccessful()) {
             Log.w(TAG, "signInAnonymously", task.getException());
             Toast.makeText(SplashScreenActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
             }
            }
            });

    }
    @Override
    public void onStart() {
         super.onStart();
         mAuth.addAuthStateListener(mAuthListener);
    }
    public void load(){
        StorageReference storyText=FirebaseStorage.getInstance().getReference().child("datasciencekids-master-story-export.json");
        final long ONE_MEGABYTE = 1024 * 1024;
        storyText.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("Text","Yes");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        int image[]={2,3,5,6,8,10,12,13,14,15,16,18};
        for(int i=0;i<12;i++){
            StorageReference storyImg = FirebaseStorage.getInstance().getReference().child("story1/slide"+image[i]+".jpg");
            if(i==2)
            {
                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storyImg)
                        .listener(new RequestListener<StorageReference, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Log.d("Image loaded at","Splash");
                                return false;
                            }
                        }).fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .preload(700,1200);
            }
            else {
                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storyImg)
                        .listener(new RequestListener<StorageReference, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Log.d("Image loaded at","Splash");
                                return false;
                            }
                        }).fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .preload();
            }
        }
    }

    @Override
    public void onStop() {
         super.onStop();
         if (mAuthListener != null) {
              mAuth.removeAuthStateListener(mAuthListener);
             }
    }
}
