package com.example.frescogif.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.frescogif.R;
import com.example.frescogif.adapter.CustomAdapter;
import com.example.frescogif.adapter.NormalAdapter;
import com.example.frescogif.constant.Constant;
import com.example.frescogif.layoutmanager.SignLayoutManager;
import com.example.frescogif.utils.Utils;
import com.example.frescogif.view.citychocie.CustomerAddressDialog;
import com.example.frescogif.view.citychocie.CustomerTimeDialog;
import com.example.frescogif.view.weekanim.MetaballDebugView;
import com.example.frescogif.view.weekanim.MetaballView;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by GG on 2017/12/18.
 */

public class CityActivity extends AppCompatActivity {

    int mYear, mMonth, mDay;
    TextView dateDisplay;
    final int DATE_DIALOG = 1;

    private TextView city_text;
    private RecyclerView recycler;
    int initPosition =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        city_text = (TextView) findViewById(R.id.city_text);
        dateDisplay = (TextView) findViewById(R.id.dateDisplay);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        List list = new ArrayList();

        list.add("邻家小妹");
        list.add("情感专家");
        list.add("青川美女");
        list.add("宅男女神");

        list.add("气质美女");
        list.add("长发飘飘");
        list.add("大眼睛");
        list.add("女孩子");

        list.add("妩媚性感");
        list.add("清纯萝莉");
        list.add("萌妹子");
        list.add("时尚御姐");

        list.add("妩媚性感");
        list.add("清纯萝莉");
        list.add("萌妹子");
        list.add("时尚御姐");

        list.add("妩媚性感");
        list.add("清纯萝莉");
        list.add("萌妹子");
        list.add("时尚御姐");

        list.add("妩媚性感");
        list.add("清纯萝莉");
        list.add("萌妹子");
        list.add("时尚御姐");

        list.add("妩媚性感");
        list.add("清纯萝莉");
        list.add("萌妹子");
        list.add("时尚御姐");
        /*FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.CENTER);*/
//        layoutManager.setAlignItems(AlignItems.FLEX_START);

        SignLayoutManager signLayoutManager = new SignLayoutManager(4, Utils.convertDpToPixel(CityActivity.this, 20));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        recycler.setLayoutManager(gridLayoutManager);
        NormalAdapter customAdapter = new NormalAdapter(this, list);

//        recycler.addItemDecoration(new SpacesItemDecoration(20));
//        recycler.addItemDecoration(new GridSpacingItemDecoration(4,20,true));

        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                int left = Utils.convertDpToPixel(CityActivity.this,20);
                int kerning = Utils.convertDpToPixel(CityActivity.this,15);

                if(childAdapterPosition%4 == 0){
//                    int initPosition = Constant.initPosition;
//                    GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
                    switch (initPosition %4){
                        case 0:
                            left = Utils.convertDpToPixel(CityActivity.this,30);
                            break;
                        case 1:
                            left = Utils.convertDpToPixel(CityActivity.this,45);
                            break;
                        case 2:
                            left = Utils.convertDpToPixel(CityActivity.this,13);
                            break;
                        case 3:
                            left = Utils.convertDpToPixel(CityActivity.this,49);
                            break;
                    }
                    initPosition ++;
                    outRect.set(left, 0, 0, kerning);

                }else{
                    outRect.set(left, 0, 0, kerning);
                }
                Log.e("random","childAdapterPosition = "+childAdapterPosition +"----initPosition" + initPosition + "++++left" + left);
            }
        });

        recycler.setAdapter(customAdapter);
    }


    public void clickDialog(View view){

        CustomerAddressDialog.Builder builder = new CustomerAddressDialog.Builder(this);
        builder.setOnDialogListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                city_text.setTextColor(Color.BLACK);
                city_text.setText(Constant.CityInfo.PROVICE_NAME + " " + Constant.CityInfo.CITY_NAME + " " + Constant.CityInfo.DISTRICT_NAME);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void clickDialogTime(View view){

        showDialog(DATE_DIALOG);
    }

    public void clickDialogTimeCus(View view){
        CustomerTimeDialog.Builder builder = new CustomerTimeDialog.Builder(this);
        builder.setOnDialogListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dateDisplay.setTextColor(Color.RED);
                dateDisplay.setText(Constant.displayTime);
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    public void clickTextCus(View view) {


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this,R.style.MyDatePickerDialogTheme, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        dateDisplay.setText(new StringBuffer().append(mMonth + 1).append("-").append(mDay).append("-").append(mYear).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
        }
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }


}