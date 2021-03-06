package com.robotshell.timerecorder.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RemoteViews.RemoteView;

import com.robotshell.timerecorder.R;
import com.robotshell.timerecorder.bean.Day;
import com.robotshell.timerecorder.data.DateFactory;
import com.robotshell.timerecorder.utils.DataUtils;
import com.robotshell.timerecorder.utils.UIUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 * 仿GitHub的提交活跃表
 * 横屏使用
 */

@RemoteView
public class ContributionView extends View {
    private static final int DISPLAY_MODE_MONTH = 0;
    private static final int DISPLAY_MODE_SEASON = 1;
    private static final int DISPLAY_MODE_YEAR = 2;

    private static final String TAG = "ContributionView";
    /**
     * 灰色方格的默认颜色
     **/
    private final static int DEFAULT_BOX_COLOR = 0xFFDDDDDD;
    private final static int DEFAULT_TEXT_COLOR = Color.GRAY;

    private final static int DEFAULT_PADDING = 24;
    private final static int DEFAULT_TEXT_SIZE = 8;
    private final static int DEFAULT_BOX_SIZE = 8;
    /**
     * 提交次数颜色值
     **/
    private final static int[] COLOUR_LEVEL =
            new int[]{0xFF1E6823, 0xFF44A340, 0xFF8CC665, 0xFFD6E685, DEFAULT_BOX_COLOR};
    /**
     * 星期
     **/
    private String[] weeks = new String[]{"Mon", "Wed", "Fri", "Sun"};
    /**
     * 月份
     **/
    private String[] months =
            new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    /**
     * 默认的padding,绘制的时候不贴边画
     **/
    private int padding = DEFAULT_PADDING;
    /**
     * 小方格的默认边长
     **/
    private int boxSide = DEFAULT_BOX_SIZE;
    /**
     * 小方格间的默认间隔
     **/
    private int boxInterval = 2;
    private int textSize = DEFAULT_TEXT_SIZE;
    private int textColor = DEFAULT_TEXT_COLOR;

    /**
     * 所有周的列数
     **/
    private int column = 0;

    private int displayMode = DISPLAY_MODE_YEAR;

    private List<Day> mDays;//一年中所有的天
    private Paint boxPaint;//方格画笔
    private Paint textPaint;//文字画笔
    private Paint infoPaint;//弹出框画笔

    private Paint.FontMetrics metrics;//测量文字

    private float downX;//按下的点的X坐标
    private float downY;//按下的点的Y坐标
    private Day clickDay;//按下所对应的天

    private int year;
    private int season;
    private int month;

    public ContributionView(Context context) {
        this(context, null);
        Log.d(TAG, "ContributionView() called with: context = [" + context + "]");
    }

    public ContributionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Log.d(TAG, "ContributionView() called with: context = [" + context + "], attrs = [" + attrs + "]");
    }

    public ContributionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "ContributionView() called with: context = [" + context + "], attrs = [" + attrs + "], defStyleAttr = [" + defStyleAttr + "]");
        initView(context, attrs);
    }

    public void initView(Context context, AttributeSet attrs) {
        Log.d(TAG, "initView() called");
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ContributionView);
            displayMode = a.getInt(R.styleable.ContributionView_display_mode, displayMode);
            textSize = (int) a.getDimension(R.styleable.ContributionView_textSize, DEFAULT_TEXT_SIZE);
            textColor = a.getColor(R.styleable.ContributionView_textColor, DEFAULT_TEXT_COLOR);
            a.recycle();
        }

        year = DataUtils.getCurYear();
        season = DataUtils.getCurSeason();
        month = DataUtils.getCurMonth();

        getDaysByDisplayMode();
        //方格画笔
        boxPaint = new Paint();
        boxPaint.setStyle(Paint.Style.FILL);
        boxPaint.setStrokeWidth(1);
        boxPaint.setColor(DEFAULT_BOX_COLOR);
        boxPaint.setAntiAlias(true);
        //文字画笔
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        //弹出的方格信息画笔
        infoPaint = new Paint();
        infoPaint.setStyle(Paint.Style.FILL);
        infoPaint.setColor(0xCC888888);
        infoPaint.setTextSize(textSize);
        infoPaint.setAntiAlias(true);
        //将默认值转换px
        padding = UIUtils.dp2px(getContext(), DEFAULT_PADDING);
