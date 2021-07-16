package com.example.frescogif.view.keybaorddrag;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.effective.android.panel.PanelSwitchHelper;
import com.effective.android.panel.interfaces.ContentScrollMeasurer;
import com.effective.android.panel.interfaces.PanelHeightMeasurer;
import com.effective.android.panel.interfaces.listener.OnPanelChangeListener;
import com.effective.android.panel.utils.PanelUtil;
import com.effective.android.panel.view.panel.IPanelView;
import com.effective.android.panel.view.panel.PanelView;
import com.effective.android.panel.window.PanelDialog;
import com.example.frescogif.R;
import com.example.frescogif.view.behavior.BottomSheetBehavior;
import com.example.frescogif.view.emotion.EmotionPagerView;
import com.example.frescogif.view.emotion.Emotions;
import com.example.frescogif.view.guide.util.ScreenUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FeedCommentDialog extends PanelDialog implements DialogInterface.OnKeyListener {

    private static final String TAG = FeedCommentDialog.class.getSimpleName();
    private Activity activity;

    private BottomSheetBehavior<?> bottomSheetBehavior;
    private final int center_height;
    private final EditText edit_text;

    @Override
    public int getDialogLayout() {
        return R.layout.dialog_feed_comment_layout;
    }

    public FeedCommentDialog(Activity activity) {
        super(activity);
        //折叠高度
        center_height = (int) (ScreenUtils.getScreenHeight(getContext()) * 0.75);

        this.activity = activity;
        setCanceledOnTouchOutside(true);
        setOnKeyListener(this);

        edit_text = ((EditText) rootView.findViewById(R.id.edit_text));
        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                rootView.findViewById(R.id.send).setEnabled(s.length() != 0);
            }
        });
        rootView.findViewById(R.id.send).setOnClickListener(v ->
                {
                    showCollapsedPop();
                    edit_text.setText("");

                }
        );


        rootView.findViewById(R.id.fill_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCollapsedPop();
                dismiss();
            }
        });

        rootView.findViewById(R.id.input_layout).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

            }
        });

        initBottomSheetBehavior();
    }

    //展示成中间屏幕高度
    private void showCollapsedPop() {

        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED
                && bottomSheetBehavior.getPeekHeight() > 0) return;

        //设置折叠高度
        if (bottomSheetBehavior.getPeekHeight() == 0) {
            bottomSheetBehavior.setPeekHeight(center_height);
        }

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    //隐藏状态
    private void hideCollapsedPop() {

        if (bottomSheetBehavior.getPeekHeight() > 0) bottomSheetBehavior.setPeekHeight(0);

        if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onBackPressed() {
        int state = bottomSheetBehavior.getState();
        if (state == AnchorSheetBehavior.STATE_COLLAPSED || state == AnchorSheetBehavior.STATE_HIDDEN) {
            super.onBackPressed();
        } else {
            bottomSheetBehavior.setState(AnchorSheetBehavior.STATE_COLLAPSED);
        }
    }

    //初始化折叠相关操作
    private void initBottomSheetBehavior() {

        //将Behavior与布局关联
        bottomSheetBehavior = BottomSheetBehavior.from(rootView.findViewById(R.id.anchor_panel));
        //设置折叠高度
        bottomSheetBehavior.setPeekHeight(center_height);

        //        //设置折叠关闭状态，会直接弹起/实际交互不应该默认弹起/默认注释
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onSlide(@NotNull View bottomSheet, float slideOffset) {
            }

            @Override
            public void onStateChanged(@NotNull View bottomSheet, int newState) {
                /**
                 * TODO 通过状态可以处理，关闭折叠状态时候，通过修改折叠高度为0，直接隐藏列表布局：如下
                 * TODO 注意折叠制为0后，需要判断恢复折叠高度
                 */
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    PanelUtil.hideKeyboard(getContext(), edit_text);
                    if (bottomSheetBehavior.getPeekHeight() > 0) {
                        bottomSheetBehavior.setPeekHeight(0);
                    }
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    PanelUtil.showKeyboard(getContext(), edit_text);
                }

            }
        });
    }

    @Override
    public void show() {
        if (helper == null) {
            helper = new PanelSwitchHelper.Builder(activity.getWindow(), rootView)
                    //可选
                    .addKeyboardStateListener((visible, height) -> Log.d(TAG, "系统键盘是否可见 : " + visible + " 高度为：" + height))
                    //可选
                    .addPanelChangeListener(new OnPanelChangeListener() {

                        @Override
                        public void onKeyboard() {
                            Log.d(TAG, "唤起系统输入法");
                            rootView.findViewById(R.id.emotion_btn).setSelected(false);
                            //设置折叠高度 从全展开状态唤起输入法则变为折叠状态
                            if (bottomSheetBehavior.getPeekHeight() == 0
                                    && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                                bottomSheetBehavior.setPeekHeight(center_height);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            }
                        }

                        @Override
                        public void onNone() {
                            Log.d(TAG, "隐藏所有面板");
                            rootView.findViewById(R.id.emotion_btn).setSelected(false);
//                            dismiss();
                        }

                        @Override
                        public void onPanel(IPanelView view) {
                            Log.d(TAG, "唤起面板 : " + view);
                            if (view instanceof PanelView) {
                                rootView.findViewById(R.id.emotion_btn).setSelected(((PanelView) view).getId() == R.id.panel_emotion ? true : false);
                                hideCollapsedPop();
                            }
                        }


                        @Override
                        public void onPanelSizeChange(IPanelView panelView, boolean portrait, int oldWidth, int oldHeight, int width, int height) {
                            if (panelView instanceof PanelView) {
                                switch (((PanelView) panelView).getId()) {
                                    case R.id.panel_emotion: {
                                        EmotionPagerView pagerView = rootView.findViewById(R.id.view_pager);
                                        int viewPagerSize = height - ScreenUtils.dp2px(getContext(), 30);
                                        pagerView.buildEmotionViews(
                                                rootView.findViewById(R.id.pageIndicatorView),
                                                rootView.findViewById(R.id.edit_text),
                                                Emotions.getEmotions(), width, viewPagerSize);
                                        break;
                                    }
                                }
                            }
                        }
                    })

                    /**
                     * tipViewTop 不跟随滑动
                     */
                    .addContentScrollMeasurer(new ContentScrollMeasurer() {
                        @Override
                        public int getScrollDistance(int i) {
                            return 0;
                        }

                        @Override
                        public int getScrollViewId() {
                            return R.id.coordinator;
                        }
                    })
                    .logTrack(true)
                    .build(true);
        }
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public boolean onKey(@Nullable DialogInterface dialog, int keyCode, @NotNull KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }
        return false;
    }

}
