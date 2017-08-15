package com.example.a1.himaster;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by a1 on 2017. 8. 15..
 */

public class BaseActivity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/kopubdotumbold.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/kopubdotumlight.ttf"))
                .addCustom1(Typekit.createFromAsset(this, "fonts/kopubdotumbold.ttf"));// "fonts/폰트.ttf"
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}