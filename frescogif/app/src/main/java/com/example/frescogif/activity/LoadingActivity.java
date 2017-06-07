package com.example.frescogif.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.frescogif.R;
import com.example.frescogif.view.loadingview.Titanic;
import com.example.frescogif.view.loadingview.TitanicTextView;
import com.example.frescogif.view.loadingview.Typefaces;

/**
 * Created by GG on 2017/6/7.
 */
public class LoadingActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
            TitanicTextView tv = (TitanicTextView) findViewById(R.id.my_text_view);

            // set fancy typeface
            tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));

        // start animation
        new Titanic().start(tv);

    }
}
