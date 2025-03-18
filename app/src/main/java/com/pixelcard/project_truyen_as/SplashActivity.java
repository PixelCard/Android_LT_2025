package com.pixelcard.project_truyen_as;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import android.os.CountDownTimer;

public class SplashActivity extends AppCompatActivity {
    private TextView txtViewDemNguoc;
    private int countdownTime = 5; // Số giây đếm ngược
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        txtViewDemNguoc=findViewById(R.id.txtDemNguocTimeResponse);

        // Bắt đầu đếm ngược
        new CountDownTimer(countdownTime * 1000, 1000) { // Đếm từ 5s, giảm mỗi 1s
            public void onTick(long millisUntilFinished) {
                txtViewDemNguoc.setText(millisUntilFinished / 1000 + "s"); // Cập nhật UI
            }

            public void onFinish() {
                txtViewDemNguoc.setText("0s"); // Hiển thị 0 khi hết thời gian

                // Chuyển sang màn hình đăng nhập
                Intent intent = new Intent(SplashActivity.this, DangNhapActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
