package com.dz.ninegridimages.preview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dz.ninegridimages.R;
import com.dz.ninegridimages.bean.BaseImageBean;
import com.dz.ninegridimages.config.NineGridViewConfigure;
import com.dz.ninegridimages.util.NineGridViewHelper;
import com.dz.ninegridimages.view.IndicatorView;

/**
 * creat_user: zhengzaihong
 * email:1096877329@qq.com
 * creat_date: 2019/1/22 0022
 * creat_time: 16:11
 * describe: 预览视图界面
 **/

@SuppressWarnings("all")
public class ImagePreviewActivity extends Activity implements ViewTreeObserver.OnPreDrawListener {

    public static final String IMAGE_INFO = "IMAGE_INFO";
    public static final String CURRENT_ITEM = "CURRENT_ITEM";
    public static final int ANIMATE_DURATION = 200;

    private RelativeLayout rootView;
    private ImagePreviewAdapter imagePreviewAdapter;
    private BaseImageBean imageInfo;
    private int currentItem;
    private int imageHeight;
    private int imageWidth;
    private int screenWidth;
    private int screenHeight;

    private Context mContext;
    private ImageView[] preImageViews;

    private NineGridViewConfigure configure;

    private ViewPager viewPager;
    private TextView tvPager;
    private LinearLayout llIndicator;

    private ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
    @SuppressLint("StringFormatMatches")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        mContext = this;
        viewPager = findViewById(R.id.viewPager);
        tvPager = findViewById(R.id.tv_pager);
        rootView = findViewById(R.id.rootView);
        llIndicator = findViewById(R.id.llIndicator);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;

        Intent intent = getIntent();
        imageInfo = ((BaseImageBean) intent.getSerializableExtra(IMAGE_INFO));
        currentItem = intent.getIntExtra(CURRENT_ITEM, 0);

