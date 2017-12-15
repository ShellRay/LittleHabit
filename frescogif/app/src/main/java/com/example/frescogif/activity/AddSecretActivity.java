package com.example.frescogif.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.utils.PackOutput;
import com.example.frescogif.utils.StringCodeUtil;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GG on 2017/12/14.
 */

public class AddSecretActivity extends BaseActivity implements View.OnClickListener {

    private EditText etInput;
    private TextView showText;
    private TextView jimiText;
    private Button addSecret;
    private Button decodSecret;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_seceret);
        initView();
    }

    private void initView() {
        etInput = (EditText) findViewById(R.id.etInput);
        showText = (TextView) findViewById(R.id.showText);
        jimiText = (TextView) findViewById(R.id.jimiText);
        addSecret = (Button) findViewById(R.id.addSecret);
        decodSecret = (Button) findViewById(R.id.decodSecret);

        addSecret.setOnClickListener(this);
        decodSecret.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.decodSecret:
                //解密 二进制字符
                String toString = showText.getText().toString();
                Map<String, Object> stringObjectMap = checkWebLiveMcheck(toString);
                Log.e("jiemi",stringObjectMap.toString());
                Long uid = (Long) stringObjectMap.get("uid");
                jimiText.setText(uid + "");
                break;
            case R.id.addSecret:
                //加密 变成二进制字符
                String input = etInput.getText().toString().trim();
                String webLiveCheck = createWebLiveCheck(input, "123");
                showText.setText(webLiveCheck);
                break;
        }


    }

    public static String createWebLiveCheck(String uid,String roomid)
    {
        byte[] bs = new byte[64];
        PackOutput out = new PackOutput(bs, 0, false);
        long time = System.currentTimeMillis();
        out.writeLong(time);
        out.writeLong(Long.valueOf(uid));
        out.writeLong(time);
        out.writeBytes("guagualive".getBytes());
        String str = "";
        try {
            str = StringCodeUtil.bytesToHexString(out.getBytes());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    public static long byteToLong(byte[] b, boolean isBigEndian)
    {
        long l = 0L;
        for (int i = 0; (i < b.length) && (i < 8); i++) {
            l |= (b[i] & 0xFF) << 8 * (isBigEndian ? 7 - i : i);
        }
        return l;
    }

    public static Map<String, Object> checkWebLiveMcheck(String livecheck)  {
        Map<String,Object> map = new HashMap<String,Object>();
        byte[] crypt = StringCodeUtil.hexStringToBytes(livecheck);
        byte[] bs;
        try {
            ByteBuffer buffer = ByteBuffer.wrap(crypt);

            buffer.getLong();

            byte[] uidbytes = new byte[8];
            buffer.get(uidbytes, 0, 8);
            long uid = byteToLong(uidbytes, false);
            map.put("uid", uid);
            buffer.getLong();

            byte[] verifyBytes = new byte["guagualive".getBytes().length];
            buffer.get(verifyBytes, 0, verifyBytes.length);
            String verifyBaseStr = new String(verifyBytes);
            if (!"guagualive".equals(verifyBaseStr)) {
                return map;
            }
        } catch (Exception e) {
            map.put("uid", 0l);
            //e.printStackTrace();
        }
        return map;
    }


}
