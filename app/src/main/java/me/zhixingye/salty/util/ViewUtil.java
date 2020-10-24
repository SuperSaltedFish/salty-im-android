package me.zhixingye.salty.util;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年10月24日.
 */
public class ViewUtil {

    public static void setSupportsCancelChangeAnimations(RecyclerView rv, boolean supportsChangeAnimations) {
        RecyclerView.ItemAnimator animator = rv.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(supportsChangeAnimations);
        }
    }

}
