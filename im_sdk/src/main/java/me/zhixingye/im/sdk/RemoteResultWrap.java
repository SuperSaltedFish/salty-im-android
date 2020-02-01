package me.zhixingye.im.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by zhixingye on 2020年01月31日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class RemoteResultWrap implements Parcelable {

    private Parcelable result;

    public RemoteResultWrap() {
    }

    public RemoteResultWrap(Parcelable result) {
        this.result = result;
    }

    public Parcelable getResult() {
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.result, flags);
    }

    protected RemoteResultWrap(Parcel in) {
        this.result = in.readParcelable(Parcelable.class.getClassLoader());

        String className = in.readString();
        if (!TextUtils.isEmpty(className)) {
            try {
                Class aClass = Class.forName(className);
                this.result = in.readParcelable(aClass.getClassLoader());
            } catch (ClassNotFoundException var5) {
                var5.printStackTrace();
            }
        }
    }

    public static final Parcelable.Creator<RemoteResultWrap> CREATOR = new Parcelable.Creator<RemoteResultWrap>() {
        @Override
        public RemoteResultWrap createFromParcel(Parcel source) {
            return new RemoteResultWrap(source);
        }

        @Override
        public RemoteResultWrap[] newArray(int size) {
            return new RemoteResultWrap[size];
        }
    };
}
