package com.example.user.datascienceapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.lang.*;

import java.*;

/**
 * Created by USER on 24-09-2016.
 */
public class create_databean_list {
    static ArrayList<DataBean> list1 = new ArrayList<DataBean>();

    static int image_id[] = {R.drawable.rating_pic_1, R.drawable.rating_pic_2, R.drawable.rating_pic_3, R.drawable.rating_pic_4, R.drawable.rating_pic_5, R.drawable.rating_pic_6,
            R.drawable.rating_pic_7, R.drawable.rating_pic_8, R.drawable.rating_pic_9, R.drawable.rating_pic_10, R.drawable.rating_pic_11, R.drawable.rating_pic_12, R.drawable.rating_pic_13, R.drawable.rating_pic_14, R.drawable.rating_pic_15, R.drawable.rating_pic_16
            , R.drawable.rating_pic_17, R.drawable.rating_pic_18, R.drawable.rating_pic_19, R.drawable.rating_pic_20, R.drawable.rating_pic_21, R.drawable.rating_pic_22, R.drawable.rating_pic_23, R.drawable.rating_pic_24, R.drawable.rating_pic_25, R.drawable.rating_pic_26, R.drawable.rating_pic_27, R.drawable.rating_pic_28
            , R.drawable.rating_pic_29, R.drawable.rating_pic_30, R.drawable.rating_pic_31, R.drawable.rating_pic_32, R.drawable.rating_pic_33, R.drawable.rating_pic_34, R.drawable.rating_pic_35, R.drawable.rating_pic_36
            , R.drawable.rating_pic_37, R.drawable.rating_pic_38, R.drawable.rating_pic_39, R.drawable.rating_pic_40, R.drawable.rating_pic_41, R.drawable.rating_pic_42, R.drawable.rating_pic_43, R.drawable.rating_pic_44, R.drawable.rating_pic_45, R.drawable.rating_pic_46
            , R.drawable.rating_pic_47, R.drawable.rating_pic_48, R.drawable.rating_pic_49, R.drawable.rating_pic_50, R.drawable.rating_pic_51, R.drawable.rating_pic_52, R.drawable.rating_pic_53, R.drawable.rating_pic_54, R.drawable.rating_pic_55, R.drawable.rating_pic_56};


    static String name[] = {"NEERAJA", "SEETARAM", "TANMAY", "MANJULA", "RADHIKA", "SHILPI", "RAMLAL", "ATMARAM", "SURENDRA", "SEETA", "AARYAN", "VAAMIKA",
            "AYAAN", "MOUNA", "SEETARAM", "RADHIKA", "AYUSHMAN", "MADHAV", "MAHENDRA", "SAANVI", "DEVIKA", "AKKSHAY", "MAITHILI", "SUNEETA",
            "NEERAJA", "MANJULA", "RAMLAL", "NARENDRA", "JANAKI", "AASTHA", "ATMARAM", "AISHWARYA", "TANMAY", "SHILPI", "SAMARTH", "YATHARTH"
            , "RITU", "GANGARAM", "PARIKSHIT", "VIJAYSHREE", "KANIKA", "VAIJAYANTI", "PRETTI", "ANKUR", "SURYANARAYAN", "TAPESHWAR", "GAJODHAR", "RAUNAK"
            , "RICHA", "ADHARSH", "SAMHITA", "AMIT", "CHANCHAL", "SAROJA", "KHUSHBhU", "SULEKHA"};


    static String game[] = {"LIKES PLAYING MUSICAL INSTRUMENTS", "LIKES STITCHING CLOTHES", "LIKES ARCHERY", "LIKES GYMNASTICS", "LIKES TO PLAY TENNIS", "LIKES COLLECTING MARBELS"
            , "LIKES DANCING", "LIKES TO PLAY BASKET-BALL", "LIKES SKETCHING", "LIKES TO SING", "LIKES PLAYING BADMINTON", "LIKES GARDENING", "LIKES REPAIRING WATCHES",
            "LIKES DECORATING WATCHES", "LIKES STITCHING CLOTHES", "LIKES MARTIAL ARTS", "LIKES CALLIGRAPHY", "LIKES TO SWIM", "LIKES ROCK CLIMBING", "LIKES CYCLING", "LIKES PLAYING SNOOKER",
            "LIKES SKIPPING", "LIKES PLAYING HOCKEY", "LIKES MAKING CLAY POTS", "LIKES PLAYING MUSICAL INSTRUMENTS", "LIKES GYMNASTICS", "LIKES DANCING", "LIKES WRITING", "LIKES PLAYING CRICKET", "LIKES PAINTING", "LIKES RUNNING",
            "LIKES PLAYING SQUASH", "LIKES ARCHERY", "LIKES COLLECTING MARBELS", "LIKES PENCIL SHADING", "LIKES TO BOX", "LIKES KARATE", "LIKES SKIING", "LIKES COLLECTING COINS", "LIKES COLOURING",
            "LIKES READING", "LIKES DEBATING", "LIKES PLAYING BASKETBALL", "LIKES PLAYING VOLLEYBALL", "LIKES JUDO", "LIKES SPRAY PAINTING", "LIKES COOKING", "LIKES JOGGING", "LIKES PLAYING TENNIS",
            "LIKES ARRANGING FLOWERS", "LIKES FOLLOWING THE NEWS", "LIKES COLLECTING STAMPS", "LIKES PLAYING FOOTBALL", "LIKES PLAYING ICE HOCKEY", "LIKES PLAYING BASKETBALL", "LIKES LEARNING LANGUAGES"};


    static String gender[] = {"FEMALE", "MALE", "MALE", "FEMALE", "FEMALE", "FEMALE", "MALE", "MALE", "MALE", "FEMALE", "MALE", "FEMALE", "MALE", "FEMALE", "MALE",
            "FEMALE", "MALE", "MALE", "MALE", "FEMALE", "FEMALE", "MALE", "FEMALE", "FEMALE", "FEMALE", "FEMALE", "MALE", "MALE", "FEMALE", "FEMALE", "MALE", "FEMALE",
            "MALE", "FEMALE", "MALE", "MALE", "FEMALE", "MALE", "MALE", "FEMALE", "FEMALE", "FEMALE", "FEMALE", "MALE", "MALE", "MALE", "MALE", "MALE",
            "FEMALE", "MALE", "FEMALE", "MALE", "MALE", "FEMALE", "FEMALE", "FEMALE"};

    public static ArrayList<DataBean> create_list() {
        int i = 0;
        if (list1.size() == 0) {
            for (int l = 1; l <= image_id.length; l++) {
                DataBean obj = new DataBean();
                obj.setId(l);

                //Log.e("ge",img+"");
                obj.setImage(image_id[i]);
                obj.setGender(gender[i]);
                obj.setName(name[i]);
                obj.setGame(game[i++]);
                list1.add(obj);
            }
        }
        return list1;

    }
}
