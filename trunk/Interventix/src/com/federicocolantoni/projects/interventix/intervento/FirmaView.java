
package com.federicocolantoni.projects.interventix.intervento;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class FirmaView extends View {

    private Paint mPaint;
    private Path mPath;

    public FirmaView(Context context) {

	super(context);
	init(context);
    }

    public FirmaView(Context context, AttributeSet attrs) {

	super(context, attrs);
    }

    public FirmaView(Context context, AttributeSet attrs, int defStyle) {

	super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

	super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

	return super.onTouchEvent(event);
    }

    private void init(Context context) {

	mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	mPath = new Path();
    }

    @Override
    public void onDraw(Canvas can) {

    }
}
