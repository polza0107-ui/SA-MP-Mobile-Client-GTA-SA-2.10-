package com.samp.mobile.game;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionActivity extends AppCompatActivity {

    private static final int PERMISSION_REQ_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkAllPermissions()) {
            startGame();
            return;
        }

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(60, 60, 60, 60);
        layout.setBackgroundColor(Color.parseColor("#121212"));

        TextView title = new TextView(this);
        title.setText("SA-MP Mobile 2.10");
        title.setTextSize(24);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 30);
        layout.addView(title);

        TextView message = new TextView(this);
        message.setText("เกมจำเป็นต้องใช้สิทธิ์ 'เข้าถึงไฟล์ทั้งหมด' (All Files Access)\nเพื่อโหลดข้อมูลแคชเกมจากโฟลเดอร์ /GTA\n\nกรุณากดปุ่มด้านล่างเพื่อเปิดการอนุญาตในหน้าตั้งค่า");
        message.setTextSize(16);
        message.setTextColor(Color.LTGRAY);
        message.setGravity(Gravity.CENTER);
        message.setPadding(0, 0, 0, 50);
        layout.addView(message);

        Button btnGrant = new Button(this);
        btnGrant.setText("อนุญาตสิทธิ์เข้าถึงไฟล์ (Grant Permission)");
        btnGrant.setTextSize(16);
        btnGrant.setBackgroundColor(Color.parseColor("#2196F3"));
        btnGrant.setTextColor(Color.WHITE);
        btnGrant.setPadding(40, 20, 40, 20);
        btnGrant.setOnClickListener(v -> requestRequiredPermissions());
        layout.addView(btnGrant);

        setContentView(layout);
    }

    private boolean checkAllPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivity(intent);
                } catch (Exception ignored) {
                    Toast.makeText(this, "ไม่สามารถเปิดหน้าตั้งค่าได้ กรุณาไปเปิดสิทธิ์ใน การตั้งค่า -> แอพ ด้วยตนเอง", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
            }, PERMISSION_REQ_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkAllPermissions()) {
            startGame();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkAllPermissions()) {
            startGame();
        } else {
            Toast.makeText(this, "กรุณาอนุญาตสิทธิ์เพื่อเข้าเล่นเกม", Toast.LENGTH_SHORT).show();
        }
    }

    private void startGame() {
        Intent intent = new Intent(this, SAMP.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
