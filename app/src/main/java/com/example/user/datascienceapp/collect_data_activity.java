package com.example.user.datascienceapp;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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


public class collect_data_activity extends AppCompatActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {
    View v;
    private Button newq;
    private TextView id, name, game;
    private RatingBar ratingBar;
    private ImageView rating_image;
    private int pic_no = 1;
    private int j =1 ;      //used for checking slide no
    private ArrayList<DataBean> cards;

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
        cards= (ArrayList<DataBean>) getIntent().getSerializableExtra("Card");

        game.setText(cards.get(0).getGame());
        id.setText(cards.get(0).getId()+"/"+"56");
        name.setText(cards.get(0).getName());

        StorageReference female = FirebaseStorage.getInstance().getReference().child("card/rating_pic_f.png");
        StorageReference male = FirebaseStorage.getInstance().getReference().child("card/rating_pic_m.png");
       //Images are fetched from server and not drawable

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(female)
                .fitCenter()
                .override(600, 1000)
                .into(rating_image);


      /*  Glide.with(this).using(new FirebaseImageLoader())
                .load(female).downloadOnly(600,1000);
        Glide.with(this).using(new FirebaseImageLoader())
                .load(male).downloadOnly(600,1000);*/

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

                        Response res = new Response();

                        res.setCard("id_" +pic_no);
                        res.setUser("admin");
                        res.setExercise("friends");
                        res.setResponse(a + "");
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference response;
                        if(pic_no<=9)
                             response= database.getReference("response").child("resid_0" + pic_no );
                        else
                             response = database.getReference("response").child("resid_" + pic_no );
                        response.setValue(res);
                        if (j <= 56) {
                            ratingBar.setRating(0);
                           // Log.d("Pic no in if", pic_no + "");
                            game.setText(cards.get(pic_no).getGame());
                            id.setText(cards.get(pic_no).getId()+"/"+"56");
                            name.setText(cards.get(pic_no).getName());
                            String gender=cards.get(pic_no).getGender();

                            StorageReference female = FirebaseStorage.getInstance().getReference().child("card/rating_pic_f.png");
                            StorageReference male = FirebaseStorage.getInstance().getReference().child("card/rating_pic_m.png");
                            if(gender.equals("FEMALE")){
                                Log.d("gender","FEMALE");
                                Glide.with(this)
                                        .using(new FirebaseImageLoader())
                                        .load(female)
                                        .fitCenter()
                                        .override(600, 1000)
                                        .into(rating_image);
                            }
                            else if(gender.equals("MALE")) {
                                Log.d("gender","MALE");
                                Glide.with(this)
                                        .using(new FirebaseImageLoader())
                                        .load(male)
                                        .fitCenter()
                                        .override(600, 1000)
                                        .into(rating_image);
                            }

                            final View l = findViewById(R.id.main);
                            Animation ab = AnimationUtils.loadAnimation(
                                    collect_data_activity.this, R.anim.blink);
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