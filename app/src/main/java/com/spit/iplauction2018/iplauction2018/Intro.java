package com.spit.iplauction2018.iplauction2018;

import android.Manifest;
import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class Intro extends IntroActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        addSlide(new SimpleSlide.Builder()
//                .title(R.string.app_name)
//                .description("This is IPL auction... Take this mf")
//                .image(R.mipmap.ipl_foreground)
//                .background(R.color.colorPrimary)
//                .backgroundDark(R.color.colorPrimaryDark)
        @Override protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addSlide(new SimpleSlide.Builder()
                .title(R.string.app_name)
                .description("This is IPL auction... Take this mf")
//                .image(R.mipmap.ipl_foreground)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());






            // Add slides, edit configuration...
        }
//                .permission(Manifest.permission.CAMERA)
//                .build());
//        addSlide(new FragmentSlide.Builder()
//                .background(R.color.colorPrimary)
//                .backgroundDark(R.color.colorPrimaryDark)
//                .fragment(R.layout.nav_header_main)
//                .build());


    }
