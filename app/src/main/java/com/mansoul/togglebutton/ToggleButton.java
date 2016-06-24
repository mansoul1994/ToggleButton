package com.mansoul.togglebutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义开关按钮
 * Created by Mansoul on 16/6/23.
 */
public class ToggleButton extends View {

    private Paint mPaint;
    private Bitmap switchBackgroundBitmap;     //开关背景 图片
    private Bitmap slideButtonBitmap;         //开关滑块按钮

    private boolean switchState = false;  //开关状态, 默认关闭
    private boolean isTouchMode = false; //触摸状态

    private float currentX;

    public ToggleButton(Context context) {
        super(context);
        init();
    }

    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        //获取配置的自定义属性
        String namespace = "http://schemas.android.com/apk/res-auto";
        int switchBackground = attrs.getAttributeResourceValue(namespace, "switch_background", -1);
        int slideButton = attrs.getAttributeResourceValue(namespace, "slide_button", -1);
        boolean switchState = attrs.getAttributeBooleanValue(namespace, "switch_state", false);

        //初始化自定义属性
        setSwitchBackground(switchBackground);
        setSlideButton(slideButton);
        setSwitchState(switchState);

    }

    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(switchBackgroundBitmap.getWidth(), switchBackgroundBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //1. 绘制背景
        canvas.drawBitmap(switchBackgroundBitmap, 0, 0, mPaint);

        //2. 绘制滑块
        if (isTouchMode) {
            float newLeft = currentX - slideButtonBitmap.getWidth() / 2.0f;

            float maxLeft = switchBackgroundBitmap.getWidth() - slideButtonBitmap.getWidth();

            if (newLeft < 0) {
                newLeft = 0;
            } else if (newLeft > maxLeft) {
                newLeft = maxLeft;
            }

            canvas.drawBitmap(slideButtonBitmap, newLeft, 0, mPaint);
        } else {
            //根据开关状态绘制滑块
            if (switchState) {
                //开
                int left = switchBackgroundBitmap.getWidth() - slideButtonBitmap.getWidth();
                canvas.drawBitmap(slideButtonBitmap, left, 0, mPaint);
            } else {
                //关
                canvas.drawBitmap(slideButtonBitmap, 0, 0, mPaint);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouchMode = true;

                currentX = event.getX();

                break;
            case MotionEvent.ACTION_MOVE:

                currentX = event.getX();

                break;
            case MotionEvent.ACTION_UP:
                isTouchMode = false;

                currentX = event.getX();

                // 如果开关状态变化了, 通知界面. 里边开关状态更新了.
                boolean state = currentX > switchBackgroundBitmap.getWidth() / 2.0f;

                //开关状态改变了, 通知界面
                if (state != switchState && mListener != null) {
                    mListener.onSwitchState(state);
                }

                //更新开关
                switchState = state;
                break;
        }

        invalidate(); //重新调用onDraw()方法, 更新界面

        return true;
    }

    public void setSwitchBackground(int switchBackground) {
        switchBackgroundBitmap = BitmapFactory.decodeResource(getResources(), switchBackground);
    }

    public void setSlideButton(int slideButton) {
        slideButtonBitmap = BitmapFactory.decodeResource(getResources(), slideButton);
    }

    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }

    public interface OnSwitchStateListener {
        void onSwitchState(boolean state);
    }

    private OnSwitchStateListener mListener;

    public void setOnSwitchStateListener(OnSwitchStateListener listener) {
        this.mListener = listener;
    }
}
