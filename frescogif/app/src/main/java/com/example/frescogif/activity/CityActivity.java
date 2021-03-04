package com.example.frescogif.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.frescogif.R;
import com.example.frescogif.constant.Constant;
import com.example.frescogif.view.citychocie.CustomerAddressDialog;
import com.example.frescogif.view.citychocie.CustomerTimeDialog;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * Created by GG on 2017/12/18.
 */

public class CityActivity extends AppCompatActivity {

    int mYear, mMonth, mDay;
    TextView dateDisplay;
    final int DATE_DIALOG = 1;

    private TextView city_text;
    private FixedHoloDatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        city_text = (TextView) findViewById(R.id.city_text);
        dateDisplay = (TextView) findViewById(R.id.dateDisplay);

        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        Context themed = new ContextThemeWrapper(this,
                R.style.MyDatePickerDialogTheme);
        //Android 7.0 下我们默认改变theme不能管用，所以需要自定义写一个
        datePickerDialog = new FixedHoloDatePickerDialog(themed, mdateListener, mYear, mMonth, mDay);
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

        datePickerDialog.show();
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

        startActivity(new Intent(this,CusTagActivity.class));

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

    class FixedHoloDatePickerDialog extends DatePickerDialog {

        DatePicker datePicker;
        final Context context;
        private final Calendar mCalendar;

        @SuppressLint("NewApi")
        public FixedHoloDatePickerDialog(Context context,
                                         OnDateSetListener callBack, int year, int monthOfYear,
                                         int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);

            this.context = context;
            mCalendar = Calendar.getInstance();
            // Force spinners on Android 7.0 only (SDK 24).
            // Note: I'm using a naked SDK value of 24 here, because I'm
            // targeting SDK 23, and Build.VERSION_CODES.N is not available yet.
            // But if you target SDK >= 24, you should have it.
            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    final Field field = this.findField(DatePickerDialog.class,
                            DatePicker.class, "mDatePicker");

                    datePicker = (DatePicker) field.get(this);
                    final Class<?> delegateClass = Class
                            .forName("android.widget.DatePicker$DatePickerDelegate");
                    final Field delegateField = this.findField(DatePicker.class,
                            delegateClass, "mDelegate");

                    final Object delegate = delegateField.get(datePicker);
                    final Class<?> spinnerDelegateClass = Class
                            .forName("android.widget.DatePickerSpinnerDelegate");

                    if (delegate.getClass() != spinnerDelegateClass) {
                        delegateField.set(datePicker, null);
                        datePicker.removeAllViews();

                        final Constructor spinnerDelegateConstructor = spinnerDelegateClass
                                .getDeclaredConstructor(DatePicker.class,
                                        Context.class, AttributeSet.class,
                                        int.class, int.class);
                        spinnerDelegateConstructor.setAccessible(true);

                        final Object spinnerDelegate = spinnerDelegateConstructor
                                .newInstance(datePicker, context, null,
                                        android.R.attr.datePickerStyle, 0);
                        delegateField.set(datePicker, spinnerDelegate);

                        datePicker.init(year, monthOfYear, dayOfMonth, this);
                        datePicker.setCalendarViewShown(false);
                        datePicker.setSpinnersShown(true);
                    }
                } catch (Exception e) { /* Do nothing */
                }
            } else {
                datePicker = new DatePicker(context);
            }
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            super.onDateChanged(view, year, month, day);
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, day);
            String title = DateUtils.formatDateTime(context,
                    mCalendar.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_SHOW_WEEKDAY
                            | DateUtils.FORMAT_SHOW_YEAR
                            | DateUtils.FORMAT_ABBREV_MONTH
                            | DateUtils.FORMAT_ABBREV_WEEKDAY);
            if (hasNoDay)
                title = year + "年" + (month + 1) + "月";

            this.setTitle(title);
        }

        /**
         * Find Field with expectedName in objectClass. If not found, find first
         * occurrence of target fieldClass in objectClass.
         */
        private Field findField(Class objectClass, Class fieldClass,
                                String expectedName) {
            try {
                final Field field = objectClass.getDeclaredField(expectedName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) { /* Ignore */
            }

            // Search for it if it wasn't found under the expectedName.
            for (final Field field : objectClass.getDeclaredFields()) {
                if (field.getType() == fieldClass) {
                    field.setAccessible(true);
                    return field;
                }
            }

            return null;
        }

        boolean hasNoDay;

        public void setHasNoDay(boolean bol) {

            hasNoDay = bol;

            if (hasNoDay)

                try {
                    ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0))
                            .getChildAt(0)).getChildAt(2).setVisibility(
                            View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        public boolean isHasNoDay() {
            return hasNoDay;
        }
    }

}