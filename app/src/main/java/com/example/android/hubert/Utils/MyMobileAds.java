package com.example.android.hubert.Utils;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MyMobileAds {

    public static void loadAdIntoAdView(Context context, AdView adView ){

        MobileAds.initialize(context, "ca-app-pub-2753645322508525~9181318898");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
