package com.alanbertadev.bigfinancekeyboard;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * TextView that squishes the text to fit within the width of the view
 */
public class SquishableTextView extends TextView {

    private final float FORCED_MAXIMUM_TEXT_SIZE = 65;
    private float currentTextSize = FORCED_MAXIMUM_TEXT_SIZE;
    private int currentWidth = 0;
    private DisplayMetrics metrics;

    public SquishableTextView(Context context) {
        super(context);
        init();
    }


    public SquishableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public SquishableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @TargetApi(21)
    public SquishableTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        metrics = Resources.getSystem().getDisplayMetrics();
    }

    private int getPixels(int unit, float size) {
        return (int)TypedValue.applyDimension(unit, size, metrics);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if(currentWidth!=0) {
            currentTextSize = FORCED_MAXIMUM_TEXT_SIZE;
            float textWidthMeasurement = currentWidth + 1;
            Paint paint = this.getPaint();
            while(textWidthMeasurement > currentWidth) {
                paint.setTextSize(getPixels(TypedValue.COMPLEX_UNIT_SP, currentTextSize));
                textWidthMeasurement = paint.measureText(text.toString());
                if(textWidthMeasurement > currentWidth) {
                    currentTextSize--;
                }
            }
        }
        this.setTextSize(currentTextSize);
        super.setText(text, type);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(this.getWidth() > 0) {
            currentWidth = this.getWidth();
        }
    }
}
