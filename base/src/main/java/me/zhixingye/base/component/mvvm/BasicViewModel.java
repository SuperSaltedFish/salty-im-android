package me.zhixingye.base.component.mvvm;

import android.content.DialogInterface;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import me.zhixingye.base.component.LifecycleUIComponent;
import me.zhixingye.base.view.SaltyToast;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2021年01月27日.
 */
public class BasicViewModel extends ViewModel {

    protected MutableLiveData<ToastData> mToastData = new MutableLiveData<>();
    protected MutableLiveData<LoadingDialogData> mLoadingDialogData = new MutableLiveData<>();
    protected MutableLiveData<HintDialogData> mHintDialogData = new MutableLiveData<>();
    protected MutableLiveData<ErrorData> mErrorData = new MutableLiveData<>();

    public void setToastData(@Nullable ToastData data) {
        postValue(mToastData, data);
    }

    public void setLoadingDialogData(@Nullable LoadingDialogData data) {
        postValue(mLoadingDialogData, data);
    }

    public void setHintDialogData(@Nullable HintDialogData data) {
        postValue(mHintDialogData, data);
    }

    public void setErrorData(@Nullable ErrorData data) {
        postValue(mErrorData, data);
    }

    public void observeBasic(LifecycleUIComponent component) {
        mToastData.observe(component, new Observer<ToastData>() {
            @Override
            public void onChanged(BasicViewModel.ToastData data) {
                if (data == null) {
                    return;
                }
                component.showShortToast(data.mToastContent, data.mToastType);
            }
        });

        mLoadingDialogData.observe(component, new Observer<LoadingDialogData>() {
            @Override
            public void onChanged(LoadingDialogData data) {
                if (data == null) {
                    component.hideLoadingDialog();
                    return;
                }
                component.showLoadingDialog(data.mLoadingText, data.isCancelable, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (data.mOnDismissListener != null) {
                            data.mOnDismissListener.onDismiss(dialog);
                        }
                        mLoadingDialogData.setValue(null);
                    }
                });
            }
        });

        mHintDialogData.observe(component, new Observer<HintDialogData>() {
            @Override
            public void onChanged(HintDialogData data) {
                if (data == null) {
                    return;
                }
                component.showHintDialog(data.mTitle, data.mContent, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (data.mOnDismissListener != null) {
                            data.mOnDismissListener.onDismiss(dialog);
                        }
                        mHintDialogData.setValue(null);
                    }
                });
            }
        });

        mErrorData.observe(component, new Observer<ErrorData>() {
            @Override
            public void onChanged(ErrorData data) {
                if (data == null || TextUtils.isEmpty(data.mErrorContent)) {
                    return;
                }
                component.showError(data.mErrorContent);
                mErrorData.setValue(null);
            }
        });
    }

    protected static <T> void postValue(MutableLiveData<T> liveData, T data) {
        if (liveData == null) {
            return;
        }
        if (Looper.getMainLooper().isCurrentThread()) {
            liveData.setValue(data);
        } else {
            liveData.postValue(data);
        }
    }

    public static class ErrorData {
        public final int mErrorCode;
        public final String mErrorContent;

        public ErrorData(String error) {
            this(0, error);
        }

        public ErrorData(int code, String error) {
            this.mErrorCode = code;
            this.mErrorContent = error;
        }
    }

    public static class ToastData {
        public final String mToastContent;
        public final int mToastType;

        public ToastData(String toastContent, @SaltyToast.ToastType int toastType) {
            this.mToastContent = toastContent;
            this.mToastType = toastType;
        }
    }

    public static class LoadingDialogData {
        public final String mLoadingText;
        public final boolean isCancelable;
        public final DialogInterface.OnDismissListener mOnDismissListener;

        public LoadingDialogData(
                @Nullable String text,
                boolean isCancelable,
                @Nullable DialogInterface.OnDismissListener listener) {
            this.mLoadingText = text;
            this.isCancelable = isCancelable;
            this.mOnDismissListener = listener;
        }
    }

    public static class HintDialogData {
        public final String mTitle;
        public final String mContent;
        public final DialogInterface.OnDismissListener mOnDismissListener;

        public HintDialogData(
                String title,
                String content,
                @Nullable DialogInterface.OnDismissListener listener) {
            this.mTitle = title;
            this.mContent = content;
            this.mOnDismissListener = listener;
        }
    }

    public static class LoadingStateLiveData extends LiveData<Boolean> {
        public void changeToShowLoadingState() {
            postValue(true);
        }

        public void changeToHideLoadingState() {
            postValue(false);
        }
    }
//
//    public static class ErrorLiveData extends LiveData<Boolean> {
//        public void changeToShowLoadingState() {
//            postValue(true);
//        }
//
//        public void changeToHideLoadingState() {
//            postValue(false);
//        }
//    }

}
