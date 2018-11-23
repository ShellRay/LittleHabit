package com.example.frescogif.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;
import com.example.frescogif.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;

import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;

/**
 * Created by GG on 2018/11/19.
 */

public class PopWindowActivity extends BaseActivity{

    private TextView aView;
    private TextView bView;
    private TextView cView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popwindow);
        aView = (TextView) findViewById(R.id.a);
        bView = (TextView) findViewById(R.id.b);
        cView = (TextView) findViewById(R.id.c);

    }

    public void moveview(View view){

        RelativeLayout.LayoutParams aLayoutParams = (RelativeLayout.LayoutParams) aView.getLayoutParams();
        aLayoutParams.width = Utils.convertDpToPixel(this,80);
        aLayoutParams.height = Utils.convertDpToPixel(this,80);
        aLayoutParams.addRule(ALIGN_PARENT_RIGHT);
        aLayoutParams.addRule(RelativeLayout.ABOVE,R.id.c);

        aView.setLayoutParams(aLayoutParams);
        RelativeLayout.LayoutParams bLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        bView.setLayoutParams(bLayoutParams);

        aView.bringToFront();
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

        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        Thread thread1 = new Thread(){
            public void run() {
                Iterator<Integer> iterator = list.iterator();


                    while (iterator.hasNext()) {
                        synchronized(iterator) {
                            Integer integer = iterator.next();
                            System.out.println(integer);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                    }

                }
            };
        };
        Thread thread2 = new Thread(){
            public void run() {
                Iterator<Integer> iterator = list.iterator();

                    while (iterator.hasNext()) {
                        synchronized(iterator) {
                            Integer integer = iterator.next();
                            if (integer == 2)
                                iterator.remove();
                        }
                    }
            };
        };
        thread1.start();
        thread2.start();
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
}
