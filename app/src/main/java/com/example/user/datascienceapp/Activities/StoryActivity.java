package com.example.user.datascienceapp.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.eftimoff.viewpagertransformers.StackTransformer;
import com.example.user.datascienceapp.R;
import com.example.user.datascienceapp.Wrappers.Story;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;

public class StoryActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static ArrayList<Story> story;
    private int count;
    private Button btnSkip, btnNext,btnPrev;

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
                Intent intent=new Intent(StoryActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

                dialog.dismiss();
                return;
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        setContentView(R.layout.activity_story);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        story = (ArrayList<Story>) getIntent().getSerializableExtra("Story");

        count = story.size();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.notifyDataSetChanged();
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        mViewPager.setPageTransformer(true, new StackTransformer());

        btnSkip = (Button) findViewById(R.id.btn_skip1);
        btnNext = (Button) findViewById(R.id.btn_next1);
        btnPrev = (Button) findViewById(R.id.btn_prev1);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(StoryActivity.this,MainActivity.class);
                startActivity(intent);
                return;
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current = mViewPager.getCurrentItem() + 1;
                if (current < count) {
                    // move to next screen
                    mViewPager.setCurrentItem(current);
                } else {
                    Intent intent=new Intent(StoryActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = mViewPager.getCurrentItem() - 1;
                if (current < count) {
                    // move to previous screen
                    mViewPager.setCurrentItem(current);
                }
            }
        });
    }





    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position == count - 1) {
                // last page. make button text to GOT IT
                btnNext.setText("GOT IT!");
                btnSkip.setVisibility(View.GONE);
            } else {
                btnNext.setText("NEXT");
                btnSkip.setVisibility(View.VISIBLE);

            }
            if (position==0){
                btnPrev.setVisibility(View.GONE);
            }
            else{
                btnPrev.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageSelected(int position) {}
        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        public PlaceholderFragment() {}

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int page = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView;
            rootView = inflater.inflate(R.layout.slider_screen, container, false);

            TextView textView = (TextView) rootView.findViewById(R.id.textView_story);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.image_story);
            if (story.get(page - 1).getText() != null) {
                textView.setText(story.get(page - 1).getText());
            }

            if (story.get(page - 1).isImage()) {
                Log.d("image","here");
                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("story1/slide" + page + ".jpg");
                if (page == 5) {
                    Glide.with(this)
                            .using(new FirebaseImageLoader())
                            .load(mStorageRef)
                            .fitCenter()
                            .override(700, 1200).placeholder(R.drawable.placeholder5).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);
                }
                else if(page==6)
                {
                    Glide.with(this)
                            .using(new FirebaseImageLoader())
                            .load(mStorageRef)
                            .fitCenter()
                            .placeholder(R.drawable.placeholder6).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);
                }
                else if(page==10)
                {
                    Glide.with(this)
                            .using(new FirebaseImageLoader())
                            .load(mStorageRef)
                            .fitCenter()
                            .placeholder(R.drawable.placeholder10).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);
                }else if(page>=12)
                {
                    Glide.with(this)
                            .using(new FirebaseImageLoader())
                            .load(mStorageRef)
                            .fitCenter()
                            .placeholder(R.drawable.placeholder2).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);
                }
                else
                    Glide.with(this)
                            .using(new FirebaseImageLoader())
                            .load(mStorageRef).placeholder(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .fitCenter()
                            .into(imageView);

            }


            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public int pages = count;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return pages;
        }


    }
}
