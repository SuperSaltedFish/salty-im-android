package me.zhixingye.salty.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * Created by zhixingye on 2020年02月21日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class PermissionHelper {

    public static void requestPermissions(String[] permissions, OnPermissionsResult resultCallback) {

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

    public interface OnPermissionsResult {
        void onGranted();

        void onDenied(String[] deniedPermissions);
    }


    private static class PermissionFragment extends Fragment {

        private static final String TAG = "PermissionFragment";
        private static final int PERMISSION_REQUEST_CODE = PermissionFragment.class.hashCode();

        private static PermissionFragment newInstance(String[] permissions) {
            Bundle args = new Bundle();
            args.putStringArray(TAG, permissions);
            PermissionFragment fragment = new PermissionFragment();
            fragment.setArguments(args);
            return fragment;
        }

        private OnPermissionsResult mPermissionsResult;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestPermissionsIfNeed();
        }

        private void requestPermissionsIfNeed() {
            Bundle bundle = getArguments();
            Activity activity = getActivity();
            if (bundle == null || activity == null) {
                if (mPermissionsResult != null) {
                    mPermissionsResult.onDenied(null);
                }
                dismiss();
                return;
            }

            String[] permissions = bundle.getStringArray(TAG);
            if (permissions == null || permissions.length == 0) {
                if (mPermissionsResult != null) {
                    mPermissionsResult.onDenied(null);
                }
                return;
            }

            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (PERMISSION_REQUEST_CODE != requestCode || mPermissionsResult == null) {
                return;
            }

            List<String> deniedList = new ArrayList<>(permissions.length);
            for (int i = 0, size = permissions.length; i < size; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedList.add(permissions[i]);
                }
            }

            dismiss();

            if (deniedList.size() == 0) {
                mPermissionsResult.onGranted();
            } else {
                mPermissionsResult.onDenied((String[]) deniedList.toArray());
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
                        .commit();
                return;
            }

            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .remove(this)
                        .commit();
            }
        }
    }


}
