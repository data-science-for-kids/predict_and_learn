package com.example.user.datascienceapp;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class CollectDataExerciseActivity extends AppCompatActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    private Button newq;
    private TextView id, name, game;
    private RatingBar ratingBar;
    private ImageView rating_image;
    private int pic_no = 1;
    private int card_no = 1;      //used for checking slide no
    private ArrayList<DataBean> cards;
    private int page = -1, session;

    @Override
    public void onBackPressed() {

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
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String uid = "admin";
                if (mAuth.getCurrentUser() != null) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    uid = user.getUid();
                }

                SharedPreferences.Editor editor = getSharedPreferences("Page", MODE_PRIVATE).edit();
                editor.putInt(uid, page - 1);
                editor.commit();
                Log.d("pageCommit", (page - 1) + "");
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_collect_data_activity);
        newq = (Button) findViewById(R.id.button);
        id = (TextView) findViewById(R.id.id);
        name = (TextView) findViewById(R.id.name);
        game = (TextView) findViewById(R.id.game);
        rating_image = (ImageView) findViewById(R.id.rating_image);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(this);
        newq.setOnClickListener(CollectDataExerciseActivity.this);
        cards = (ArrayList<DataBean>) getIntent().getSerializableExtra("Card");

        String uid = "admin";
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            uid = user.getUid();
        }
        SharedPreferences prefs = getSharedPreferences("Page", MODE_PRIVATE);
        card_no = prefs.getInt(uid, 1);
        pic_no = prefs.getInt(uid, 1);//0 is the default value.
        session = prefs.getInt("session" + uid, 1);
        Log.d("pageRead", card_no + "   " + session);

        if (card_no == -1) {
            card_no = 1;
            pic_no = 1;
        }

        game.setText(cards.get(card_no - 1).getGame());
        id.setText(cards.get(card_no - 1).getId() + "/" + "56");
        name.setText(cards.get(card_no - 1).getName());


        StorageReference female = FirebaseStorage.getInstance().getReference().child("card/rating_pic_f.png");
        StorageReference male = FirebaseStorage.getInstance().getReference().child("card/rating_pic_m.png");
        //Images are fetched from server and not drawable


        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(female).diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .override(600, 1000)
                .into(rating_image);


        page = card_no + 1;

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:

                float a = ratingBar.getRating();
                String uid = "admin";

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

                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    if (mAuth.getCurrentUser() != null) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        uid = user.getUid();
                    }
                    Response res = new Response();
                    res.setCard("id_" + pic_no);
                    res.setExercise("friends");
                    res.setResponse(a + "");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference response;
                    if (session < 10) {
                        if (pic_no < 10)
                            response = database.getReference("response").child("resid_" + uid).child("ses_0" + session).child("id_0" + pic_no);
                        else
                            response = database.getReference("response").child("resid_" + uid).child("ses_0" + session).child("id_" + pic_no);
                    } else {
                        if (pic_no < 10)
                            response = database.getReference("response").child("resid_" + uid).child("ses_" + session).child("id_0" + pic_no);
                        else
                            response = database.getReference("response").child("resid_" + uid).child("ses_" + session).child("id_" + pic_no);
                    }
                    response.setValue(res);

                    if (card_no < 56) {
                        page++;
                        ratingBar.setRating(0);
                        // Log.d("Pic no in if", pic_no + "");
                        game.setText(cards.get(pic_no).getGame());
                        id.setText(cards.get(pic_no).getId() + "/" + "56");
                        name.setText(cards.get(pic_no).getName());
                        String gender = cards.get(pic_no).getGender();

                        StorageReference female = FirebaseStorage.getInstance().getReference().child   ("card/rating_pic_f.png");
                        StorageReference male = FirebaseStorage.getInstance().getReference().child("card/rating_pic_m.png");
                        if (gender.equals("FEMALE")) {
                           // Log.d("gender", "FEMALE");
                            Glide.with(this)
                                    .using(new FirebaseImageLoader())
                                    .load(female).diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .fitCenter()
                                    .override(600, 1000)
                                    .into(rating_image);
                        } else if (gender.equals("MALE")) {
                            //Log.d("gender", "MALE");

                            Glide.with(this)
                                    .using(new FirebaseImageLoader())
                                    .load(male).diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .fitCenter()
                                    .override(600, 1000)
                                    .into(rating_image);
                        }

                        final View l = findViewById(R.id.main);
                        Animation ab = AnimationUtils.loadAnimation(CollectDataExerciseActivity.this, R.anim.blink);
                        ab.setDuration(1500);
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
                        card_no++;

                        // cards finished
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
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                String uid = "admin";
                                if (mAuth.getCurrentUser() != null) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    uid = user.getUid();
                                }
                                Log.d("user", "id");


                                SharedPreferences.Editor editor = getSharedPreferences("Page", MODE_PRIVATE).edit();
                                editor.putInt(uid, -1);
                                editor.putInt("session" + uid, session + 1);
                                editor.commit();
                                Log.d("pageCommit", -1 + "");

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference response = database.getReference("user").child(uid);
                                response.setValue(session + 1);
                                finish();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        ratingBar.setRating(0);
                    }
                }
                break;
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating,
                                boolean fromTouch) {
    }


}