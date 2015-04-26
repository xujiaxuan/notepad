/*
 * Copyright (C) 2013, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * File name: LinedEditText.java
 * 
 * Description: the text once you edit, you can see a line on the bottom of the text
 *  
 * Author: fuping
 * 
 * Ver 1.0, 2013-5-3, fuping, Create file
 */

package com.tplink.android;

import android.widget.*;
import android.util.*;
import android.graphics.*;
import android.content.*;

/**
 * The class LinedEditText
 */
public class LinedEditText extends EditText {
    private Rect mRect;

    private Paint mPaint;

    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0x800000FF);
    }

    /**
     * once the LinedEditText is being modified, the onDraw is being called
     * automatically, it will draw lines on the bottom of the text
     */
    protected void onDraw(Canvas canvas) {
        int count = getLineCount();
        Rect rect = mRect;
        Paint paint = mPaint;

        for (int i = 0; i < count; i++) {
            int baseLine = getLineBounds(i, rect);
            canvas.drawLine(rect.left, baseLine + 1, rect.right, baseLine + 1, paint);
        }
        super.onDraw(canvas);
    }
}
