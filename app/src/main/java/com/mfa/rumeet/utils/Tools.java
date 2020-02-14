package com.mfa.rumeet.utils;


import android.support.v4.widget.NestedScrollView;
import android.view.View;


public class Tools {


    public static void nestedScrollTo(final NestedScrollView nested, final View targetView) {
        nested.post(new Runnable() {
            @Override
            public void run() {
                nested.scrollTo(500, targetView.getBottom());
            }
        });
    }

}
