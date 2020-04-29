package me.zhixingye.salty.util;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * Created by zhixingye on 2020年02月21日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class PermissionHelper {

    public static void requestExternalStoragePermissions(
            FragmentManager manager,
            OnPermissionsResult resultCallback,
            boolean isShowPermissionHintDialog) {
        String[] externalStoragePermissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermissions(
                manager,
                externalStoragePermissions,
                resultCallback,
                null,
                null,
                isShowPermissionHintDialog);
    }

    public static void requestCameraPermissions(
            FragmentManager manager,
            OnPermissionsResult resultCallback,
            boolean isShowPermissionHintDialog) {
        String[] cameraPermissions = new String[]{
                Manifest.permission.CAMERA};
        requestPermissions(
                manager,
                cameraPermissions,
                resultCallback,
                null,
                null,
                isShowPermissionHintDialog);
    }

    public static void requestPermissions(
            FragmentManager manager,
            String[] permissions,
            OnPermissionsResult resultCallback,
            PermissionHintDialogInfo againRequestDialogInfo,
            PermissionHintDialogInfo settingDialogInfo,
            boolean isShowPermissionHintDialog) {

        PermissionFragment fragment = PermissionFragment.newInstance(
                permissions,
                againRequestDialogInfo,
                settingDialogInfo,
                isShowPermissionHintDialog);

        fragment.setPermissionsResult(resultCallback);

        manager.beginTransaction()
                .add(fragment, PermissionFragment.TAG)
                .commitAllowingStateLoss();
    }

    public static boolean checkPermission(Context context, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return false;
        }
        context = context.getApplicationContext();
        boolean isGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }
        return isGranted;
    }

    public static String[] getDeniedPermission(Context context, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return new String[0];
        }
        context = context.getApplicationContext();
        List<String> deniedPermissions = new ArrayList<>(permissions.length);
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions.toArray(new String[0]);
    }


    public interface OnPermissionsResult {
        void onGranted();

        void onDenied(String[] deniedPermissions);
    }

    public static class PermissionHintDialogInfo implements Parcelable {
        public String title;
        public String message;
        public String negativeBtnText;
        public String positiveBtnText;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.message);
            dest.writeString(this.negativeBtnText);
            dest.writeString(this.positiveBtnText);
        }

        public PermissionHintDialogInfo() {
        }

        protected PermissionHintDialogInfo(Parcel in) {
            this.title = in.readString();
            this.message = in.readString();
            this.negativeBtnText = in.readString();
            this.positiveBtnText = in.readString();
        }

        public static final Parcelable.Creator<PermissionHintDialogInfo> CREATOR = new Parcelable.Creator<PermissionHintDialogInfo>() {
            @Override
            public PermissionHintDialogInfo createFromParcel(Parcel source) {
                return new PermissionHintDialogInfo(source);
            }

            @Override
            public PermissionHintDialogInfo[] newArray(int size) {
                return new PermissionHintDialogInfo[size];
            }
        };
    }

    public static class PermissionFragment extends Fragment {

        private static final String TAG = "PermissionFragment";
        private static final String EXTRA_PERMISSIONS = "Permission";
        private static final String EXTRA_IS_SHOW_HINT_DIALOG = "isShowHintDialog";
        private static final String EXTRA_AGAIN_REQUEST_DIALOG_INFO = "AgainRequestDialogInfo";
        private static final String EXTRA_SETTING_DIALOG_INFO = "SettingDialogInfo";

        private static final int PERMISSION_REQUEST_CODE = 10001;
        private static final int PERMISSION_SETTING_CODE = 10002;

        private static PermissionFragment newInstance(
                String[] permissions,
                PermissionHintDialogInfo againRequestDialogInfo,
                PermissionHintDialogInfo settingDialogInfo,
                boolean isShowPermissionHintDialog) {
            Bundle args = new Bundle();
            args.putStringArray(EXTRA_PERMISSIONS, permissions);
            args.putParcelable(EXTRA_AGAIN_REQUEST_DIALOG_INFO, againRequestDialogInfo);
            args.putParcelable(EXTRA_SETTING_DIALOG_INFO, settingDialogInfo);
            args.putBoolean(EXTRA_IS_SHOW_HINT_DIALOG, isShowPermissionHintDialog);
            PermissionFragment fragment = new PermissionFragment();
            fragment.setArguments(args);
            return fragment;
        }

        private String[] mPermissions;
        private PermissionHintDialogInfo mAgainRequestDialogInf;
        private PermissionHintDialogInfo mSettingDialogInfo;
        private boolean isShowPermissionHintDialog;

        private OnPermissionsResult mPermissionsResult;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            initArguments();
            requestPermissionsIfNeed();
        }

        private void initArguments() {
            Bundle bundle = getArguments();
            if (bundle == null) {
                return;
            }
            mAgainRequestDialogInf = bundle.getParcelable(EXTRA_AGAIN_REQUEST_DIALOG_INFO);
            mSettingDialogInfo = bundle.getParcelable(EXTRA_SETTING_DIALOG_INFO);
            isShowPermissionHintDialog = bundle.getBoolean(EXTRA_IS_SHOW_HINT_DIALOG);
            mPermissions = bundle.getStringArray(EXTRA_PERMISSIONS);
        }

        private void requestPermissionsIfNeed() {
            Activity activity = getActivity();
            if (mPermissions == null || mPermissions.length == 0 || activity == null) {
                callDenied(null);
                return;
            }

            requestPermissions(mPermissions, PERMISSION_REQUEST_CODE);
        }


        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (PERMISSION_REQUEST_CODE != requestCode) {
                return;
            }

            boolean isAllowApplyAgain = true;
            List<String> deniedList = new ArrayList<>(permissions.length);
            for (int i = 0, size = permissions.length; i < size; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedList.add(permissions[i]);
                    if (!shouldShowRequestPermissionRationale(permissions[i])) {
                        isAllowApplyAgain = false;
                    }
                }
            }

            if (deniedList.size() == 0) {
                callGranted();
            } else {
                if (isShowPermissionHintDialog) {
                    if (isAllowApplyAgain) {
                        showAgainRequestDialog();
                    } else {
                        showPermissionsSettingDialog();
                    }
                } else {
                    callDenied(deniedList.toArray(new String[0]));
                }
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            Activity activity = getActivity();
            if (requestCode == PERMISSION_SETTING_CODE) {
                if (activity != null && mPermissions != null && mPermissions.length > 0) {
                    if (checkPermission(activity, mPermissions)) {
                        callGranted();
                    } else {
                        callDenied(getDeniedPermission(activity, mPermissions));
                    }
                }
            }
        }

        public void setPermissionsResult(OnPermissionsResult permissionsResult) {
            mPermissionsResult = permissionsResult;
        }

        private void dismiss() {
            Fragment parentFragment = getParentFragment();
            if (parentFragment != null) {
                parentFragment.getChildFragmentManager()
                        .beginTransaction()
                        .remove(this)
                        .commitAllowingStateLoss();
                return;
            }

            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .remove(this)
                        .commitAllowingStateLoss();
            }
        }

        private void showAgainRequestDialog() {
            final Activity activity = getActivity();
            if (activity == null) {
                return;
            }

            if (mAgainRequestDialogInf == null) {
                mAgainRequestDialogInf = new PermissionHintDialogInfo();
                mAgainRequestDialogInf.title = "无法使用此功能";
                mAgainRequestDialogInf.message = "当前的应用程序缺少必要的权限。\n如果需要使用该功能，可尝试点击“再次申请”按钮。";
                mAgainRequestDialogInf.negativeBtnText = "取消";
                mAgainRequestDialogInf.positiveBtnText = "再次申请";
            }

            new MaterialAlertDialogBuilder(activity)
                    .setTitle(mAgainRequestDialogInf.title)
                    .setMessage(mAgainRequestDialogInf.message)
                    .setNegativeButton(mAgainRequestDialogInf.negativeBtnText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PermissionFragment.this.callDenied(getDeniedPermission(activity, mPermissions));
                        }
                    })
                    .setPositiveButton(mAgainRequestDialogInf.positiveBtnText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PermissionFragment.this.requestPermissionsIfNeed();
                        }
                    })
                    .show();
        }

        private void showPermissionsSettingDialog() {
            final Activity activity = getActivity();
            if (activity == null) {
                return;
            }
            if (mSettingDialogInfo == null) {
                mSettingDialogInfo = new PermissionHintDialogInfo();
                mSettingDialogInfo.title = "无法使用此功能";
                mSettingDialogInfo.message = "当前的应用程序缺少必要的权限。\n请点击“设置”-“权限”-打开所需的权限。";
                mSettingDialogInfo.negativeBtnText = "取消";
                mSettingDialogInfo.positiveBtnText = "设置";
            }
            new MaterialAlertDialogBuilder(activity)
                    .setTitle(mSettingDialogInfo.title)
                    .setMessage(mSettingDialogInfo.message)
                    .setNegativeButton(mSettingDialogInfo.negativeBtnText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PermissionFragment.this.callDenied(getDeniedPermission(activity, mPermissions));
                        }
                    })
                    .setPositiveButton(mSettingDialogInfo.positiveBtnText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PermissionFragment.this.startAppSettings(activity);
                        }
                    })
                    .show();
        }

        private void startAppSettings(Context context) {
            try {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.parse("package:" + context.getPackageName()));
                startActivityForResult(intent, PERMISSION_SETTING_CODE);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                    startActivityForResult(intent, PERMISSION_SETTING_CODE);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

        private void callGranted() {
            dismiss();
            if (mPermissionsResult != null) {
                mPermissionsResult.onGranted();
                mPermissionsResult = null;
            }
        }

        private void callDenied(String[] deniedPermissions) {
            dismiss();
            if (mPermissionsResult != null) {
                mPermissionsResult.onDenied(deniedPermissions);
                mPermissionsResult = null;
            }
        }
    }

}
