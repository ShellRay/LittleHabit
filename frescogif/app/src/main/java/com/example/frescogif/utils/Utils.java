package com.example.frescogif.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Created by GG on 2017/6/6.
 */
public class Utils {

    public static int convertDpToPixel(Context context, int dp) {
        /*float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);*/
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    //将16进制的字符串变成整型int
    public static int parse(String s) throws NumberFormatException
    {
        if(!s.startsWith("0x"))
            throw new NumberFormatException();
        int number=0,n=0;
        for(int i=2;i<s.length();i++)
        {
            char c=s.charAt(i);
            switch(c)
            {
                case '1':
                    n=1;break;
                case '2':
                    n=2;break;
                case '3':
                    n=3;break;
                case '4':
                    n=4;break;
                case '5':
                    n=5;break;
                case '6':
                    n=6;break;
                case '7':
                    n=7;break;
                case '8':
                    n=8;break;
                case '9':
                    n=9;break;
                case '0':
                    n=0;break;
                case 'a':
                case 'A':
                    n=10;break;
                case 'b':
                case 'B':
                    n=11;break;
                case 'c':
                case 'C':
                    n=12;break;
                case 'd':
                case 'D':
                    n=13;break;
                case 'e':
                case 'E':
                    n=14;break;
                case 'f':
                case 'F':
                    n=15;break;
                default:
                    throw new NumberFormatException();
            }
            number=number*16+n;
        }
        return number;
    }

    public static final String SYS_EMUI = "sys_emui";
    public static final String SYS_MIUI = "sys_miui";
    public static final String SYS_FLYME = "sys_flyme";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

    /**获取当前手机名称*/
    public static String getSystemName(){
        String SYS = "unknown";
        try {
            Properties prop= new Properties();
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            if(prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null){
                SYS = SYS_MIUI;//小米
            }else if(prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
                    ||prop.getProperty(KEY_EMUI_VERSION, null) != null
                    ||prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null){
                SYS = SYS_EMUI;//华为
            }else if(getMeizuFlymeOSFlag().toLowerCase().contains("flyme")){
                SYS = SYS_FLYME;//魅族
            };
        } catch (IOException e){
            e.printStackTrace();
            return SYS;
        }
        return SYS;
    }

    public static String getMeizuFlymeOSFlag() {
        return getSystemProperty("ro.build.display.id", "");
    }

    private static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String)get.invoke(clz, key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }

}
