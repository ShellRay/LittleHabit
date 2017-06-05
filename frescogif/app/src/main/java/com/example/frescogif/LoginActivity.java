package com.example.frescogif;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by GG on 2017/5/16.
 */
public class LoginActivity extends AppCompatActivity{

    private EditText etUserName;
    private EditText etPassword;
    private Button btLogin;
    private TextView tvAnimUsername;
    private TextView tvAnimPsw;
    private boolean tagPhone = true;
    private boolean tagPsw = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUserName = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_pwd);
        btLogin = (Button) findViewById(R.id.btn_login);
        tvAnimUsername = (TextView) findViewById(R.id.tv_anim_username);
        tvAnimPsw = (TextView) findViewById(R.id.tv_anim_pwd);
        initView();
    }

    private void initView() {
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
    }
}
