package com.example.frescogif.utils;

import android.content.Context;

/**
 * Created by GG on 2017/6/6.
 */
public class Utils {

    public static int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
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

}
