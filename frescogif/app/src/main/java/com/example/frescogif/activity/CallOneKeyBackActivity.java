package com.example.frescogif.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.effective.android.panel.PanelSwitchHelper;
import com.effective.android.panel.interfaces.listener.OnPanelChangeListener;
import com.effective.android.panel.view.panel.IPanelView;
import com.effective.android.panel.view.panel.PanelView;
import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.view.emotion.EmotionPagerView;
import com.example.frescogif.view.emotion.Emotions;
import com.example.frescogif.view.guide.util.ScreenUtils;
import com.example.frescogif.view.keybaorddrag.FeedCommentDialog;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GG on 2017/12/14.
 */

public class CallOneKeyBackActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CallOneKeyBackActivity";

    @BindView(R.id.video_view)
    VideoView videoView;

    @BindView(R.id.gift)
    ImageView gift;

    @BindView(R.id.action_real_layout)
    RelativeLayout actionRealLayout;

    @BindView(R.id.action_show_layout)
    LinearLayout actionShowLayout;

    @BindView(R.id.close)
    View close;

    @BindView(R.id.input_action)
    TextView inputAction;
    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.emotion_btn)
    ImageView emotionBtn;

    @BindView(R.id.root)
    FrameLayout root;

    private PanelSwitchHelper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_call_one_keyback);
        ButterKnife.bind(this);
        initView();
    }
    
    private void initView() {
        videoView.getLayoutParams().width = ScreenUtils.getScreenWidth(this);
        videoView.getLayoutParams().height = ScreenUtils.getScreenHeight(this);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.yexiaoma));
        videoView.setOnPreparedListener(mp -> {
            mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            mp.start();
            mp.setLooping(true);
        });
        videoView.start();
        gift.setSelected(true);
        actionRealLayout.setVisibility(View.GONE);
        actionShowLayout.setVisibility(View.VISIBLE);
        close.setOnClickListener(v -> finish());
        inputAction.setOnClickListener(v -> {
            actionRealLayout.setVisibility(View.VISIBLE);
            actionShowLayout.setVisibility(View.GONE);
            input.requestFocus();
        });
        gift.setOnClickListener(this);
//        send.setOnClickListener(v -> {
//            mAdapter.insertMessage(new Message("yummylau", input.getText().toString()));
//            input.setText("");
//        });

//        mLinearLayoutManager = new LinearLayoutManager(this);
//        commentList.setLayoutManager(mLinearLayoutManager);
//        LinkedList<Message> messages = new LinkedList<>();
//        for (int i = 0; i < 100; i++) {
//            messages.addFirst(new Message("yummylau" + i, "唱的好好听哦"));
//        }
//        mAdapter = new ChatAdapter(this, messages);
//        commentList.setAdapter(mAdapter);
//        scrollToBottom();
//        handler.postDelayed(insertMessage, 5000);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gift:
                new FeedCommentDialog(this).show();
                break;
        }

    }

//    private void scrollToBottom() {
//        root.post(() -> mLinearLayoutManager.scrollToPosition(mAdapter.getItemCount()-1));
//    }
    
    @Override
    protected void onStart() {
        super.onStart();
        if (mHelper == null) {
            mHelper = new PanelSwitchHelper.Builder(this)
                    //可选
                    .addKeyboardStateListener((visible, height) -> Log.d(TAG, "系统键盘是否可见 : " + visible + " 高度为：" + height))
                    .addEditTextFocusChangeListener((view, hasFocus) -> {
                        Log.d(TAG, "输入框是否获得焦点 : " + hasFocus);
                        if (hasFocus) {
//                            scrollToBottom();
                        }
                    })
                    //可选
                    .addViewClickListener(view -> {
                        switch (view.getId()) {
                            case R.id.edit_text:
                            case R.id.emotion_btn: {
//                                scrollToBottom();
                            }
                        }
                        Log.d(TAG, "点击了View : " + view);
                    })
                    //可选
                    .addPanelChangeListener(new OnPanelChangeListener() {

                        @Override
                        public void onKeyboard() {
                            Log.d(TAG, "唤起系统输入法");
                            emotionBtn.setSelected(false);
//                            scrollToBottom();
                        }

                        @Override
                        public void onNone() {
                            Log.d(TAG, "隐藏所有面板");
                            emotionBtn.setSelected(false);
                            actionRealLayout.setVisibility(View.GONE);
                            actionShowLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onPanel(IPanelView view) {
                            Log.d(TAG, "唤起面板 : " + view);
                            if (view instanceof PanelView) {
                                emotionBtn.setSelected(((PanelView) view).getId() == R.id.panel_emotion ? true : false);
//                                scrollToBottom();
                            }
                        }

                        @Override
                        public void onPanelSizeChange(IPanelView panelView, boolean portrait, int oldWidth, int oldHeight, int width, int height) {
                            if (panelView instanceof PanelView) {
                                switch (((PanelView) panelView).getId()) {
                                    case R.id.panel_emotion: {
                                        EmotionPagerView pagerView = root.findViewById(R.id.view_pager);
                                        int viewPagerSize = height - ScreenUtils.dp2px(CallOneKeyBackActivity.this, 30);
                                        pagerView.buildEmotionViews(
                                                root.findViewById(R.id.pageIndicatorView),
                                                input,
                                                Emotions.getEmotions(), width, viewPagerSize);
                                        break;
                                    }
                                }
                            }
                        }
                    })
                    .logTrack(true)
                    .build();
        }
    }

    @Override
    public void onBackPressed() {
        if (mHelper != null && mHelper.hookSystemBackByPanelSwitcher()) {
            return;
        }
        super.onBackPressed();
    }

}
