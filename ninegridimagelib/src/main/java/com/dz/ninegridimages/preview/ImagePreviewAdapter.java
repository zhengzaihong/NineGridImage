package com.dz.ninegridimages.preview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dz.ninegridimages.R;
import com.dz.ninegridimages.config.NineGridViewConfigure;
import com.dz.ninegridimages.view.NineGridView;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * creat_user: zhengzaihong
 * email:1096877329@qq.com
 * creat_date: 2019/1/22 0022
 * creat_time: 16:11
 * describe: 大图的适配器
 **/

@SuppressWarnings("all")
public class ImagePreviewAdapter<T> extends PagerAdapter implements PhotoViewAttacher.OnPhotoTapListener {

    private List<T> imageInfo;
    private Context context;
    private View currentView;
    private NineGridViewConfigure configure;

    public ImagePreviewAdapter(Context context, NineGridViewConfigure configure, @NonNull List<T> imageInfo) {
        super();
        this.imageInfo = imageInfo;
        this.context = context;
        this.configure = configure;
    }


    @Override
    public int getCount() {
        return imageInfo.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentView = (View) object;

    }

    public View getPrimaryItem() {
        return currentView;
    }

    public ImageView getPrimaryImageView() {
        return (ImageView) currentView;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final PhotoView imageView = (PhotoView) LayoutInflater.from(context).inflate(R.layout.item_photoview, container, false);

        imageView.setAdjustViewBounds(true);
        imageView.setOnPhotoTapListener(this);
        final T info = imageInfo.get(position);
        showExcessPic(imageView);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (null != configure.getOnPreLongClickListener()) {
                    configure.getOnPreLongClickListener().onImageLongClick(imageView, position, info);
                }
                return false;
            }
        });

        configure.getImageLoader().loadPreImage(context, imageView, info);
        container.addView(imageView);
        return imageView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof PhotoView) {
            PhotoView view = (PhotoView) object;
            view.setImageDrawable(null);
            view.setBackground(null);
            container.removeView((View) view);
            releaseImageViewResouce((ImageView) view);

        }
    }

    /**
     * 展示过度图片
     */
    private void showExcessPic(PhotoView imageView) {
        imageView.setImageResource(R.drawable.ic_default_color);
    }

    /**
     * 单击屏幕关闭
     */
    @Override
    public void onPhotoTap(View view, float x, float y) {
        ((ImagePreviewActivity) context).finishActivityAnim();

        releaseImageViewResouce((ImageView) view);

    }

    /**
     * 释放图片资源的方法
     *
     * @param imageView
     */
    public void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        }
        System.gc();
    }


}