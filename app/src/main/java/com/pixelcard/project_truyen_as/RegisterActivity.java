package com.pixelcard.project_truyen_as;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.databinding.ActivityRegisterBinding;
import com.pixelcard.project_truyen_as.model.User;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private boolean isPasswordVisible = false;
    private FirebaseDatabase db;
    DatabaseReference reference;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();



        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        setEventHandlers();
    }


    private void setEventHandlers() {
        Log.d("DEBUG", "Hàm setEventHandlers() đã được gọi");
        binding.registerButton.setOnClickListener(v -> dangKyNguoiDung());
        togglePasswordVisibility(binding.passwordToggle, binding.registerPassword);

        binding.registerLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, DangNhapActivity.class));
        });

        binding.btnBackRegisHome.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        });


    }

    private void togglePasswordVisibility(ImageView icon, EditText editText) {
        icon.setOnClickListener(v -> {
            if (isPasswordVisible) {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            isPasswordVisible = !isPasswordVisible;
            editText.setSelection(editText.getText().length());
        });
    }

    public void dangKyNguoiDung() {
        Log.d("DEBUG", "Hàm dangKyNguoiDung() đã được gọi");

        // Lấy thông tin từ UI
        String hoten = binding.registerFullname.getText().toString().trim();
        String email = binding.registerEmail.getText().toString().trim();
        String matkhau = binding.registerPassword.getText().toString().trim();
        Integer role = 0; // Default role

        // Kiểm tra các trường trống
        if (hoten.isEmpty() || email.isEmpty() || matkhau.isEmpty()) {
            showAlertDialog("Cảnh báo", "Vui lòng nhập đầy đủ thông tin, các trường không được bỏ trống");
            return;
        }

        // Kiểm tra mật khẩu
        if (matkhau.length() < 6) {
            showAlertDialog("Cảnh báo", "Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }

        // Tiến hành đăng ký người dùng
        mAuth.createUserWithEmailAndPassword(email, matkhau)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();
                            User newUser = new User(hoten, email, role); // Create User object

                            // Consistent database reference
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users");

                            // Log the data being saved (for debugging)
                            Log.d("DEBUG", "Saving user data to database: " + newUser.toString());

                            myRef.child(uid).setValue(newUser)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this, DangNhapActivity.class));
                                        } else {
                                            String errorMessage = "Lỗi khi lưu thông tin người dùng. Vui lòng thử lại sau.";
                                            if (dbTask.getException() == null) {
                                                errorMessage = "Lỗi khi lưu thông tin người dùng: " + dbTask.getException().getMessage();
                                            }
                                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                            Log.d("FIREBASE_DB", "Error saving user " + email + ": " + errorMessage, dbTask.getException());
                                        }
                                    });
                        } else {
                            // This should ideally not happen, but handle it defensively
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công nhưng không thể lưu thông tin người dùng.", Toast.LENGTH_SHORT).show();
                            Log.w("FIREBASE_AUTH", "User authenticated but FirebaseUser object is null.");
                            // Consider navigating to the login screen or handling this case appropriately.
                            startActivity(new Intent(RegisterActivity.this, DangNhapActivity.class));
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " +
                                        (task.getException() != null ? task.getException().getMessage() : "Lỗi không xác định"),
                                Toast.LENGTH_SHORT).show();
                        Log.e("FIREBASE_AUTH", "Sign up failed", task.getException());
                    }
                });
    }




    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }


}

//    public void AddRealTime(){
//
//    }




