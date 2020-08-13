package com.dz.ninegridimages.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.dz.ninegridimages.config.NineGridViewConfigure;
import com.dz.ninegridimages.config.RadiusType;

import static com.dz.ninegridimages.config.RadiusType.*;


/**
 * create_user: zhengzaihong
 * Email:1096877329@qq.com
 * create_date: 2019/11/20
 * create_time: 16:42
 * describe 圆点指示器
 **/
public class IndicatorView extends AppCompatImageView {

    //记录当前View是选中还是未选中的 indicator

    private Boolean isSelectView = true;

    private NineGridViewConfigure config = new NineGridViewConfigure();

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 设置样式配置参数
     *
     * @param configBean
     */
    public void setConfig(NineGridViewConfigure configBean, boolean isSelectView) {
        this.config = configBean;
        this.isSelectView = isSelectView;
        // 设置背景
        initView();
    }

    public void changView(boolean isSelectView) {
        this.isSelectView = isSelectView;
        // 设置背景
        initView();
    }



    @SuppressLint("NewApi")
    private void initView() {

        int padding = config.getIndicatorBgPadding();

        this.setPadding(padding, padding, padding, padding);

        Drawable drawable = createShape(config);

        StateListDrawable stateListDrawable = new StateListDrawable();

        stateListDrawable.addState(new int[]{}, drawable);

        setBackground(stateListDrawable);
    }


    private GradientDrawable createShape(NineGridViewConfigure config) {
        GradientDrawable drawable = new GradientDrawable();
        RadiusType radiusType = config.getIndicatorRadiusType();
        float radius = config.getIndicatorBgRadius();
        if (radiusType == LEFT_TOP_BOTTOM_RADIUS) {
            //1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
            drawable.setCornerRadii(new float[]{radius, radius, 0, 0, 0, 0, radius, radius});
        } else if (radiusType == RIGHT_TOP_BOOTOM_RADIUS) {
            drawable.setCornerRadii(new float[]{0, 0, radius, radius, radius, radius, 0, 0});
        } else if (radiusType == LEFT_BOTTOM_RADIUS) {
            drawable.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, radius, radius});
        } else if (radiusType == LEFT_TOP_RADIUS) {
            drawable.setCornerRadii(new float[]{radius, radius, 0, 0, 0, 0, 0, 0});
        } else if (radiusType == RIGHT_TOP_RADIUS) {
            drawable.setCornerRadii(new float[]{0, 0, radius, radius, 0, 0, 0, 0});
        } else if (radiusType == RIGHT_BOOTOM_RADIUS) {
            drawable.setCornerRadii(new float[]{0, 0, 0, 0, radius, radius, 0, 0});
        } else if (radiusType == ALL_RADIUS) {
            //设置4个角的弧度
            drawable.setCornerRadius(radius);
        } else if (radiusType == NONE_RADIUS) {
            drawable.setCornerRadius(0);
        }
        // 设置背景颜色
        drawable.setColor(isSelectView?config.getSelectIndicatorBgColor():config.getUnSelectIndicatorBgColor());
        // 设置边框颜色
        drawable.setStroke(config.getIndicatorStrokeWidth(), config.getIndicatorStrokeColor());
        return drawable;
    }

}
