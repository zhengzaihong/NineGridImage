package com.dz.ninegridimages.preview;

import android.content.Context;
import android.graphics.Bitmap;
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

public class ImagePreviewAdapter<T> extends PagerAdapter implements PhotoViewAttacher.OnPhotoTapListener {

    private List<T> imageInfo;
    private Context context;
    private View currentView;
    private NineGridViewConfigure configure;

    public ImagePreviewAdapter(Context context, @NonNull List<T> imageInfo) {
        super();
        this.imageInfo = imageInfo;
        this.context = context;
        this.configure = NineGridView.getConfigure();
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
        return (ImageView) currentView.findViewById(R.id.pv);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photoview, container, false);

        final PhotoView imageView = (PhotoView) view.findViewById(R.id.pv);
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
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
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
    }

}