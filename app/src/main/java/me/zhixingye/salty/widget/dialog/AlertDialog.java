package me.zhixingye.salty.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
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
