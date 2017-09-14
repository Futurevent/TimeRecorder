package com.robotshell.timerecorder.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.robotshell.timerecorder.bean.Contribution;
import com.robotshell.timerecorder.bean.Day;
import com.robotshell.timerecorder.data.ContributionDataManager;
import com.robotshell.timerecorder.utils.DataUtils;

import java.util.List;


public class DayBar extends View {
    public static final int DAY_MINUTES = 1440;

    private int[] colors = new int[]{Color.GREEN,
            Color.parseColor("#EF9A9A"),
            Color.parseColor("#4A148C"),
            Color.parseColor("#26A69A"),
            Color.parseColor("#FFEB3B"),
            Color.parseColor("#FF5722"),
            Color.parseColor("#99CC00"),
            Color.GREEN};
    private int[] barColors = new int[]{
            Color.parseColor("#00FFFFFF"),
            Color.parseColor("#88BBBBBB"),
            Color.parseColor("#88444444"),
            Color.parseColor("#00FFFFFF")
    };

    private Paint barPaint;
    private Paint contributPaint;
    private Shader mGradient;
    private Shader mBarGradient;
    private int minHeight;
    private float minWidth;

    private int viewWidth;
    private int viewHeight;

    public DayBar(Context context) {
        this(context, null);
    }

    public DayBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        barPaint = new Paint();
        barPaint.setColor(Color.LTGRAY);
        barPaint.setStyle(Paint.Style.FILL);

        contributPaint = new Paint();
        contributPaint.setColor(Color.GREEN);
        contributPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        viewWidth = width;
        viewHeight = width / 8;

        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startX, startY, endX, endY;
        Day day = ContributionDataManager.getInstance().getToday();
        List<Contribution> contributions = day.contributions;

        canvas.save();
        minWidth = ((float) viewWidth) / DAY_MINUTES;
        minHeight = viewHeight;
        startX = 0;
        startY = 0;
        endX = minWidth * DAY_MINUTES;
        endY = minHeight;
        mBarGradient = new LinearGradient(startX, startY, endX, endY, barColors, null, Shader.TileMode.CLAMP);
        barPaint.setShader(mBarGradient);
        canvas.drawRect(startX, startY, endX, endY, barPaint);

        mGradient = new LinearGradient(startX, startY, endX, endY, colors, null, Shader.TileMode.CLAMP);
        contributPaint.setShader(mGradient);

        for (Contribution contribution : contributions) {
            int startMin = (int) (contribution.startTime - DataUtils.getTodayMillis()) / (1000 * 60);
            int durMin = (int) Math.ceil(contribution.duration / (1000 * 60));
            startX = minWidth * startMin;
            startY = 0;
            endX = startX + minWidth * durMin;
            endY = minHeight;
            canvas.drawRect(startX, startY, endX, endY, contributPaint);
        }
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
