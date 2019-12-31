package me.zhixingye.salty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = findViewById(R.id.mBtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onclick(null);
    }

    public void onclick(View v) {
//        new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SaltyIM/").mkdirs();
        String file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SaltyIM/aaaaa.apk";
//        File file1 = new File(file);
//        try {
//            if (!file1.exists() && !file1.createNewFile()) {
//                return;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
        startActivity(getPendingIntent(file));

    }

    private Intent getPendingIntent(String filePath) {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri = null;
        int targetSDK = getApplicationInfo().targetSdkVersion;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && targetSDK >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, getPackageName(), new File(filePath));
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            installIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        if (uri == null) {
            uri = Uri.fromFile(new File(filePath));
        }
        installIntent.setPackage(getPackageName());
        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return installIntent;
    }
}
