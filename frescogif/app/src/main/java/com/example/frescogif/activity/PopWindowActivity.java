package com.example.frescogif.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.utils.Utils;
import com.example.frescogif.view.SimpleAnimater;
import com.example.frescogif.view.popwindow.ChoseGiftPopWindow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;

/**
 * Created by GG on 2018/11/19.
 */

public class PopWindowActivity extends BaseActivity{

    private FrameLayout aView;
    private FrameLayout bView;
    private FrameLayout cView;
    private ChoseGiftPopWindow choseGiftCountPopWindow;
    private String[] chooseNames = new String[]{"一生一世", "我爱你", "长长久久", "三生有幸", "全心全意", "想你...", "一心一意"};
    private String[] chooseCounts = new String[]{"1314", "520", "99", "33", "11", "3", "1"};
    private Button showpop;
    private boolean showA = true;
    private TextView tvAnimateView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popwindow);
        aView = (FrameLayout) findViewById(R.id.a);
        bView = (FrameLayout) findViewById(R.id.b);
        cView = (FrameLayout) findViewById(R.id.c);

        showpop = (Button) findViewById(R.id.showpop);
        tvAnimateView = (TextView) findViewById(R.id.tvAnimateView);

    }

    public void moveview(View view){

        if(showA){
            RelativeLayout.LayoutParams aLayoutParams = (RelativeLayout.LayoutParams) aView.getLayoutParams();
            aLayoutParams.width = Utils.convertDpToPixel(this,80);
            aLayoutParams.height = Utils.convertDpToPixel(this,80);
            aLayoutParams.addRule(ALIGN_PARENT_RIGHT);
            aLayoutParams.addRule(RelativeLayout.ABOVE,R.id.c);

            aView.setLayoutParams(aLayoutParams);
            RelativeLayout.LayoutParams bLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            bView.setLayoutParams(bLayoutParams);

            aView.bringToFront();

            beginDelayedTransition(aView);
            beginDelayedTransition(bView);
            showA = false;
        }else {
            RelativeLayout.LayoutParams aLayoutParams = (RelativeLayout.LayoutParams) bView.getLayoutParams();
            aLayoutParams.width = Utils.convertDpToPixel(this,80);
            aLayoutParams.height = Utils.convertDpToPixel(this,80);
            aLayoutParams.addRule(ALIGN_PARENT_RIGHT);
            aLayoutParams.addRule(RelativeLayout.ABOVE,R.id.c);

            bView.setLayoutParams(aLayoutParams);
            RelativeLayout.LayoutParams bLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            aView.setLayoutParams(bLayoutParams);

            bView.bringToFront();
            showA = true;
            beginDelayedTransition(aView);
            beginDelayedTransition(bView);
        }

        cView.bringToFront();
    }

    public void hashsetChange(View view) {

        User user1 = new User();
        user1.setId(1);
        user1.setName("shangsan");

        User user2 = new User();
        user2.setId(2);
        user2.setName("lisi");

        User user3 = new User();
        user3.setId(3);
        user3.setName("bishi");

        Set userSet = new HashSet();
        userSet.add(user1);
        userSet.add(user2);
        userSet.add(user3);

        List delList = new ArrayList();//方法1 放到缓冲列表
        for (Iterator it = userSet.iterator(); it.hasNext(); ) {
            User user = (User) it.next();
            if (user.getId() == 1) {
//                delList.add(user);
                //方法2 先移除迭代器在操作
                it.remove();
                userSet.remove(user);
            }

            if (user.getId() == 2) {
                user.setName("zhangsan");
            }

        }
//        userSet.removeAll(delList);

        for (Iterator it = userSet.iterator(); it.hasNext(); ) {
            User user = (User) it.next();
            System.out.println(user.getId() + "=>" + user.getName());
        }
    }

    static CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<Integer>();

    public void hashmapChange(View view){

        SimpleAnimater simpleAnimater = new SimpleAnimater();
        simpleAnimater.setDuration(1000);
        tvAnimateView.startAnimation(simpleAnimater);
    }

    public void showpop(View view){
        if (choseGiftCountPopWindow == null) {
            choseGiftCountPopWindow = new ChoseGiftPopWindow(this);
            choseGiftCountPopWindow.bindData(chooseNames, chooseCounts, false);
            choseGiftCountPopWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
        }

        if (!choseGiftCountPopWindow.isShowing()) {
            choseGiftCountPopWindow.calculatePopWindowPos(showpop, showpop, 0);
        } else {
            choseGiftCountPopWindow.dismiss();
        }


    }

    private class User {
        private int id;
        private String name;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    //过渡动画 视图变化的效果
    public void beginDelayedTransition(ViewGroup viewGroup){
        AutoTransition autoTransition = new AutoTransition();
        autoTransition.setDuration(500);
        TransitionManager.beginDelayedTransition(viewGroup,autoTransition);
    }
}
