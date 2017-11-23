package com.example.frescogif.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.view.rotateMenu.CircleImageView;
import com.example.frescogif.view.rotateMenu.CircleLayout;

/**
 * Created by GG on 2017/10/30.
 */
public class RotateMenuActivity extends BaseActivity implements CircleLayout.OnItemSelectedListener, CircleLayout.OnItemClickListener, View.OnClickListener {

    TextView selectedTextView;
    private Button btnClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_menue);

        CircleLayout circleMenu = (CircleLayout)findViewById(R.id.main_circle_layout);
        circleMenu.setOnItemSelectedListener(this);
        circleMenu.setOnItemClickListener(this);

        selectedTextView = (TextView)findViewById(R.id.main_selected_textView);
        selectedTextView.setText(((CircleImageView)circleMenu.getSelectedItem()).getName());

        btnClose = (Button)findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(View view, int position, long id, String name) {
        selectedTextView.setText(name);
    }

    @Override
    public void onItemClick(View view, int position, long id, String name) {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.drawer_open) + " " + name, Toast.LENGTH_SHORT).show();
        switch ((int) id){
            case R.id.main_facebook_image://snapHelper
                Intent intent = new Intent(this, SnapHelperLayoutActivity.class);
                startActivity(intent);
                break;
            case R.id.main_myspace_image:

                break;
            case R.id.main_google_image:

                break;
            case R.id.main_linkedin_image:

                break;
            case R.id.main_twitter_image:

                break;
            case R.id.main_wordpress_image:

                break;
            case R.id.main_myspace_1:

                break;
            case R.id.main_myspace_2:

                break;
        }

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
