package com.pixelcard.project_truyen_as;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {
    EditText edtHoTen, edtEmailDangKy, edtMatKhau, edtXacNhanMatKhau;
    CheckBox chkDongYDieuKhoan;
    Button btnDangKy;
    TextView txtChuyenSangDangNhap;
    DatabaseHelper csoDuLieu;
    ImageView iconAnHienMatKhau, iconAnHienMatKhau2;
    ImageView icon_back_to_login;
    boolean isPasswordVisible = false;
    private View iconAnHienXacNhanMK2;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ các thành phần giao diện
        initViews();
        dangKyNguoiDung();

        HienMatkhau();
        HienMatKhau2();

        ChuyenSangDangNhap();





        // Xử lý sự kiện nút đăng ký
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKyNguoiDung();
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



    }

    private void initViews() {
        // Khởi tạo database helper
        csoDuLieu = new DatabaseHelper(this);
        edtHoTen = findViewById(R.id.register_fullname);

        edtEmailDangKy = findViewById(R.id.register_email);


        chkDongYDieuKhoan = findViewById(R.id.register_checkbox);


        txtChuyenSangDangNhap = findViewById(R.id.register_login);

        edtMatKhau = findViewById(R.id.register_password);
        iconAnHienMatKhau = findViewById(R.id.password_toggle);

        edtXacNhanMatKhau = findViewById(R.id.register_confirm_password);
        iconAnHienMatKhau2 = findViewById(R.id.password_toggle2);


        btnDangKy = findViewById(R.id.register_button);

        icon_back_to_login = findViewById(R.id.back_to_login_form);

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

        // Đăng ký tài khoản
        mAuth.createUserWithEmailAndPassword(email, matKhau)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Mặc định user mới có role "user"
                            saveUserToFirestore(user.getUid(), email, hoTen, "user");
                        }
                    } else {
                        String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(this, "Đăng ký thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 🔹 Hàm lưu thông tin user vào Firestore
    private void saveUserToFirestore(String userId, String email, String hoTen, String role) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("hoTen", hoTen);
        userMap.put("role", role); // Luôn là "user" khi đăng ký

        db.collection("users").document(userId)
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, DangNhapActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Lỗi khi lưu thông tin: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    public void HienMatkhau()
    {
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

    }
    public void HienMatKhau2()
    {
        iconAnHienMatKhau2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    edtXacNhanMatKhau.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iconAnHienMatKhau2.setImageResource(R.drawable.eye_circle);
                } else {
                    edtXacNhanMatKhau.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iconAnHienMatKhau2.setImageResource(R.drawable.eye_circle);
                }
                isPasswordVisible = !isPasswordVisible;
                edtXacNhanMatKhau.setSelection(edtXacNhanMatKhau.getText().length());
            }

        });
    }
    public void ChuyenSangDangNhap(){
        // Xử lý sự kiện chuyển sang màn hình đăng nhập
        txtChuyenSangDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, DangNhapActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
