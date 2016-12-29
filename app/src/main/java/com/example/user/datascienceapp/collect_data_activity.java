package com.example.user.datascienceapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.util.ArrayList;
import java.util.Iterator;


public class collect_data_activity extends AppCompatActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {
    View v;
    Button newq;
    TextView id, name, game;
    CoordinatorLayout snackbarCoordinatorLayout;
    RatingBar ratingBar;
    ImageView rating_image;

    int pic_no = 1;
    int j = 2;                //used for checking slide no
    static int numStars = 0;


    @Override
    public void onBackPressed() {
        //do nothing
        //make back button dis-functional

        final Context context = this;


        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.back_pressed_dialog_box);
        dialog.setCanceledOnTouchOutside(false);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        Button okButton = (Button) dialog.findViewById(R.id.okButton1);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelbutton);

        // if decline button is clicked, close the custom dialog

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                //create_databean_list a=new create_databean_list();


                finish();

                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                //create_databean_list a=new create_databean_list();


                dialog.dismiss();
            }
        });
        dialog.show();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_collect_data_activity);
        newq = (Button) findViewById(R.id.button);
        id = (TextView) findViewById(R.id.id);
        name = (TextView) findViewById(R.id.name);
        game = (TextView) findViewById(R.id.game);

        rating_image = (ImageView) findViewById(R.id.rating_image);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(this);
        newq.setOnClickListener(collect_data_activity.this);
       // list1 = create_databean_list.create_list();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference cards = database.getReference("cards").child("id_1");
        cards.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    //Data being fetched from server database
                DataBean ob=dataSnapshot.getValue(DataBean.class);
                game.setText("" + ob.getGame());
                id.setText("" + ob.getId() + "/" + "56");
                name.setText("" + ob.getName());
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
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("card/rating_pic_1.png");

       //Images are fetched from server and not drawable
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(mStorageRef)
                .fitCenter()
                .override(600,1000)
                .into(rating_image);
        pic_no++;

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:


                float a = ratingBar.getRating();
                if (a == 0) {

                    final Context context = this;


                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.no_rating_dialog_box);
                    dialog.setCanceledOnTouchOutside(false);

                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    dialog.getWindow().setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
                    Button declineButton = (Button) dialog.findViewById(R.id.declineButton);


                    declineButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                } else {
                    String Tag = "" + a;
                    Log.e(Tag, Tag);
                     FirebaseDatabase database = FirebaseDatabase.getInstance();

                    if (j <= 56) {

                        ratingBar.setRating(0);
                    Log.d("Pic no",pic_no+"");
                        DatabaseReference cards = database.getReference("cards").child("id_"+pic_no);
                        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("card/rating_pic_"+pic_no+".png");
                        cards.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                DataBean ob=dataSnapshot.getValue(DataBean.class);
                                game.setText("" + ob.getGame());
                                id.setText("" + ob.getId() + "/ 56");
                                name.setText("" + ob.getName());

                                Log.d("Image no",""+ob.getImage());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                        Glide.with(this)
                                .using(new FirebaseImageLoader())
                                .load(mStorageRef)
                                .fitCenter()
                                .override(600,1000)
                                .into(rating_image);

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
                        final View l = findViewById(R.id.main);
                        Animation ab = AnimationUtils.loadAnimation(
                                collect_data_activity.this, R.anim.blink);
                        ab.setDuration(3500);
                        ab.setAnimationListener(new Animation.AnimationListener() {

                            public void onAnimationEnd(Animation animation) {
                                // Do what ever you need, if not remove it.

                            }

                            public void onAnimationRepeat(Animation animation) {
                                // Do what ever you need, if not remove it.
                            }

                            public void onAnimationStart(Animation animation) {
                                // Do what ever you need, if not remove it.

                            }

                        });
                        l.startAnimation(ab);
                        pic_no++;
                        j++;

                    } else {
                        final Context context = this;


                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.ending_dialog_box);
                        dialog.setCanceledOnTouchOutside(false);

                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog.getWindow().setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
                        Button declineButton = (Button) dialog.findViewById(R.id.declineButton);


                        declineButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                finish();

                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        ratingBar.setRating(0);
                        //dialog.getWindow().setAttributes(lp);

                    }
                }
                break;
        }
    }

    /**
     * Notification that the rating has changed.
     *
     * @see android.widget.RatingBar.OnRatingBarChangeListener#onRatingChanged(android.widget.RatingBar,
     * float, boolean)
     */
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating,
                                boolean fromTouch) {
        //do nothing


    }


}