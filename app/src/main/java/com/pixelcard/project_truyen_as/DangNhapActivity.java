package com.pixelcard.project_truyen_as;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.FirebaseDatabase;

public class DangNhapActivity extends AppCompatActivity {
    TextView textRegister;
    Button btn_LoginEmail;
    EditText LoginEmail,LoginPassword;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bắt sự kiện khi nhấn vào "Đăng ký"
        textRegister = findViewById(R.id.text_register);
        LoginEmail = findViewById(R.id.LoginEmail);
        LoginPassword = findViewById(R.id.LoginPassword);
        btn_LoginEmail = findViewById(R.id.button_login_email);

        databaseHelper = new DatabaseHelper(this);



//Sự kiện khi  ấn nút Đăng kí
        textRegister.setOnClickListener(v -> {
            Intent intent = new Intent(DangNhapActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });


        btn_LoginEmail.setOnClickListener(v -> {
                String email = LoginEmail.getText().toString().trim();
                String password = LoginPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(DangNhapActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (databaseHelper.checkLogin(email, password)) {
                    Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công, vui lòng đợi", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DangNhapActivity.this, ProductActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(DangNhapActivity.this, "Không đúng thông tin, vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                }

        });

    }
}
