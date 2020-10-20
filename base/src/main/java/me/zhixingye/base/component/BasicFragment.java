package me.zhixingye.base.component;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */


public abstract class BasicFragment
        extends Fragment
        implements UIComponent {

    @LayoutRes
    protected abstract int getLayoutID();

    protected abstract void init(View rootView);

    protected abstract void setup(Bundle savedInstanceState);

    public Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = null;
        int layoutId = getLayoutID();
        if (layoutId != 0) {
            v = inflater.inflate(layoutId, container, false);
        }
        return v;
    }

    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setup(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

}
