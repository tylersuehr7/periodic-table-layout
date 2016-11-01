package com.tylersuehr.periodictablelayout;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
/**
 * Copyright 2016 Tyler Suehr
 * Created by tyler on 11/1/2016.
 *
 * This is another very simplified ViewGroup based on the Android TableLayout.
 *
 * This ViewGroup will allow you to add elements to it, minimizing view hierarchies, promoting
 * efficiency and rendering. This layout includes 8dp margin above the 8th row as there are 9
 * rows in the Periodic Table of Elements.
 */
public class PeriodicTableLayout extends ViewGroup {
    private final Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int columnCount = 18;
    private boolean drawGrid = false;
    private final int rowCount = 9;
    private final int eightDp;


    public PeriodicTableLayout(Context c) {
        this(c, null);
    }

    public PeriodicTableLayout(Context c, AttributeSet attrs) {
        this(c, attrs, 0);
    }

    public PeriodicTableLayout(Context c, AttributeSet attrs, int def) {
        super(c, attrs, def);
        this.eightDp = getResources().getDimensionPixelSize(R.dimen.small);
        this.gridPaint.setStyle(Paint.Style.FILL);
        this.gridPaint.setColor(Color.CYAN);

        // Set XML attributes
        if (attrs != null) {
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.PeriodicTableLayout);
            this.drawGrid = a.getBoolean(R.styleable.PeriodicTableLayout_drawGrid, false);
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize;
        int heightSize;

        widthSize = getDefaultSize(0, widthMeasureSpec);
        heightSize = getDefaultSize(0, heightMeasureSpec);

        int minDimension = Math.min(widthSize, heightSize);
        int maxDimension = Math.max(widthSize, heightSize);

        int blockDimension = heightSize / rowCount;
        int blockSpec = MeasureSpec.makeMeasureSpec(blockDimension, MeasureSpec.EXACTLY);

        // Include 8dp margin for 8th row
        blockSpec -= eightDp / 9; // Nine columns

        measureChildren(blockSpec, blockSpec);
        setMeasuredDimension(maxDimension * 2, maxDimension);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int row;
        int col;
        int left;
        int top;

        for (int i = 0; i < getChildCount(); i++) {
            row = i / columnCount;
            col = i % columnCount;

            View child = getChildAt(i);
            left = col * child.getMeasuredWidth();
            top = row * child.getMeasuredHeight();

            // Space at eighth row
            top += (row >= 7) ? eightDp : 0;

            child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (drawGrid) {
            // Draw horizontal lines
            for (int i = 0; i <= getWidth(); i += (getWidth() / columnCount)) {
                canvas.drawLine(i, 0, i, getHeight(), gridPaint);
            }

            // Draw vertical lines
            for (int i = 0; i <= getHeight(); i += (getHeight() / columnCount)) {
                canvas.drawLine(0, i, getWidth(), i, gridPaint);
            }
        }
    }

    public void drawGridLines(boolean value) {
        this.drawGrid = value;
        this.invalidate();
    }
}