package me.zhixingye.salty.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DecodeFormat;

import me.zhixingye.salty.R;
import me.zhixingye.salty.widget.view.GlideHexagonTransform;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */


public class GlideUtil {

    public static void clear(Context context, ImageView view) {
        Glide.with(context).clear(view);
    }

    public static void loadFromUrl(Context context, ImageView view, Object url) {
        loadFromUrl(context, view, url, null);
    }

    public static void loadFromUrl(Context context, ImageView view, Object url, Drawable defaultDrawable) {
        if (((url == null || (url instanceof CharSequence) && TextUtils.isEmpty((CharSequence) url)) && defaultDrawable == null) || view == null) {
            return;
        }

        clear(context, view);
        RequestBuilder<Drawable> glideRequest = Glide.with(context)
                .load(url)
                .dontAnimate()
                .format(DecodeFormat.PREFER_RGB_565);
        if (defaultDrawable != null) {
            glideRequest = glideRequest
                    .placeholder(defaultDrawable)
                    .error(defaultDrawable);
        }
        glideRequest.into(view);
    }

    private static final GlideHexagonTransform HEXAGON_TRANSFORM = new GlideHexagonTransform();

    public static void loadAvatarFromUrl(Context context, ImageView view, Object url) {
        if (view == null) {
            return;
        }
        clear(context, view);

        if (url == null) {
            url = R.drawable.ic_avatar_default;
        } else if (url instanceof String) {
            String strUrl = (String) url;
            if (TextUtils.isEmpty(strUrl) || "-".equals(strUrl)) {
                url = R.drawable.ic_avatar_default;
            }
        }

        RequestBuilder<Drawable> glideRequest = Glide.with(context)
                .load(url)
                .dontAnimate()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .circleCrop()
                .transform(HEXAGON_TRANSFORM)
                .error(R.drawable.ic_avatar_default);

        if (!(url instanceof Integer)) {
            glideRequest = glideRequest.placeholder(R.drawable.ic_avatar_default);
        }
        glideRequest.into(view);
    }

    public static void resumeRequests(Context context) {
        Glide.with(context).resumeRequests();
    }

    public static void pauseRequests(Context context) {
        Glide.with(context).pauseRequests();
    }

}
