package me.zhixingye.base.component.mvp;

import android.util.SparseArray;

import androidx.annotation.Nullable;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年10月05日.
 */
class PresenterCache {

    private static final SparseArray<IPresenter<?>> PRESENTER_MAP = new SparseArray<>();

    @SuppressWarnings("unchecked")
    @Nullable
    public static synchronized <T extends IPresenter<?>> T getPresenter(int hashCode) {
        return (T) PRESENTER_MAP.get(hashCode);
    }

    public static synchronized void savePresenter(int hashCode, IPresenter<?> presenter) {
        if (presenter != null) {
            PRESENTER_MAP.put(hashCode, presenter);
        }
    }

    public static synchronized void removePresenterFromCache(int hashCode) {
        PRESENTER_MAP.delete(hashCode);
    }

}
