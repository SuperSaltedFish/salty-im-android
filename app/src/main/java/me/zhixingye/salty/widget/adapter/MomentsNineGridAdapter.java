package me.zhixingye.salty.widget.adapter;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import androidx.core.content.ContextCompat;
import me.zhixingye.base.view.RoundImageView;
import me.zhixingye.salty.R;
import me.zhixingye.salty.util.AndroidHelper;
import me.zhixingye.salty.util.GlideUtil;
import me.zhixingye.salty.widget.view.NineGridView;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年06月02日.
 */
public class MomentsNineGridAdapter extends NineGridView.Adapter {

    public List<Object> mImageUrlList;
    private Drawable mDefaultDrawable;

    public MomentsNineGridAdapter(List<Object> imageUrlList) {
        mImageUrlList = imageUrlList;
    }

    @Override
    protected View createView(ViewGroup parent) {
        RoundImageView imageView = new RoundImageView(parent.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setRoundRadius(AndroidHelper.dip2px(4));

        if (mDefaultDrawable == null) {
            mDefaultDrawable = new ColorDrawable(ContextCompat.getColor(parent.getContext(), R.color.textColorTertiary));
        }
        imageView.setImageDrawable(mDefaultDrawable);
        return imageView;
    }

    @Override
    protected void setupImageResource(int position, View v) {
        GlideUtil.loadFromUrl(v.getContext(), (ImageView) v, mImageUrlList.get(position));
    }

    @Override
    protected void destroyView(View v) {
        GlideUtil.clear(v.getContext(), (ImageView) v);
    }

    @Override
    protected int getCount() {
        return mImageUrlList == null ? 0 : mImageUrlList.size();
    }
}
