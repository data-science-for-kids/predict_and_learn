package com.example.user.datascienceapp;

import android.app.ActionBar;
import android.app.Dialog;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button story, collect;

    static int counter = 0;

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


    }

    public void onClick(View w) {
        switch (w.getId()) {
            case R.id.story_button:
                Intent next = new Intent(MainActivity.this, Slider_activity.class);
                startActivity(next);
                break;
            case R.id.collect_button:
                final Context context = this;

                final Dialog dialog = new Dialog(context);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogue_box_layout);
                //View v = getWindow().getDecorView();
                //v.setBackgroundResource(android.R.color.transparent);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.getWindow().setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);


                Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        //create_databean_list a=new create_databean_list();

                        Intent next1 = new Intent(MainActivity.this, collect_data_activity.class);
                        startActivity(next1);
                        dialog.dismiss();
                    }
                });

                dialog.show();

               // dialog.getWindow().setAttributes(lp);


                break;

        }


    }


}