//        boxSide = UIUtils.dp2px(getContext(), boxSide);

        metrics = textPaint.getFontMetrics();
    }

    private void getDaysByDisplayMode() {
        switch (displayMode) {
            case DISPLAY_MODE_MONTH:
                mDays = DateFactory.getDaysForMonth(year, month);
                break;
            case DISPLAY_MODE_SEASON:
                mDays = DateFactory.getDaysForSeason(year, season);
                break;
            default:
                mDays = DateFactory.getDays();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        getDaysByDisplayMode();
        Log.d(TAG, "onMeasure() called with: widthMeasureSpec = [" + widthMeasureSpec + "], heightMeasureSpec = [" + heightMeasureSpec + "]");
        float width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        float height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        float viewWidth = width;
        float viewHeight = height;

        if (width != 0 && width <= height) {
            boxSide = ((int) width - 2 * padding - (int) textPaint.measureText(weeks[0])) / (int) Math.ceil(mDays.size() / 7) - boxInterval;
            viewHeight = (2 * padding + (boxSide + boxInterval) * 7);
            if (viewHeight > height) {
                boxSide = (int) (height / viewHeight * boxSide);
                viewHeight = (2 * padding + (boxSide + boxInterval) * 7);
            }
        } else if (width != 0 && width > height) {
            boxSide = ((int) height - 2 * padding - (int) textPaint.measureText(months[0])) / 7 - boxInterval;
            viewWidth = 2 * padding
                    + (boxSide + boxInterval) * (int) Math.ceil(mDays.size() / 7)
                    + (int) textPaint.measureText(weeks[0]);
            if (viewWidth > width) {
                boxSide = (int) (width / viewWidth * boxSide);
                viewWidth = 2 * padding
                        + (boxSide + boxInterval) * (int) Math.ceil(mDays.size() / 7)
                        + (int) textPaint.measureText(weeks[0]);
            }
        }

        setMeasuredDimension((int) viewWidth, (int) viewHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged() called with: w = [" + w + "], h = [" + h + "], oldw = [" + oldw + "], oldh = [" + oldh + "]");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw() called with: canvas = [" + canvas + "]");
        column = 0;
        canvas.save();
        drawBox(canvas);
        drawWeek(canvas);
        drawTag(canvas);
        drawPopupInfo(canvas);
        canvas.restore();
    }

    /**
     * 画出1-12月方格小块和上面的月份
     *
     * @param canvas 画布
     */
    private void drawBox(Canvas canvas) {
        Log.d(TAG, "drawBox() called with: canvas = [" + canvas + "]");
        //方格的左上右下坐标
        float startX, startY, endX, endY;
        //起始月份为1月
        int month = mDays.get(0).month;
        for (int i = 0; i < mDays.size(); i++) {
            Day day = mDays.get(i);

            if ((day.month == DataUtils.getCurMonth() && day.date > DataUtils.getCurDay())
                    || day.month > DataUtils.getCurMonth()) {
                boxPaint.setStyle(Paint.Style.STROKE);
            }

            if (i == 0) {
                //画1月的文本标记,坐标应该是x=padding,y=padding-boxSide/2(间隙),y坐标在表格上面一点
                canvas.drawText(months[month - 1], padding, padding - boxSide / 2, textPaint);
            }
            if (day.week == 1 && i != 0) {
                //如果当天是周1，那么说明增加了一列
                column++;
                //如果列首的月份有变化，那么说明需要画月份
                if (day.month > month) {
                    month = day.month;
                    //月份文本的坐标计算,x坐标在变化，而y坐标都是一样的，boxSide/2(间隙)
                    canvas.drawText(months[month - 1], padding + column * (boxSide + boxInterval), padding - boxSide / 2, textPaint);
                }
            }
            //计算方格坐标点,x坐标一致随列数的增多而增加,y坐标随行数的增多而变化
            startX = padding + column * (boxSide + boxInterval);
            startY = padding + (day.week - 1) * (boxSide + boxInterval);
            endX = startX + boxSide;
            endY = startY + boxSide;
            //将该方格的坐标保存下来,这样可以在点击方格的时候计算弹框的坐标
            day.startX = startX;
            day.startY = startY;
            day.endX = endX;
            day.endY = endY;
            day.colour = getColour(day.contributionCount);
            //给画笔设置当前天的颜色
            boxPaint.setColor(day.colour);
            canvas.drawRect(startX, startY, endX, endY, boxPaint);
        }
        boxPaint.setColor(DEFAULT_BOX_COLOR);//恢复默认颜色
        boxPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 画左侧的星期
     *
     * @param canvas 画布
     */
    private void drawWeek(Canvas canvas) {
        //文字是左对齐,所以找出最长的字
        float textLength = 0;
        for (String week : weeks) {
            float tempLength = textPaint.measureText(week);
            if (textLength < tempLength) {
                textLength = tempLength;
            }
        }
        //依次画出星期文本,坐标点x=padding-文本长度-文本和方格的间隙,y坐标随行数变化
        canvas.drawText(weeks[0], padding - textLength - 2, padding + boxSide - metrics.descent, textPaint);
        canvas.drawText(weeks[1], padding - textLength - 2, padding + 3 * (boxSide + boxInterval) - metrics.descent, textPaint);
        canvas.drawText(weeks[2], padding - textLength - 2, padding + 5 * (boxSide + boxInterval) - metrics.descent, textPaint);
        canvas.drawText(weeks[3], padding - textLength - 2, padding + 7 * (boxSide + boxInterval) - metrics.descent, textPaint);
    }

    /**
     * 画出右下角的颜色深浅标志，因为是右对齐的所以需要从右往左画
     *
     * @param canvas 画布
     */
    private void drawTag(Canvas canvas) {
        //首先计算出两个文本的长度
        boxPaint.setStyle(Paint.Style.FILL);
        float moreLength = textPaint.measureText("More");
        float lessLength = textPaint.measureText("Less");
        //画 More 文本,x坐标=padding+（列数+1）*（方格边长+方格间隙）-一个方格间隙-文本长度
        float moreX = padding + (column + 1) * (boxSide + boxInterval) - boxInterval - moreLength;
        //y坐标=padding+（方格行数+1,和表格底部有些距离）*（方格边长+方格间隙）+字体的ascent高度
        float moreY = padding + 8 * (boxSide + boxInterval) + Math.abs(metrics.ascent);
        canvas.drawText("More", moreX, moreY, textPaint);
        //画深浅色块,坐标根据上面的More依次计算就可以了
        float interval = boxSide - 2;//文字和色块间的距离
        float leftX = moreX - interval - boxSide;
        float topY = moreY - boxSide;
        float rightX = moreX - interval;
        float bottomY = moreY;//色块的Y坐标是一样的
        for (int i = 0; i < COLOUR_LEVEL.length; i++) {
            boxPaint.setColor(COLOUR_LEVEL[i]);
            canvas.drawRect(leftX - i * (boxSide + boxInterval), topY, rightX - i * (boxSide + boxInterval), bottomY, boxPaint);
        }
        //最后画 Less 文本,原理同上
        canvas.drawText("Less", leftX - 4 * (boxSide + boxInterval) - interval - lessLength, moreY, textPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取点击时候的坐标，用来判断点在哪天，并弹出·
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            downX = event.getX();
            downY = event.getY();
            findClickBox();
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断是否点击在方格内
     */
    private void findClickBox() {
        for (Day day : mDays) {
            //检测点击的坐标如果在方格内，则弹出信息提示
            if (downX >= day.startX && downX <= day.endX && downY >= day.startY && downY <= day.endY) {
                clickDay = day;//纪录点击的哪天
                break;
            }
        }
        //点击完要刷新，这样每次点击不同的方格，弹窗就可以在相应的位置显示
        refreshView();
    }

    public void setYear(int year) {
        this.year = year;
        refreshView();
    }

    public void setSeason(int season) {
        this.season = season;
        refreshView();
    }

    public void setMonth(int month) {
        this.month = month;
        refreshView();
    }

    public void refreshView() {
        getDaysByDisplayMode();
        invalidate();
    }

    /**
     * 画方格上的文字弹框
     *
     * @param canvas 画布
     */
    private void drawPopupInfo(Canvas canvas) {
        if (clickDay != null) {
            //先根据方格来画出一个小三角形，坐标就是方格的中间
            Path infoPath = new Path();
            //先从方格中心
            infoPath.moveTo(clickDay.startX + boxSide / 2, clickDay.startY + boxSide / 2);
            //然后是方格的左上点
            infoPath.lineTo(clickDay.startX, clickDay.startY);
            //然后是方格的右上点
            infoPath.lineTo(clickDay.endX, clickDay.startY);
            //画出三角
            canvas.drawPath(infoPath, infoPaint);
            //画三角上的圆角矩形
            textPaint.setColor(Color.WHITE);
            //得到当天的文本信息
            String popupInfo = clickDay.toString();
            System.out.println(popupInfo);
            //计算文本的高度和长度用以确定矩形的大小
            float infoHeight = metrics.descent - metrics.ascent;
            float infoLength = textPaint.measureText(popupInfo);
            Log.e("height", infoHeight + "");
            Log.e("length", infoLength + "");
            //矩形左上点应该是x=当前天的x+边长/2-（文本长度/2+文本和框的间隙）
            float leftX = (clickDay.startX + boxSide / 2) - (infoLength / 2 + boxSide);
            //矩形左上点应该是y=当前天的y+边长/2-（文本高度+上下文本和框的间隙）
            float topY = clickDay.startY - (infoHeight + 2 * boxSide);
            //矩形的右下点应该是x=leftX+文本长度+文字两边和矩形的间距
            float rightX = leftX + infoLength + 2 * boxSide;
            //矩形的右下点应该是y=当前天的y
            float bottomY = clickDay.startY;
            System.out.println("" + leftX + "/" + topY + "/" + rightX + "/" + bottomY);
            RectF rectF = new RectF(leftX, topY, rightX, bottomY);
            canvas.drawRoundRect(rectF, 4, 4, infoPaint);
            //绘制文字,x=leftX+文字和矩形间距,y=topY+文字和矩形上面间距+文字顶到基线高度
            canvas.drawText(popupInfo, leftX + boxSide, topY + boxSide + Math.abs(metrics.ascent), textPaint);
            clickDay = null;//重新置空，保证点击方格外信息消失
            textPaint.setColor(textColor);//恢复画笔颜色
        }
    }

    /**
     * 设置某天的次数
     *
     * @param year         年
     * @param month        月
     * @param day          日
     * @param contribution 次数
     */
    public void setData(int year, int month, int day, int contribution) {
        //先找到是第几天，为了方便不做参数检测了
        for (Day d : mDays) {
            if (d.year == year && d.month == month && d.date == day) {
                d.contributionCount = contribution;
                d.colour = getColour(contribution);
                break;
            }
        }
        refreshView();
    }

    /**
     * 根据提交次数来获取颜色值
     *
     * @param contribution 提交的次数
     * @return 颜色值
     */
    private int getColour(int contribution) {
        int colour = 0;
        if (contribution <= 0) {
            colour = COLOUR_LEVEL[4];
        }
        if (contribution == 1) {
            colour = COLOUR_LEVEL[3];
        }
        if (contribution == 2) {
            colour = COLOUR_LEVEL[2];
        }
        if (contribution == 3) {
            colour = COLOUR_LEVEL[1];
        }
        if (contribution >= 4) {
            colour = COLOUR_LEVEL[0];
        }
        return colour;
    }

}
