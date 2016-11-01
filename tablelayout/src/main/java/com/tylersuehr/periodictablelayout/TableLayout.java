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
 * This is a very simplified version of the Android TableLayout ViewGroup.
 *
 * Basically, this allows you to define the rows and columns of the table and put views into it
 * such that there is very minimal nesting for maximum efficiency and rendering.
 */
public class TableLayout extends ViewGroup {
    private static final int DEFAULT_COLUMN_LENGTH = 3;
    private static final int DEFAULT_ROW_LENGTH = 3;

    // Drawing
    private final Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // Attributes
    private boolean drawGrid = false;
    private int columnCount = DEFAULT_COLUMN_LENGTH;
    private int rowCount = DEFAULT_ROW_LENGTH;


    public TableLayout(Context c) {
        this(c, null);
    }

    public TableLayout(Context c, AttributeSet attrs) {
        this(c, attrs, 0);
    }

    public TableLayout(Context c, AttributeSet attrs, int def) {
        super(c, attrs, def);
        this.gridPaint.setStyle(Paint.Style.FILL);
        this.gridPaint.setColor(Color.CYAN);

        // Get XML attributes
        if (attrs != null) {
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.TableLayout);
            this.columnCount = a.getInt(R.styleable.TableLayout_columnCount, DEFAULT_COLUMN_LENGTH);
            this.rowCount = a.getInt(R.styleable.TableLayout_rowCount, DEFAULT_ROW_LENGTH);
            this.drawGrid = a.getBoolean(R.styleable.TableLayout_drawGrid, false);
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

    public void setColumnCount(int cols) {
        this.columnCount = cols;
        this.invalidate();
    }

    public void setRowCount(int rows) {
        this.rowCount = rows;
        this.invalidate();
    }

    public void drawGridLines(boolean value) {
        this.drawGrid = value;
        this.invalidate();
    }
}