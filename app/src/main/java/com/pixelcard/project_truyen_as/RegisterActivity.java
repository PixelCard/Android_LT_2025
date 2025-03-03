package com.pixelcard.project_truyen_as;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText edtHoTen, edtEmailDangKy, edtMatKhau, edtXacNhanMatKhau;
    CheckBox chkDongYDieuKhoan;
    Button btnDangKy;
    TextView txtChuyenSangDangNhap;
    DatabaseHelper csoDuLieu;
    ImageView iconAnHienMatKhau, iconAnHienXacNhanMK;
    ImageView icon_back_to_login;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ các thành phần giao diện
        edtHoTen = findViewById(R.id.register_fullname);
        edtEmailDangKy = findViewById(R.id.register_email);
        edtMatKhau = findViewById(R.id.register_password);
        edtXacNhanMatKhau = findViewById(R.id.register_confirm_password);
        chkDongYDieuKhoan = findViewById(R.id.register_checkbox);
        btnDangKy = findViewById(R.id.register_button);
        txtChuyenSangDangNhap = findViewById(R.id.register_login);

        iconAnHienMatKhau = findViewById(R.id.password_toggle);
        iconAnHienXacNhanMK = findViewById(R.id.password_toggle2);

        icon_back_to_login = findViewById(R.id.back_to_login_form);

        iconAnHienMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    edtMatKhau.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iconAnHienMatKhau.setImageResource(R.drawable.eye_circle);
                } else {
                    edtMatKhau.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iconAnHienMatKhau.setImageResource(R.drawable.eye_circle);
                }
                isPasswordVisible = !isPasswordVisible;
                edtMatKhau.setSelection(edtMatKhau.getText().length());
            }
        });

        // Khởi tạo database helper
        csoDuLieu = new DatabaseHelper(this);

        // Xử lý sự kiện nút đăng ký
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKyNguoiDung();
            }
        });

        // Xử lý sự kiện chuyển sang màn hình đăng nhập
        txtChuyenSangDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, DangNhapActivity.class);
                startActivity(intent);
                finish();
            }
        });
        icon_back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, DangNhapActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        //Khi nguời dùng ấn nút quay về trang dăng nhập

    }

    private void dangKyNguoiDung() {
        String hoTen = edtHoTen.getText().toString().trim();
        String email = edtEmailDangKy.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();
        String xacNhanMatKhau = edtXacNhanMatKhau.getText().toString().trim();

        if (hoTen.isEmpty() || email.isEmpty() || matKhau.isEmpty() || xacNhanMatKhau.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!matKhau.equals(xacNhanMatKhau)) {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!chkDongYDieuKhoan.isChecked()) {
            Toast.makeText(this, "Bạn phải đồng ý với điều khoản sử dụng!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (csoDuLieu.checkUserExists(email)) {
            Toast.makeText(this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = csoDuLieu.insertUser(hoTen, email, matKhau);
        if (isInserted) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, DangNhapActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
