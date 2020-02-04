package me.zhixingye.salty.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by zhixingye on 2020年02月04日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class AlertDialog {

    private android.app.AlertDialog.Builder mDialogBuilder;
    private android.app.AlertDialog mAlertDialog;

    public AlertDialog(Context context) {
        mDialogBuilder = new android.app.AlertDialog.Builder(context);
        mDialogBuilder.setCancelable(true);
    }

    public void show() {
        mAlertDialog = mDialogBuilder.create();
        mAlertDialog.show();
    }

    public void dismiss() {
        mAlertDialog.dismiss();
    }

    public AlertDialog setTitle(CharSequence title) {
        mDialogBuilder.setTitle(title);
        return this;
    }

    public AlertDialog setMessage(CharSequence content) {
        mDialogBuilder.setMessage(content);
        return this;
    }

    public AlertDialog setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
        mDialogBuilder.setPositiveButton(text, listener);
        return this;
    }

    public AlertDialog setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
        mDialogBuilder.setNegativeButton(text, listener);
        return this;
    }

    public AlertDialog setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mDialogBuilder.setOnDismissListener(listener);
        return this;
    }
}
