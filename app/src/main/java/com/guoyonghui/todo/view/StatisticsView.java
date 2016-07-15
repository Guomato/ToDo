package com.guoyonghui.todo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.guoyonghui.todo.R;

import java.util.ArrayList;

public class StatisticsView extends View {

    private static final int HORIZONTAL_DISTANCE = 150;

    private static final int VERTICAL_DISTANCE = 20;

    private static final int COLUMN_MIN_HEIGHT = 10;

    private static int UNIT_HEIGHT = 60;

    private ArrayList<String> mTitles;

    private ArrayList<Integer> mValues;

    private ArrayList<Integer> mColors;

    private Context mContext;

    private Paint mPaint;

    private Rect mRect;

    private boolean mDataIsAvailable = false;

    public StatisticsView(Context context) {
        super(context);

        init(context);
    }

    public StatisticsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public StatisticsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        mContext = context;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
        mRect = new Rect();
    }

    public void setCategory(Category category) {
        if (category.mValues.size() != category.mColors.size()) {
            throw new IllegalStateException("The number of colors must equal to that of value.");
        }

        if (category.mValues.isEmpty()) {
            return;
        }

        mTitles = category.mTitles;
        mValues = category.mValues;
        mColors = category.mColors;

        mDataIsAvailable = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!mDataIsAvailable) {
            return;
        }

        int size = mTitles.size();

        int viewWidth = getWidth();
        int bottom = getHeight() * 3 / 4;

        int unitWidth = viewWidth / size;
        int unitHeight = checkUnitHeight();
        
        int baseline;

        checkUnitHeight();

        mPaint.setTextSize(obtainTextSize(unitWidth));
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();

        for (int i = 0; i < size; i++) {
            int left = i * unitWidth + HORIZONTAL_DISTANCE / 2;
            int right = (i + 1) * unitWidth - HORIZONTAL_DISTANCE / 2;
            int top = bottom - (mValues.get(i) == 0 ? COLUMN_MIN_HEIGHT : mValues.get(i) * unitHeight);

            //绘制柱形图
            mRect.set(left, top, right, bottom);
            mPaint.setColor(getColor(mColors.get(i)));
            canvas.drawRect(mRect, mPaint);

            //绘制柱形图数量标题
            mRect.set(left, top - VERTICAL_DISTANCE, right, top - VERTICAL_DISTANCE - 50);
            mPaint.setColor(getColor(R.color.colorPrimary));
            mPaint.setTextAlign(Paint.Align.CENTER);
            baseline = (mRect.top + mRect.bottom - fmi.top - fmi.bottom) / 2;
            canvas.drawText(mValues.get(i) + "", mRect.centerX(), baseline, mPaint);

            //绘制柱形图内容标题
            mRect.set(left, bottom + VERTICAL_DISTANCE, right, bottom + VERTICAL_DISTANCE + 100);
            mPaint.setColor(getColor(R.color.colorPrimary));
            mPaint.setTextAlign(Paint.Align.CENTER);
            baseline = (mRect.top + mRect.bottom - fmi.top - fmi.bottom) / 2;
            canvas.drawText(mTitles.get(i), mRect.centerX(), baseline, mPaint);
        }
    }

    private int checkUnitHeight() {
        int max = mValues.get(0);
        for (int value : mValues) {
            max = max < value ? value : max;
        }
        if (max * UNIT_HEIGHT > getHeight() / 2) {
            return getHeight() / 2 / max;
        } else {
            return UNIT_HEIGHT;
        }
    }

    /**
     * 根据数据的标题计算标题所需的字号
     *
     * @param unitWidth 柱形图单位宽度
     * @return 字号
     */
    private int obtainTextSize(int unitWidth) {
        int textSize = 200;

        mPaint.setTextSize(textSize);
        int titleWidth = 0;
        for (String title : mTitles) {
            mPaint.getTextBounds(title, 0, title.length(), mRect);
            titleWidth = titleWidth < (mRect.right - mRect.left) ? (mRect.right - mRect.left) : titleWidth;
        }

        int maxWidth = (unitWidth - HORIZONTAL_DISTANCE) / 2;
        if (titleWidth > maxWidth) {
            textSize = 200 * maxWidth / titleWidth;
        }

        return textSize;
    }

    private int getColor(int resId) {
        return mContext.getResources().getColor(resId);
    }

    public static class Category {

        private ArrayList<String> mTitles;

        private ArrayList<Integer> mValues;

        private ArrayList<Integer> mColors;

        public Category(ArrayList<Integer> colors) {
            mColors = new ArrayList<>();
            mColors.addAll(colors);

            mTitles = new ArrayList<>();
            mValues = new ArrayList<>();
        }

        public Category(int... colors) {
            mTitles = new ArrayList<>();
            mValues = new ArrayList<>();

            mColors = new ArrayList<>();
            for (int color : colors) {
                mColors.add(color);
            }
        }

        public void add(String title, int value) {
            mTitles.add(title);
            mValues.add(value);
        }
    }
}
