package com.example.frescogif.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.view.ValidateView;

/**
 * Created by GG on 2017/12/22.
 *
 * 验证码
 */

public class ValidCodeActivity extends BaseActivity{

    private ValidateView valida;
    private static final int LENGHT = 4;//验证码的长度
    private String[] code = new String[LENGHT];//验证码
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        valida = (ValidateView)findViewById(R.id.valida);
        valida.setValidateCodeLenght(LENGHT);
        valida.setPointNum(10);
        valida.setLineNum(3);
        valida.createAndSetValidateCode();
        valida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 code = valida.createAndSetValidateCode();
                valida.invaliChenkNum();
            }
        });
    }
}
