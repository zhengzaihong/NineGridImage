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

import static com.dz.ninegridimages.config.NineGridViewConfigure.MODE_GRID;

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

    //全局的图片加载器(必须设置,否者不回调不显示图片)
    private ImageLoader mImageLoader;

    private int columnCount;    // 列数
    private int rowCount;       // 行数
    private int gridWidth;      // 宫格宽度
    private int gridHeight;     // 宫格高度


    private Context mContext;

    private static NineGridViewConfigure configure;

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
        configure = new NineGridViewConfigure();
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
            if (configure.getRectAdius() > 0)
                imageView.setRectAdius(configure.getRectAdius());
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
        if (configure.getMaxImageSize() > 0 && imageCount > configure.getMaxImageSize()) {
            imageInfo = imageInfo.subList(0, configure.getMaxImageSize());
            imageCount = imageInfo.size();   //再次获取图片数量
        }

        rowCount = imageCount / configure.getColumnNum() + (imageCount % configure.getColumnNum() == 0 ? 0 : 1);
        columnCount = configure.getColumnNum();
        //grid模式下，显示4张使用2X2模式
        if (configure.getMode() == MODE_GRID) {
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
        if (nineGridViewAdapter.getImage().size() > configure.getMaxImageSize()) {
            View child = getChildAt(configure.getMaxImageSize() - 1);
            if (child instanceof NineGridViewWrapper) {
                NineGridViewWrapper imageView = (NineGridViewWrapper) child;
                imageView.setMoreNum(nineGridViewAdapter.getImage().size() - configure.getMaxImageSize());
                imageView.setTextColor(configure.getMoreTextColor());
                imageView.setTextSize(configure.getMoreTextSize());
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
                gridWidth = configure.getSingleImageSize() > totalWidth ? totalWidth : configure.getSingleImageSize();
                gridHeight = (int) (gridWidth / configure.getSingleImageRatio());
                //矫正图片显示区域大小，不允许超过最大显示范围
                if (gridHeight > configure.getSingleImageSize()) {

                    if (!configure.isSingleFixed()) {
                        float ratio = configure.getSingleImageSize() * 1.0f / gridHeight;
                        gridWidth = (int) (gridWidth * ratio);
                    }

                    gridHeight = configure.getSingleImageSize();
                }
            } else {
//                gridWidth = gridHeight = (totalWidth - gridSpacing * (columnCount - 1)) / columnCount;
                //这里无论是几张图片，宽高都按总宽度的 1/columnCount
                gridWidth = gridHeight = (totalWidth - configure.getGridSpacing() * 2) / columnCount;
            }
            width = gridWidth * columnCount + configure.getGridSpacing() * (columnCount - 1) + getPaddingLeft() + getPaddingRight();
            height = gridHeight * rowCount + configure.getGridSpacing() * (rowCount - 1) + getPaddingTop() + getPaddingBottom();
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
            int left = (gridWidth + configure.getGridSpacing()) * columnNum + getPaddingLeft();
            int top = (gridHeight + configure.getGridSpacing()) * rowNum + getPaddingTop();
            int right = left + gridWidth;
            int bottom = top + gridHeight;
            childrenView.layout(left, top, right, bottom);

            if (mImageLoader != null) {
                mImageLoader.displayImage(getContext(), childrenView, mImageInfo.get(i));
            }
        }
    }


    public NineGridView bindConfigure(NineGridViewConfigure configure) {
        if (null != configure) {
            this.mImageLoader = configure.getImageLoader();
            this.configure = configure;
        }
        return this;
    }

    public static NineGridViewConfigure getConfigure() {
        return configure;
    }


}