        configure = NineGridViewHelper.getInstance().getNineGridViewConfigure();
        imagePreviewAdapter = new ImagePreviewAdapter(this, configure, imageInfo.getDatas());
        final int[] indicator = configure.getIndicator();
        viewPager.setAdapter(imagePreviewAdapter);
        viewPager.setCurrentItem(currentItem);
        viewPager.getViewTreeObserver().addOnPreDrawListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                if (configure.isEnableIndicatorDot()) {
                    if (null != indicator) {
                        for (int i = 0; i < preImageViews.length; i++) {
                            preImageViews[i].setBackgroundResource(i == position ? indicator[0] : indicator[1]);
                        }
                    } else {
                        //刷新View
                        for (int i = 0; i < preImageViews.length; i++) {
                            IndicatorView indicatorView = (IndicatorView) preImageViews[i];
                            indicatorView.changView(i == position);
                        }
                    }
                } else {
                    tvPager.setText(String.format(getString(R.string.select), currentItem + 1, imageInfo.getDatas().size()));
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //是否开启 小圆点指示器
        if (configure.isEnableIndicatorDot()) {

            llIndicator.setVisibility(View.VISIBLE);
            tvPager.setVisibility(View.GONE);
            preImageViews = new ImageView[imageInfo.getDatas().size()];

            //如果是 xml 方式配置的指示器(优先权最高)
            if (null != indicator) {
                for (int i = 0; i < imageInfo.getDatas().size(); i++) {
                    ImageView imageView = new ImageView(mContext);
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(15, 15));
                    preImageViews[i] = imageView;
                    preImageViews[i].setBackgroundResource(i == currentItem ? indicator[0] : indicator[1]);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    layoutParams.leftMargin = configure.getIndicatorMargin();
                    llIndicator.addView(imageView, layoutParams);
                }
            } else {

                //代码方式
                for (int i = 0; i < imageInfo.getDatas().size(); i++) {
                    IndicatorView imageView = new IndicatorView(mContext);
                    int boxSize = configure.getIndicatorSize();
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(boxSize, boxSize));

                    //默认第一个是选中
                    imageView.setConfig(configure, currentItem == i);
                    preImageViews[i] = imageView;

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    layoutParams.leftMargin = configure.getIndicatorMargin();
                    llIndicator.addView(imageView, layoutParams);
                }
            }

        } else {
            tvPager.setTextSize(configure.getPreTipTextSize());
            tvPager.setTextColor(configure.getPreTipColor());
            tvPager.setText(String.format(getString(R.string.select), currentItem + 1, imageInfo.getDatas().size()));
        }

    }

    @Override
    public void onBackPressed() {
        finishActivityAnim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        valueAnimator.cancel();
        System.gc();
    }

    /**
     * 绘制前开始动画
     */
    @Override
    public boolean onPreDraw() {
        rootView.getViewTreeObserver().removeOnPreDrawListener(this);
        final View view = imagePreviewAdapter.getPrimaryItem();
        final ImageView imageView = imagePreviewAdapter.getPrimaryImageView();

        computeImageWidthAndHeight(imageView);

//        final BaseImageBean imageData = imageInfo.get(currentItem);
        final BaseImageBean imageData = imageInfo;
        final float vx = imageData.imageViewWidth * 1.0f / imageWidth;
        final float vy = imageData.imageViewHeight * 1.0f / imageHeight;

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                long duration = animation.getDuration();
                long playTime = animation.getCurrentPlayTime();
                float fraction = duration > 0 ? (float) playTime / duration : 1f;
                if (fraction > 1) fraction = 1;
                view.setTranslationX(evaluateInt(fraction, imageData.imageViewX + imageData.imageViewWidth / 2 - imageView.getWidth() / 2, 0));
                view.setTranslationY(evaluateInt(fraction, imageData.imageViewY + imageData.imageViewHeight / 2 - imageView.getHeight() / 2, 0));
                view.setScaleX(evaluateFloat(fraction, vx, 1));
                view.setScaleY(evaluateFloat(fraction, vy, 1));
                view.setAlpha(fraction);
                rootView.setBackgroundColor(evaluateArgb(fraction, Color.TRANSPARENT, configure.getPreBgColor()));
            }
        });
        addIntoListener(valueAnimator);
        valueAnimator.setDuration(ANIMATE_DURATION);
        valueAnimator.start();
        return true;
    }

    /**
     * activity的退场动画
     */
    public void finishActivityAnim() {
        final View view = imagePreviewAdapter.getPrimaryItem();
        final ImageView imageView = imagePreviewAdapter.getPrimaryImageView();
        computeImageWidthAndHeight(imageView);

        final BaseImageBean imageData = imageInfo;
        final float vx = imageData.imageViewWidth * 1.0f / imageWidth;
        final float vy = imageData.imageViewHeight * 1.0f / imageHeight;
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                long duration = animation.getDuration();
                long playTime = animation.getCurrentPlayTime();
                float fraction = duration > 0 ? (float) playTime / duration : 1f;
                if (fraction > 1) fraction = 1;
                view.setTranslationX(evaluateInt(fraction, 0, imageData.imageViewX + imageData.imageViewWidth / 2 - imageView.getWidth() / 2));
                view.setTranslationY(evaluateInt(fraction, 0, imageData.imageViewY + imageData.imageViewHeight / 2 - imageView.getHeight() / 2));
                view.setScaleX(evaluateFloat(fraction, 1, vx));
                view.setScaleY(evaluateFloat(fraction, 1, vy));
                view.setAlpha(1 - fraction);
                rootView.setBackgroundColor(evaluateArgb(fraction, Color.BLACK, Color.TRANSPARENT));
            }

        });
        addOutListener(valueAnimator);
        valueAnimator.setDuration(ANIMATE_DURATION);
        valueAnimator.start();
    }

    /**
     * 计算图片的宽高
     */
    private void computeImageWidthAndHeight(ImageView imageView) {

        // 获取真实大小
        Drawable drawable = imageView.getDrawable();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        int intrinsicWidth = drawable.getIntrinsicWidth();
        // 计算出与屏幕的比例，用于比较以宽的比例为准还是高的比例为准，因为很多时候不是高度没充满，就是宽度没充满
        float h = screenHeight * 1.0f / intrinsicHeight;
        float w = screenWidth * 1.0f / intrinsicWidth;
        if (h > w) h = w;
        else w = h;

        // 得出当宽高至少有一个充满的时候图片对应的宽高
        imageHeight = (int) (intrinsicHeight * h);
        imageWidth = (int) (intrinsicWidth * w);
        drawable = null;
    }

    /**
     * 进场动画过程监听
     */
    private void addIntoListener(ValueAnimator valueAnimator) {
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                rootView.setBackgroundColor(0x0);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * 退场动画过程监听
     */
    private void addOutListener(ValueAnimator valueAnimator) {
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                rootView.setBackgroundColor(0x0);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ImagePreviewActivity.this.finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * Integer 估值器
     */
    public Integer evaluateInt(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int) (startInt + fraction * (endValue - startInt));
    }

    /**
     * Float 估值器
     */
    public Float evaluateFloat(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    /**
     * Argb 估值器
     */
    public int evaluateArgb(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return (startA + (int) (fraction * (endA - startA))) << 24//
                | (startR + (int) (fraction * (endR - startR))) << 16//
                | (startG + (int) (fraction * (endG - startG))) << 8//
                | (startB + (int) (fraction * (endB - startB)));
    }


}
