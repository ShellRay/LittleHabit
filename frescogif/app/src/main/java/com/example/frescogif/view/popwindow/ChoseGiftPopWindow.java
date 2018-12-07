package com.example.frescogif.view.popwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.example.frescogif.R;
import com.example.frescogif.utils.MediaUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GG on 2017/11/10.
 */
public class ChoseGiftPopWindow extends PopupWindow {
    private final Context context;
    private ListView listView;
    private View contentView;
    private List<GiftCountBean> strDatas = new ArrayList<GiftCountBean>();
    private AdapterView.OnItemClickListener listener;
    private int toPx;

    public ChoseGiftPopWindow(Context context){
        this.context = context;
        contentView = LayoutInflater.from(context).inflate(R.layout.popw_chose_item_view, null);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的宽
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        strDatas.clear();
        listView= contentView.findViewById(R.id.listView);

    }

    /**
     *
     * @param anchorView  呼出window的view
     * @return window显示的左上角的xOff,yOff坐标
     */
    public void calculatePopWindowPos(TextView anchorView, View fatherView, int rootHeight) {
         int anchorLoc[] = new int[2];
        anchorView.getLocationOnScreen(anchorLoc);
        int listHeight = getListHeight(listView);
        showAtLocation(anchorView, Gravity.NO_GRAVITY,anchorLoc[0] ,
                MediaUtils.getScreenWH(context).heightPixels - listHeight  - fatherView.getHeight() - 46);

//        - toPx/2 + 26,
    }

    /**
     *
     * @param anchorView  呼出window的view
     * @return window显示的左上角的xOff,yOff坐标
     */
    public void calculatePopForRecharge(TextView anchorView,View fatherView, int rootHeight) {
        int listHeight = getListHeight(listView);
        showAtLocation(anchorView, Gravity.NO_GRAVITY, MediaUtils.getScreenWH(context).widthPixels - 2 * anchorView.getHeight() - 50,
                rootHeight - listHeight - fatherView.getMeasuredHeight() + anchorView.getHeight() );

    }

    @SuppressLint("NewApi")
    public static int getListHeight(ListView list) {
        ListAdapter listAdapter = list.getAdapter();
        if (listAdapter == null) {
            return 0; }
        int totalHeight = 0;
        int count = listAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View listItem = listAdapter.getView(i, null, list);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        return totalHeight;
    }

    public void bindData(String[] chooseNames, String[] chooseCounts,boolean recharge) {
        for (int i = 0; i<chooseCounts.length; i++)
        {
            strDatas.add(new GiftCountBean(chooseNames[i],chooseCounts[i]));
        }
        SimpleAdapter adapter = new SimpleAdapter(context, R.layout.simple_list_double_item, strDatas);
        View inflate = LayoutInflater.from(context).inflate(R.layout.simple_list_item, null);
        TextView tv_simple_text = inflate.findViewById(R.id.tv_simple_text);
        View divideLine = inflate.findViewById(R.id.divideLine);
        tv_simple_text.setGravity(Gravity.CENTER);
        tv_simple_text.setText(recharge?"其他金额":"其他数量");
        divideLine.setVisibility(View.VISIBLE);
        listView.addHeaderView(inflate);
        listView.setHeaderDividersEnabled(false);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onItemClick( adapterView,  view,  i,  l);
            }
        });

        int maxWidth = MediaUtils.getMaxWidth(context, listView);//最大宽度

        toPx = MediaUtils.dip2px(context, recharge?110:100);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.width = toPx;
        listView.setLayoutParams(params);

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        this.listener = listener;
    }
    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT == 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

    public class SimpleAdapter extends ArrayAdapter{
        private final int resourceId;

        public SimpleAdapter(Context context, int textViewResourceId, List<GiftCountBean> strDatas) {
            super(context, textViewResourceId, strDatas);
            resourceId = textViewResourceId;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            GiftCountBean item = (GiftCountBean) getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            TextView tv_simple_text_count = view.findViewById(R.id.tv_simple_text_count);
            TextView tv_simple_text_dec = view.findViewById(R.id.tv_simple_text_dec);
            tv_simple_text_count.setText(item.chooseCount);
            tv_simple_text_dec.setText(item.chooseName);
            return view;
        }
    }

    class GiftCountBean{
        String chooseName;
        String chooseCount;

        public GiftCountBean(String chooseName, String chooseCount) {
            this.chooseName = chooseName;
            this.chooseCount = chooseCount;
        }
    }
}
