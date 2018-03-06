package com.example.frescogif.view.citychocie;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.frescogif.R;
import com.example.frescogif.constant.Constant;
import com.example.frescogif.view.timeChioce.ScreenInfo;
import com.example.frescogif.view.timeChioce.WheelMain;
import com.example.frescogif.view.timeChioce.WheelTimeView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义dialog for地区选择
 * Created by ShellRay on 2016/5/29.
 */
public class CustomerTimeDialog extends Dialog {
    public CustomerTimeDialog(Context context) {
        super(context);
    }

    public CustomerTimeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder implements OnWheelChangedListener, View.OnClickListener {
        private Context context;
        private OnClickListener onDialogListener;
        private View layoutView;
        private CustomerTimeDialog dialog;
        private int mYear, mMonth, mDay;
        private WheelMain wheelMainDate;


        public Builder(Context context) {
            this.context = context;
        }


        /**
         * Set the positive button resource and it's listener
         *
         * @param
         * @return
         */
        public Builder setOnDialogListener(OnClickListener listener) {
            this.onDialogListener = listener;
            return this;
        }

        public CustomerTimeDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            dialog = new CustomerTimeDialog(context, R.style.giftDialog);
            layoutView = inflater.inflate(R.layout.time_dialog_bottom_layout, null);
            dialog.addContentView(layoutView, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layoutView);

             layoutView.findViewById(R.id.btn_confirm).setOnClickListener(this);
             layoutView.findViewById(R.id.btn_cancle).setOnClickListener(this);
//            View menuView = LayoutInflater.from(this).inflate(R.layout.show_popup_window,null);

            ScreenInfo screenInfoDate = new ScreenInfo((Activity) context);
            wheelMainDate = new WheelMain(layoutView, true);
            wheelMainDate.screenheight = screenInfoDate.getHeight();
//            String time = DateUtils.currentMonth().toString();
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            wheelMainDate.initDateTimePicker(year, month, day, hours, minute);

            return dialog;
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_confirm:
                    String time = wheelMainDate.getTime();
                    Constant.displayTime = time;
                    onDialogListener.onClick(dialog, BUTTON_NEGATIVE);
                    break;
                case R.id.btn_cancle:
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }


        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {

        }

    }
}
