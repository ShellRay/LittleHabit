package com.example.frescogif;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frescogif.activity.RotateMenuActivity;
import com.example.frescogif.view.ProgressButton;

/**
 * Created by GG on 2017/5/16.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUserName;
    private EditText etPassword;
    private ProgressButton btLogin;
    private TextView tvAnimUsername;
    private TextView tvAnimPsw;
    private boolean tagPhone = true;
    private boolean tagPsw = true;
    private ImageView ivLoginBg;
    private ScaleAnimation scaleAnimation1;
    private Button btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUserName = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_pwd);
        btLogin = (ProgressButton) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        tvAnimUsername = (TextView) findViewById(R.id.tv_anim_username);
        tvAnimPsw = (TextView) findViewById(R.id.tv_anim_pwd);
        ivLoginBg = (ImageView) findViewById(R.id.iv_login_bg);
        initView();
        initAnim();
    }

    private void initAnim() {
        if(ivLoginBg.getVisibility() == View.VISIBLE){
            scaleAnimation1 = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation1.setRepeatMode(Animation.REVERSE);
            scaleAnimation1.setRepeatCount(-1);
            scaleAnimation1.setDuration(5000);
            ivLoginBg.startAnimation(scaleAnimation1);
        }
    }

    private void initView() {

        btLogin.setBgColor(getResources().getColor(R.color.colorAccent));
        btLogin.setTextColor(Color.WHITE);
        btLogin.setProColor(Color.WHITE);
        btLogin.setButtonText("Login in");

        etUserName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (tagPhone) {
//                    int distance = DensityUtil.dip2px(LoginActivity.this, 30);
                    tvAnimUsername.setVisibility(View.VISIBLE);
                    TranslateAnimation animationTranslate = new TranslateAnimation(
                            0, 0, 0, -65);
                    animationTranslate.setDuration(200);
                    animationTranslate.setFillAfter(true);
                    tvAnimUsername.startAnimation(animationTranslate);
                    tvAnimUsername.setTextColor(getResources().getColor(
                            R.color.main_color));
                    btLogin.setBackgroundColor(getResources().getColor(
                            R.color.main_color));
                    tagPhone = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(etUserName.getText().toString())) {
                    tvAnimUsername.setTextColor(getResources().getColor(
                            R.color.normal_input_text_color));
                    AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
                    animation.setFillAfter(true);
                    animation.setDuration(1);
                    tvAnimUsername.startAnimation(animation);
                    btLogin.setBackgroundColor(getResources().getColor(
                            R.color.bg_normal_btn_color));
                    tagPhone = true;
                }
            }
        });
        // 输入密码的EditText设置监听内容变化
        etPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (tagPsw) {
//                    int distance = DensityUtil.dip2px(LoginActivity.this, 30);
                    tvAnimPsw.setVisibility(View.VISIBLE);
                    TranslateAnimation animationTranslate = new TranslateAnimation(
                            0, 0, 0, -65);
                    animationTranslate.setDuration(200);
                    animationTranslate.setFillAfter(true);
                    tvAnimPsw.startAnimation(animationTranslate);
                    tvAnimPsw.setTextColor(getResources().getColor(
                            R.color.main_color));
                    tagPsw = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    tvAnimPsw.setTextColor(getResources().getColor(R.color.normal_input_text_color));
                    AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
                    animation.setFillAfter(true);
                    animation.setDuration(1);
                    tvAnimPsw.startAnimation(animation);
                    tagPsw = true;
                }
            }
        });

        btLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                btLogin.startAnim();
                Message m=mHandler.obtainMessage();
                mHandler.sendMessageDelayed(m,1500);

                break;
            case R.id.btn_register:
                startActivity(new Intent(this,RotateMenuActivity.class));
                break;
        }

    }

    private Handler mHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            btLogin.stopAnim(new ProgressButton.OnStopAnim() {
                @Override
                public void Stop() {
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }
            });

        }
    };
}
