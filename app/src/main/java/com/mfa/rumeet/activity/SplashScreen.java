package com.mfa.rumeet.activity;

import android.content.Intent;

import com.daimajia.androidanimations.library.Techniques;
import com.mfa.rumeet.R;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class SplashScreen extends AwesomeSplash {
    @Override
    public void initSplash(ConfigSplash configSplash) {
        //menambahkan background
        configSplash.setBackgroundColor(R.color.colorPrimary);
        configSplash.setAnimCircularRevealDuration(3000);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagX(Flags.REVEAL_BOTTOM);

        //menambahkan logo
        configSplash.setLogoSplash(R.drawable.logo_splash);
        configSplash.setAnimLogoSplashDuration(2000);
        configSplash.setAnimTitleTechnique(Techniques.FadeIn);

        //menambahkan title
        configSplash.setTitleSplash("Rumeet");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(40f);
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.FadeIn);
//        configSplash.setTitleFont("raw/nixmat.ttf");
    }

    @Override
    public void animationsFinished() {
        Intent a = new Intent(SplashScreen.this, LoginActivity.class);
        startActivity(a);
        finish();
    }
}
