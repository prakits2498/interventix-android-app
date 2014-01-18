package com.federicocolantoni.projects.interventix.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.federicocolantoni.projects.interventix.R;

public class DrawingView extends View {

	private Path drawPath;

	private Paint drawPaint, canvasPaint;

	private int paintColor = Color.BLACK;

	private Canvas drawCanvas;

	private Bitmap canvasBitmap;

	private boolean erase = false;

	public DrawingView(Context context) {

		super(context);
		setupDrawing();
	}

	public DrawingView(Context context, AttributeSet attrs) {

		super(context, attrs);
		setupDrawing();
	}

	private void setupDrawing() {

		drawPath = new Path();
		drawPaint = new Paint();

		drawPaint.setColor(paintColor);

		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(getResources().getDimension(R.dimen.small_brush));
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);

		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		super.onSizeChanged(w, h, oldw, oldh);

		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}

	public void setErase(boolean isErase) {

		erase = isErase;

		if (erase) {
			drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			drawPaint.setStrokeWidth(getResources().getDimension(R.dimen.small_brush) * 10);
			drawPaint.setColor(Color.WHITE);
		} else {
			drawPaint.setXfermode(null);
			drawPaint.setColor(Color.BLACK);
			drawPaint.setStrokeWidth(getResources().getDimension(R.dimen.small_brush));
		}
	}

	public void resetSignature() {

		drawPath = null;

		drawPaint = null;
		canvasPaint = null;

		drawCanvas = null;

		canvasBitmap = null;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float touchX = event.getX();
		float touchY = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			drawPath.moveTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE:
			drawPath.lineTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_UP:
			drawCanvas.drawPath(drawPath, drawPaint);
			drawPath.reset();
			break;
		default:
			return false;
		}

		invalidate();
		return true;
	}
}
