package com.dz.ninegridimages.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dz.ninegridimages.adapter.NineGridViewAdapter;
import com.dz.ninegridimages.config.NineGridViewConfigure;
import com.dz.ninegridimages.interfaces.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * creat_user: zhengzaihong
 * email:1096877329@qq.com
 * creat_date: 2019/1/17 0017
 * creat_time: 16:11
 * describe: 网格视图
 **/

@SuppressWarnings("all")
public class NineGridView extends RelativeLayout {

    private NineGridViewAdapter nineGridViewAdapter;

    private List<ImageView> imageViews;
    private List<Object> mImageInfo;
    //全局的图片加载器(必须设置,否者不显示图片)
    private static ImageLoader mImageLoader;

    public static final int MODE_FILL = 0;          //填充模式，类似于微信
    public static final int MODE_GRID = 1;          //网格模式，类似于QQ，4张图会 2X2布局

    private int columnNum = 3;

    private int singleImageSize = 250;              // 单张图片时的最大大小,单位px
    private boolean singleFixed = false;             //单张照片时是否固定宽高
    private float singleImageRatio = 1.0f;          // 单张图片的宽高比(宽/高)
    private int maxImageSize = 9;                   // 最大显示的图片数
    private int gridSpacing = 3;                    // 宫格间距，单位px
    private int mode = MODE_FILL;                   // 默认使用fill模式
    private int rectAdius;
    private float textSize = 35;          //显示文字的大小单位sp
    private int textColor = 0xFFFFFFFF;   //显示文字的颜色
    private int preIndicatorMargin = 4;   //设置指示器间距
    private int preTipTextColor = Color.WHITE;   // 设置指示器文本颜色
    private int preBgColor = Color.BLACK;   // 设置指示器文本颜色
    private int preTipTextSize = 22;   //设置指示器字体大小

    private int columnCount;    // 列数
    private int rowCount;       // 行数
    private int gridWidth;      // 宫格宽度
    private int gridHeight;     // 宫格高度

    //开启预览
    public boolean enablePre = true;


    private Context mContext;

    public NineGridView(Context context) {
        this(context, null);
    }

    public NineGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        initConfig(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NineGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        initConfig(attrs);
    }


    /**
     * 初始化用户配置参数
     */
    private void initConfig(AttributeSet attrs) {
//        全部通过NineGridViewConfigure 配置  不再使用属性配置
//        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.NineGridView);
//        singleImageSize = (int) a.getDimension(R.styleable.NineGridView_nineSingleImageSize, singleImageSize);
//        singleImageRatio = a.getFloat(R.styleable.NineGridView_nineSingleImageRatio, singleImageRatio);
//        gridSpacing = (int) a.getDimension(R.styleable.NineGridView_nineGridSpacing, gridSpacing);
//        maxImageSize = a.getInt(R.styleable.NineGridView_nineMaxSize, maxImageSize);
//        mode = a.getInt(R.styleable.NineGridView_nineMode, mode);
//
//        rectAdius = (int) a.getDimension(R.styleable.NineGridView_nineImageRectAdius, rectAdius);
//        columnNum = a.getInt(R.styleable.NineGridView_nineColumnNum, columnNum);
//        singleFixed = a.getBoolean(R.styleable.NineGridView_nineSingleFixed, singleFixed);
//        textColor = a.getColor(R.styleable.NineGridView_nineMoreTextColor, textColor);
//        textSize = a.getDimension(R.styleable.NineGridView_nineMoreTextSize, textSize);
//        preIndicatorMargin = (int) a.getDimension(R.styleable.NineGridView_preIndicatorMargin, preIndicatorMargin);
//        preTipTextSize = (int) a.getDimension(R.styleable.NineGridView_preTipTextSize, preTipTextSize);
//        preTipTextColor = a.getColor(R.styleable.NineGridView_preTipTextColor, preTipTextColor);
//        preBgColor = a.getColor(R.styleable.NineGridView_preBgColor, preBgColor);
//        a.recycle();

        imageViews = new ArrayList<>();


    }


