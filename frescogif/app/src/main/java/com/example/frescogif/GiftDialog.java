package com.example.frescogif;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.frescogif.bean.GiftDialogBean;

import java.util.List;


public class GiftDialog extends Dialog implements PageGridView.OnPageChangeListener, AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String TAG = "GiftDialog";
    private  Context context;
    private  List<GiftDialogBean> list;

    //控件定义
    private Button       sendGiftBttn; //发礼物按钮
    private PageGridView pageGridView;//分页九宫格视图
    private PageMarkView pageMarkView; //礼物页码指示器
    private TextView     loadingText;//礼物列表还未获取到时显示的加载中界面

    private TextView    userCoinText;//齐齐豆余额
    private Button      rechargeBttn;//去充值

    private LayoutInflater  inflater;
    private LiveGiftAdapter giftAdapter;


    public GiftDialog(Activity context,List<GiftDialogBean> list)
    {
        super(context, R.style.giftDialog);
        this.list  = list;
        this.context = context;
        inflater = context.getLayoutInflater();
        View contentView = inflater.inflate(R.layout.li_dialog_gift, null);
        setContentView(contentView);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        params.dimAmount = 0;
        params.alpha = 1;
        getWindow().setAttributes(params);
        intializeView();


        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
    }

    //初始化View
    private void intializeView()
    {
        sendGiftBttn = (Button) findViewById(R.id.sendGiftBttn);
        loadingText = (TextView) findViewById(R.id.loadingText);
        pageGridView = (PageGridView) findViewById(R.id.pageGridView);
        pageMarkView = (PageMarkView) findViewById(R.id.pageMarkView);
        userCoinText = (TextView) findViewById(R.id.userCoinText);
        rechargeBttn = (Button) findViewById(R.id.rechargeBttn);

        sendGiftBttn.setOnClickListener(this);

        giftAdapter = new LiveGiftAdapter(context,list, inflater);
        pageGridView.setAdapter(giftAdapter);

        int pageCount = pageGridView.getPageCount();
        int visibility = pageCount > 1 ? View.VISIBLE : View.GONE;
        pageMarkView.setVisibility(visibility);
        pageMarkView.setPageInfo(pageCount, 0);

        pageGridView.setOnPageChangeListener(this);
        pageGridView.setOnItemClickListener(this);

    }

    /**
     * 礼物列表返回
     * 打开礼物面板时请求，在RoomActivity.java中调用
     *
     * @param giftlist
     */
    public void refreshGiftList(List giftlist)
    {
//        liveGiftList = giftlist;

        giftAdapter = new LiveGiftAdapter(context, list, inflater);
        pageGridView.setAdapter(giftAdapter);

        int pageCount = pageGridView.getPageCount();
        int visibility = pageCount > 1 ? View.VISIBLE : View.GONE;
        pageMarkView.setVisibility(visibility);
        pageMarkView.setPageInfo(pageCount, 0);

    }

    @Override
    public void onPageChange(int page)
    {
        pageMarkView.setPageIndex(page);

        if (giftAdapter != null)
        {
            int selected = giftAdapter.getSelected();
            View view = pageGridView.getItemView(selected);
            giftAdapter.cancleSelect(view);
        }
    }

    int[] numbers = new int[]{1, 99, 199, 520, 999, 1314, 9999};




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        int selected = giftAdapter.getSelected();
        if (selected != position && selected > -1)
        {
            //如果position发生变化，先取消选中
            View child = pageGridView.getItemView(selected);
            giftAdapter.cancleSelect(child);
        }

//        Gift gift = list.get(position);
        int clicknum = giftAdapter.getClicknum();




        int index = -1, length = numbers.length;

        for (int i = 0; i < length; i++)
        {
            if (numbers[i] == clicknum)
            {
                index = i;
                break;
            }
        }

        index++;

        if (index >= length)
        {
            index = 0;
        }

        clicknum = numbers[index];
        View child = pageGridView.getItemView(position);
        giftAdapter.updateSelect(child, position, clicknum);
    }

    //
    public void show()
    {
        //每次打开,再次刷新一下余额
        super.show();
    }

    /**
     * 获取当前时间转化成时间戳
     */
    public static String timeStamp()
    {
        long time = System.currentTimeMillis();
        String t = String.valueOf(time / 1000);
        return t;
    }

    //设置余额
    public void setCoin(String coin)
    {

        Context context = getContext();

        if (context != null && !TextUtils.isEmpty(coin))
        {
            if (userCoinText != null)
            {
                double dcoin = Double.parseDouble(coin);
                if (dcoin < 1)
                {
                    dcoin = 0;
                }
                userCoinText.setText((long) dcoin + "");
            }
        }
    }

    @Override
    public void dismiss()
    {
        getWindow().setWindowAnimations(R.style.dialogAnim);

        //取消选中的礼物
        if (giftAdapter != null)
        {
            int selected = giftAdapter.getSelected();
            View view = pageGridView.getItemView(selected);
            giftAdapter.cancleSelect(view);
        }

        super.dismiss();
    }



    private void showToast(String msg)
    {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendGiftBttn:
                int positon = giftAdapter.getSelected();
                if(positon == -1){
                    return;
                }
                --list.get(positon).giftNum ;
                boolean b = list.get(positon).giftNum == 0;
                if(b){
                    list.remove(list.get(positon));
                }

               /* for(int i = 0; i <list.size() ; i ++){
                    if(i == positon){
                        --list.get(i).giftNum ;
                        if(list.get(i).giftNum == 0){
                            list.remove(list.get(i));
                        }
                    }                }*/
                giftAdapter.notifyDataSetChanged();
//                giftAdapter.cancleSelect(pageGridView.getItemView(positon));
                int pageCount = pageGridView.getPageCount();
                int visibility = pageCount > 1 ? View.VISIBLE : View.GONE;
                pageMarkView.setVisibility(visibility);
                pageMarkView.setPageInfo(pageCount, 0);

                if(b){
                    giftAdapter.cancleSelect(pageGridView.getItemView(positon));
                }else {
                    View child = pageGridView.getItemView(positon);
                    giftAdapter.updateSelect(child, positon, 1);
                }
                break;
        }
    }
}
