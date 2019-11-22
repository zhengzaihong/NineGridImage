package com.dz.ninegridimages.preview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.dz.ninegridimages.R;
import com.dz.ninegridimages.config.SinglePicturePreConfig;
import com.dz.ninegridimages.util.SingleViewPreViewHelper;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * creat_user: zhengzaihong
 * Email:1096877329@qq.com
 * creat_date: 2019/11/22
 * creat_time: 14:40
 * describe 该界面时提供给非 九宫格显示大图的预览
 **/

public class BigPicturePreviewActivity extends Activity implements ViewTreeObserver.OnPreDrawListener {

    private Context mContext;

    private SinglePicturePreConfig singlePicturePreConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_pre_bg_image);
        mContext = this;

        ViewGroup rootView = this.findViewById(R.id.picturebg);

        final Bundle bundle = getIntent().getExtras();
        final PhotoView photoView = this.findViewById(R.id.photoview);

        ImageView loadAnimationView = this.findViewById(R.id.loadAnimationView);


        singlePicturePreConfig = SingleViewPreViewHelper.getInstance().getSinglePicturePreConfig();
        rootView.setBackgroundColor(singlePicturePreConfig.getPreBgColor());

        if (null != singlePicturePreConfig) {
            singlePicturePreConfig.getImageLoader().loadPreImage(this, loadAnimationView, photoView, bundle);
        }

        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (null != singlePicturePreConfig) {
                    singlePicturePreConfig.getImageLoader().longClick(mContext, photoView, bundle);
                }

                return false;
            }
        });


        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finishActivityAnim();
            }
        });

        loadAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivityAnim();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finishActivityAnim();
    }

    /**
     * 绘制前开始动画
     */
    @Override
    public boolean onPreDraw() {
        return true;
    }

    /**
     * activity的退场动画
     */
    public void finishActivityAnim() {

        finish();

    }
}