    /**
     * 获得 ImageView 保证了 ImageView 的重用
     */
    private ImageView getImageView(final int position) {
        RoundIconView imageView;
        if (position < imageViews.size()) {
            imageView = (RoundIconView) imageViews.get(position);
        } else {
            imageView = (RoundIconView) nineGridViewAdapter.generateImageView(getContext());
            if (rectAdius > 0)
                imageView.setRectAdius(rectAdius);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    nineGridViewAdapter.onImageItemClick(getContext(), NineGridView.this, position, nineGridViewAdapter.getImage());
                }
            });
            imageViews.add(imageView);
        }
        return imageView;
    }


    public NineGridView setNineAdapter(@NonNull NineGridViewAdapter adapter) {
        this.nineGridViewAdapter = adapter;

        List<Object> imageInfo = adapter.getImage();
        if (imageInfo == null || imageInfo.isEmpty()) {
            setVisibility(GONE);
            return this;
        } else {
            setVisibility(VISIBLE);
        }

        int imageCount = imageInfo.size();
        if (maxImageSize > 0 && imageCount > maxImageSize) {
            imageInfo = imageInfo.subList(0, maxImageSize);
            imageCount = imageInfo.size();   //再次获取图片数量
        }

        rowCount = imageCount / columnNum + (imageCount % columnNum == 0 ? 0 : 1);
        columnCount = columnNum;
        //grid模式下，显示4张使用2X2模式
        if (mode == MODE_GRID) {
            if (imageCount == 4) {
                rowCount = 2;
                columnCount = 2;
            }
        }
        //保证View的复用，避免重复创建
        if (mImageInfo == null) {
            for (int i = 0; i < imageCount; i++) {
                ImageView iv = getImageView(i);
                if (iv == null) return this;
                if (imageCount == 1) {
                    RelativeLayout.LayoutParams layoutParams =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    iv.setLayoutParams(layoutParams);

                    LayoutParams layoutParams1 = (LayoutParams) generateDefaultLayoutParams();
                    layoutParams1.width = LayoutParams.MATCH_PARENT;
                    addView(iv, layoutParams1);
                } else {
                    addView(iv, generateDefaultLayoutParams());
                }

            }
        } else {
            int oldViewCount = mImageInfo.size();
            int newViewCount = imageCount;
            if (oldViewCount > newViewCount) {
                removeViews(newViewCount, oldViewCount - newViewCount);
            } else if (oldViewCount < newViewCount) {
                for (int i = oldViewCount; i < newViewCount; i++) {
                    ImageView iv = getImageView(i);
                    if (iv == null) return this;
                    addView(iv, generateDefaultLayoutParams());
                }
            }
        }
        //修改最后一个条目，决定是否显示更多
        if (nineGridViewAdapter.getImage().size() > maxImageSize) {
            View child = getChildAt(maxImageSize - 1);
            if (child instanceof NineGridViewWrapper) {
                NineGridViewWrapper imageView = (NineGridViewWrapper) child;
                imageView.setMoreNum(nineGridViewAdapter.getImage().size() - maxImageSize);
                imageView.setTextColor(textColor);
                imageView.setTextSize(textSize);
            }
        }

        this.mImageInfo = imageInfo;
        requestLayout();
        return this;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        int totalWidth = width - getPaddingLeft() - getPaddingRight();
        if (mImageInfo != null && mImageInfo.size() > 0) {
            if (mImageInfo.size() == 1) {
                gridWidth = singleImageSize > totalWidth ? totalWidth : singleImageSize;
                gridHeight = (int) (gridWidth / singleImageRatio);
                //矫正图片显示区域大小，不允许超过最大显示范围
                if (gridHeight > singleImageSize) {

                    if (!singleFixed) {
                        float ratio = singleImageSize * 1.0f / gridHeight;
                        gridWidth = (int) (gridWidth * ratio);
                    }

                    gridHeight = singleImageSize;
                }
            } else {
//                gridWidth = gridHeight = (totalWidth - gridSpacing * (columnCount - 1)) / columnCount;
                //这里无论是几张图片，宽高都按总宽度的 1/columnCount
                gridWidth = gridHeight = (totalWidth - gridSpacing * 2) / columnCount;
            }
            width = gridWidth * columnCount + gridSpacing * (columnCount - 1) + getPaddingLeft() + getPaddingRight();
            height = gridHeight * rowCount + gridSpacing * (rowCount - 1) + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mImageInfo == null) return;
        int childrenCount = mImageInfo.size();
        for (int i = 0; i < childrenCount; i++) {
            ImageView childrenView = (ImageView) getChildAt(i);

            int rowNum = i / columnCount;
            int columnNum = i % columnCount;
            int left = (gridWidth + gridSpacing) * columnNum + getPaddingLeft();
            int top = (gridHeight + gridSpacing) * rowNum + getPaddingTop();
            int right = left + gridWidth;
            int bottom = top + gridHeight;
            childrenView.layout(left, top, right, bottom);

            if (mImageLoader != null) {
                mImageLoader.onDisplayImage(getContext(), childrenView, mImageInfo.get(i));
            }
        }
    }


    public NineGridView bindConfigure(NineGridViewConfigure configure) {
        if (null != configure) {
            this.mImageLoader = configure.getImageLoader();
            //属性配置
            this.gridSpacing = configure.getGridSpacing();
            this.maxImageSize = configure.getMaxImageSize();
            this.singleImageRatio = configure.getSingleImageRatio();
            this.singleImageSize = configure.getSingleImageSize();
            this.rectAdius = configure.getRectAdius();
            this.columnNum = configure.getColumnNum();
            this.textColor = configure.getMoreTextColor();
            this.textSize = configure.getMoreTextSize();
            this.mode = configure.getMode();
            this.enablePre = configure.isEnablePre();
        }
        return this;
    }


    public int getMaxSize() {
        return maxImageSize;
    }

    public static ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
