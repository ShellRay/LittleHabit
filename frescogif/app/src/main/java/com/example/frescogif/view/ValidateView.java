package com.example.frescogif.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 *  验证码自定义控件
 * Created by ShellRay on 2016/6/10.
 */
public class ValidateView extends View{
    /**
     * 点数
     */
    private int pointNum = 0;// 背景杂质
    /**
     * 线段数
     */
    private int lineNum = 0;// 背景杂质
    /**
     * 验证码字长
     */
    private int validateCodeLenght = 6;// 默认长度为6
    /**
     * 验证码
     */
    private String[] validateCode;
    private Paint mTempPaint = new Paint();
    private Context mContext;
    //背景颜色
    private int backgroundColor = Color.TRANSPARENT;
    /**
     * 验证码内容
     */
    private static final String[] strContent = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a",
            "b", "c", "d", "e", "f", "g ", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
            "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S ", "T", "U", "V", "W", "X", "Y", "Z" };

    public ValidateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mTempPaint.setAntiAlias(true);
        mTempPaint.setTextSize(50);
        mTempPaint.setStrokeWidth(5);
        validateCode = new String[validateCodeLenght];// 验证码
    }

    public String[] getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String[] validateCode) {
        this.validateCode = validateCode;
    }

    public void onDraw(Canvas canvas) {

        canvas.drawColor(backgroundColor);

        // 绘制验证码
        final int height = getHeight();
        final int width = getWidth();
        int dx = 25;

        for (int i = 0; i < validateCodeLenght; i++) {
            mTempPaint.setColor((int) (Math.random() * Color.GREEN));
            canvas.drawText("" + validateCode[i], dx, getPositon(height), mTempPaint);
            dx += width / (validateCodeLenght + 1);
        }
        int[] line;

        for (int i = 0; i < lineNum; i++) {
            line = getLine(height, width);
            canvas.drawLine(line[0], line[1], line[2], line[3], mTempPaint);
        }
        // 绘制小圆点
        int[] point;
        for (int i = 0; i < pointNum; i++) {
            point = getPoint(height, width);
            canvas.drawCircle(point[0], point[1], 1, mTempPaint);
        }
    }

    /**
     * 更新验证码显示
     */
    public void invaliChenkNum() {
        invalidate();
    }

    /**
     * 生成验证码，并显示在图片上
     *
     * @return
     */
    public String[] createAndSetValidateCode() {
        String[] tempValidateCode = new String[validateCodeLenght];
        for (int i = 0; i < validateCodeLenght; i++) {
            tempValidateCode[i] = strContent[(int) (Math.random() * strContent.length)];
        }
        validateCode = tempValidateCode;
        return tempValidateCode;
    }

    /**
     * 生成验证码
     *
     * @return
     */
    public String[] createCheckNum() {
        String[] tempCheckNum = new String[validateCodeLenght];
        for (int i = 0; i < validateCodeLenght; i++) {
            tempCheckNum[i] = strContent[(int) (Math.random() * strContent.length)];
        }
        return tempCheckNum;
    }

    private int[] getLine(int height, int width) {
        int[] tempCheckNum = new int[validateCodeLenght];
        for (int i = 0; i < validateCodeLenght; i += 2) {
            tempCheckNum[i] = (int) (Math.random() * width);
            tempCheckNum[i + 1] = (int) (Math.random() * height);
        }
        return tempCheckNum;
    }

    private int[] getPoint(int height, int width) {
        int[] tempCheckNum = new int[validateCodeLenght];
        tempCheckNum[0] = (int) (Math.random() * width);
        tempCheckNum[1] = (int) (Math.random() * height);
        return tempCheckNum;
    }

    /**
     * 验证是否正确，单元测试通过
     *
     * @param userCheck
     *            用户输入的验证码
     * @param code
     *            自动生成的验证码
     * @return
     */
    public boolean validateCode(String userCheck, String[] code) {
        if (userCheck.length() != validateCodeLenght) {
            return false;
        }
        String checkString = "";
        for (int i = 0; i < validateCodeLenght; i++) {
            checkString += code[i].toLowerCase();//忽略大小写
        }
        if (userCheck.equals(checkString)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取验证码的纵坐标
     *
     * @param height
     * @return
     */
    public int getPositon(int height) {
       /* int tempPositoin = (int) (Math.random() * height);//上下有波动
        if (tempPositoin < 25) {
            tempPositoin += 25;
        }
        return tempPositoin;*/
        height = (int) (height * 0.6);//平行的排列
       return height;
    }

    public int getPointNum() {
        return pointNum;
    }

    public void setPointNum(int pointNum) {
        this.pointNum = pointNum;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public int getValidateCodeLenght() {
        return validateCodeLenght;
    }

    public void setValidateCodeLenght(int validateCodeLenght) {
        this.validateCodeLenght = validateCodeLenght;
        validateCode = new String[validateCodeLenght];// 验证码
    }

}
