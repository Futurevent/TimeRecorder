package com.robotshell.timerecorder.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.robotshell.timerecorder.R;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/1/10.
 * 带刻度的圆形进度条
 */

public class CircleProgressBar extends View {
    private final static int DEFAULT_TEXT_COLOR = Color.GRAY;
    private final static int DEFAULT_TEXT_SIZE = 8;

    private Paint mPaint = new Paint();
    private Paint colorPaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint textPaint = new Paint();
    private int[] colors = new int[]{Color.GREEN,
            Color.parseColor("#EF9A9A"),
            Color.parseColor("#4A148C"),
            Color.parseColor("#26A69A"),
            Color.parseColor("#FFEB3B"),
            Color.parseColor("#FF5722"),
            Color.parseColor("#99CC00"),
            Color.GREEN};
    private Shader mGradient;
    private float centerX, centerY;
    private float radius;
    private int progress = 0;
    //  默认的padding值
    private float padding = 20;
    private float grayWidth = 30;
    private float colorWidth = grayWidth;
    private float lineWidth = colorWidth + 5;
    private float intervalAngle = (float) (Math.PI / 180.0);

    private int textSize = DEFAULT_TEXT_SIZE;
    private int textColor = DEFAULT_TEXT_COLOR;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getMeasuredSize(widthMeasureSpec);
        int height = getMeasuredSize(heightMeasureSpec);
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    private int getMeasuredSize(int measureSpec) {
        int size = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int nowSize = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                size = nowSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                size = 300;
                break;
        }
        return size;
    }

    private void initView(AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
            textSize = (int) a.getDimension(R.styleable.CircleProgressBar_cpb_textSize, DEFAULT_TEXT_SIZE);
            textColor = a.getColor(R.styleable.CircleProgressBar_cpb_textColor, DEFAULT_TEXT_COLOR);
            a.recycle();
        }

        //灰色圆环画笔
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.LTGRAY);
        mPaint.setStrokeWidth(grayWidth);
        //渐变色圆环画笔
        colorPaint.setStyle(Paint.Style.STROKE);
        colorPaint.setAntiAlias(true);
        colorPaint.setColor(Color.LTGRAY);
        colorPaint.setStrokeWidth(colorWidth);
        //白色刻度画笔
        linePaint.setColor(Color.WHITE);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(5);
        //文字画笔
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        centerX = (w - getPaddingLeft() - getPaddingRight()) / 2;
        centerY = (h - getPaddingTop() - getPaddingBottom()) / 2;
        radius = centerX - padding;//让圆的半径填充一个默认padding
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        //绘制灰色圆环
        canvas.translate(centerX, centerY);
        RectF rectF = new RectF(-radius, -radius, radius, radius);
        canvas.drawArc(rectF, -90, 360, false, mPaint);
        //绘制渐变色圆环
        if (mGradient == null) {//线性渐变着色器
            mGradient = new LinearGradient(-radius, -radius, radius, radius, colors, null, Shader.TileMode.CLAMP);
        }
        colorPaint.setShader(mGradient);
        canvas.drawArc(rectF, -90, (float) ((progress / 100.0) * 360), false, colorPaint);
        //绘制白色分割条，100条白色线段，每个间隔3.6度
        for (float i = 0; i < 360; i += 3.6) {
            float startX = (float) ((radius + grayWidth) * Math.cos(i * intervalAngle));
            float startY = (float) ((radius + grayWidth) * Math.sin(i * intervalAngle));
            float stopX = (float) ((radius - grayWidth) * Math.cos(i * intervalAngle));
            float stopY = (float) ((radius - grayWidth) * Math.sin(i * intervalAngle));
            canvas.drawLine(startX, startY, stopX, stopY, linePaint);
        }
        //绘制中心进度文字
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        Calendar cal = Calendar.getInstance();
        String centerContent = "今年还剩" + (365 - cal.get(Calendar.DAY_OF_YEAR)) + "天";
        float fontWidth = textPaint.measureText(String.valueOf(centerContent));
        canvas.drawText(centerContent, -fontWidth / 2, fontHeight / 4, textPaint);
        canvas.restore();
    }

    /**
     * 设置当前进度
     *
     * @param progress 进度值，0<=progress<=100
     */
    public void setProgress(int progress) {
        if (progress <= 0) progress = 0;
        if (progress >= 100) progress = 100;
        this.progress = progress;
        invalidate();
    }
}
