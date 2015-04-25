package com.example.nyuscps.rocket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class RocketView extends View {
    Paint paint = new Paint();
    private float radius;
    private float x;    //location of animal
    private PointF rocketPos = new PointF();
    private PointF rocketDim = new PointF();
    private boolean first = true;


    public RocketView(Context context) {
        super(context);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);	//vs. STROKE
    }

    public RocketView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);	//vs. STROKE
    }

    public RocketView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);	//vs. STROKE
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("myTag", "onLayout " + left + " " + top + " " + right + " " + bottom);
        radius = getHeight() / 10f;
        x = radius;
        rocketPos.set(getWidth() - (int)rocketDim.x, getHeight() - radius);
        rocketDim.set((int)(radius / 2f), (int)(radius / 10f));
    }

    private class RollRunnable implements Runnable {
        //This method executed by second thread.
        @Override
        public void run() {
            for (;;) {
                RocketView.this.postInvalidate();
                ++x;

                final float x1 = x, y1 = getHeight() - radius; //center of animal
                final float x2 = rocketPos.x, y2 = rocketPos.y;

                if (x >= getWidth() - radius
                        || Math.hypot(x1 - x2, y1 - y2) <= radius) {
                    break;
                }


                try {
                    Thread.sleep(40L);   //milliseconds
                } catch (InterruptedException interruptedException) {
                }
            }
        }
    }

    //Called from listener in button.
    public void launchTheRocket() {
        RocketRunnable rocketRunnable = new RocketRunnable();
        Thread thread = new Thread(rocketRunnable);
        thread.start();
    }

    private class RocketRunnable implements Runnable {
        //This method executed by second thread.
        @Override
        public void run() {
            for (;;) {
                final float x1 = x, y1 = getHeight() - radius; //center of animal
                final float x2 = rocketPos.x, y2 = rocketPos.y;

                if (Math.hypot(x1 - x2, y1 - y2) <= radius) {
                        break;
                }
                RocketView.this.postInvalidate();
                rocketPos.x -= 2f;
                try {
                    Thread.sleep(40L);   //milliseconds
                } catch (InterruptedException interruptedException) {
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);	//background
        canvas.drawCircle(x, getHeight() - radius, radius, paint);
        Rect r = new Rect(
                (int)rocketPos.x,   //left
                (int)rocketPos.y,   //top
                (int)(rocketPos.x + rocketDim.x),    //right
                (int)(rocketPos.y + rocketDim.y)    //bottom
                );
        canvas.drawRect(r, paint);

        if (first) {
            first = false;

            //Could do it all in one statement:
            //new Thread(new RollRunnable()).start();

            RollRunnable rollRunnable = new RollRunnable();
            Thread thread = new Thread(rollRunnable);
            thread.start();
        }
    }
}
