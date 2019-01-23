package com.dz.ninegridimages.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.Serializable;

/**
* creat_user: zhengzaihong
* Email:1096877329@qq.com
* creat_date: 2017/12/6
* creat_time: 10:28
* describe:支持圆角的view
**/
public class RoundIconView extends android.support.v7.widget.AppCompatImageView {

    public RoundIconView(Context context) {
        this(context,null);

    }

    public RoundIconView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private final RectF roundRect = new RectF();
    private float rect_adius = 0;
    private final Paint maskPaint = new Paint();
    private final Paint zonePaint = new Paint();
 
    private void init() {
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        zonePaint.setAntiAlias(true);
        zonePaint.setColor(Color.WHITE);
        float density = getResources().getDisplayMetrics().density;
        rect_adius = rect_adius * density;
    }
 
    public void setRectAdius(float adius) {
        rect_adius = adius;
        invalidate();
    }
 
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = getWidth();
        int h = getHeight();
        roundRect.set(0, 0, w, h);
    }
 
    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(roundRect, rect_adius, rect_adius, zonePaint);
        //
        canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
    }

}
