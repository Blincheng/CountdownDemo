package com.mic.countdowndemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by zhaocheng on 2017/3/27.
 */

public class CountdownView extends TextView implements CountdownTime.OnCountdownTimeListener{
    /**
     * 当前控件绑定的倒计时实践对象id，由于重用，RecyclerView滚动的时候，
     * 会复用view，导致里面显示的时间其实是不一样的
     * */
    private String nowId;
    private CountdownTimeQueueManager manager;
    private CountdownTime countdownTime;
    private float TEXT_SIZE = 63;
    private int TEXT_COLOR = 0xFFF18D00;
    private Paint textPaint;

    public CountdownView(Context context) {
        super(context);
        init();
    }

    public CountdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        textPaint = getPaint();
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setStrokeWidth(1);
        manager = CountdownTimeQueueManager.getInstance();
    }
    private void drawText(Canvas canvas){
        String testString;
        if(countdownTime == null){
            testString = "00:00";
        }else{
            testString = countdownTime.getTimeText();
        }
        Rect bounds = new Rect();
        textPaint.getTextBounds(testString, 0, testString.length(), bounds);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(testString,getMeasuredWidth() / 2 - bounds.width() / 2, baseline, textPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawText(canvas);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }
    private int measureWidth(int origin) {
        int result = (int) textPaint.measureText("00:00");
        int specMode = MeasureSpec.getMode(origin);
        int specSize = MeasureSpec.getSize(origin);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    /**
     * 这边比较粗暴，哈哈哈
     * */
    private int measureHeight(int origin) {
        int result = (int) textPaint.measureText("00");
        int specMode = MeasureSpec.getMode(origin);
        int specSize = MeasureSpec.getSize(origin);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    public void onCountdownTimeDraw(CountdownTime time) {
        if(TextUtils.equals(nowId,time.getId())){
            countdownTime = time;
            postInvalidate();
        }
    }
    /**
     * 多了一个id参数，实际应用中可以是订单id、流水id之类，可以保证唯一性即可
     * */
    public void setCountdownTime(int time,String id){
        nowId = id;
        if(time <= 0){
            if(countdownTime != null)
                countdownTime.setSeconds(0);
        }else{
            WeakReference<CountdownView> weakReference = new WeakReference<>(this);
            countdownTime = manager.addTime(time,id,weakReference.get());
        }
        postInvalidate();
    }
}
