package com.pixelcard.project_truyen_as;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pixelcard.project_truyen_as.Account_Admin.AdminActivity;
import com.pixelcard.project_truyen_as.Admin.Admin_Home_Activity;

import java.util.Objects;

public class DangNhapActivity extends AppCompatActivity {

    private static final String KEY_EMAIL = "email";
    private static final String KEY_IS_LOGGED_IN = "isLogin";
    private static final String PREFS_USER = "UserPrefs";

    private TextView textRegister;
    private Button btn_LoginEmail, btnLoginFacebook;
    private EditText LoginEmail, LoginPassword;
    private ImageView imgShowPassword;
    private boolean isPasswordVisible = false;
    private TextView txtForgotPassword;

    private FirebaseAuth mAuth;
    private SharedPreferences userPreferences;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);

        userPreferences = getSharedPreferences(PREFS_USER, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d("DEBUG", "Người dùng đã đăng nhập: " + user.getEmail());
        } else {
            Log.d("DEBUG", "Chưa có người dùng đăng nhập");
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.email_login_form), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo CallbackManager cho Facebook Login
        callbackManager = CallbackManager.Factory.create();

        initViews();
        initListeners();
        setupFacebookLogin();
    }

    private void initViews() {
        textRegister = findViewById(R.id.text_register);
        LoginEmail = findViewById(R.id.LoginEmail);
        LoginPassword = findViewById(R.id.LoginPassword);
        btn_LoginEmail = findViewById(R.id.button_login_email);
        btnLoginFacebook = findViewById(R.id.btnLoginFacebook);
        imgShowPassword = findViewById(R.id.password_toggle_login);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
    }

    private void initListeners() {
        // Listener cho đăng ký tài khoản
        textRegister.setOnClickListener(v -> {
            Intent intent = new Intent(DangNhapActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        // Listener cho đăng nhập qua email
        btn_LoginEmail.setOnClickListener(v -> handleLogin());

        // Listener cho hiển thị/ẩn mật khẩu
        imgShowPassword.setOnClickListener(v -> togglePasswordVisibility());

        // Listener cho quên mật khẩu
        txtForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(DangNhapActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        // Đăng xuất trước khi đăng nhập lại
        FirebaseAuth.getInstance().signOut();

        String email = LoginEmail.getText().toString().trim();
        String password = LoginPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(DangNhapActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("DEBUG", "Kiểm tra không bị bỏ trống");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    Log.d("DEBUG", "Bắt đầu mAuth.signInWithEmailAndPassword");
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Log.d("DEBUG", "User signed in: " + user.getEmail());
                            String userId = user.getUid();

                            // Truy xuất dữ liệu từ Firebase Realtime Database
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                            databaseReference.child(userId).get()
                                    .addOnSuccessListener(dataSnapshot -> {
                                        if (dataSnapshot.exists()) {
                                            // Nếu tài liệu người dùng tồn tại, lấy thông tin 'role'
                                            String role = dataSnapshot.child("role").getValue(String.class);
                                            Log.d("DEBUG", "Role người dùng: " + role);

                                            // Lưu trạng thái đăng nhập vào SharedPreferences
                                            SharedPreferences.Editor editor = userPreferences.edit();
                                            editor.putBoolean(KEY_IS_LOGGED_IN, true);
                                            editor.putString(KEY_EMAIL, user.getEmail());
                                            editor.apply();


                                            // Chuyển hướng đến Activity dựa trên role
                                            Intent intent = ("1".equals(role))
                                                    ? new Intent(DangNhapActivity.this, Admin_Home_Activity.class)
                                                    : new Intent(DangNhapActivity.this, MainActivity.class);

                                            startActivity(intent);
                                            finish();

                                        } else {
                                            // Nếu không tìm thấy tài liệu người dùng
                                            Log.d("DEBUG", "Không tìm thấy tài liệu người dùng trong Realtime Database");
                                            Toast.makeText(DangNhapActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.d("DEBUG", "Lỗi khi truy xuất dữ liệu từ Realtime Database");
                                        Toast.makeText(DangNhapActivity.this, "Lỗi truy xuất dữ liệu người dùng!", Toast.LENGTH_LONG).show();
                                    });
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Đăng nhập thất bại!";
                        Log.d("DEBUG", Objects.requireNonNull(errorMessage));
                        Toast.makeText(DangNhapActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });

    }


    private void setupFacebookLogin() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(DangNhapActivity.this, "Facebook login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull FacebookException error) {
                Log.e("FACEBOOK_LOGIN", "Facebook login error", error);
                new AlertDialog.Builder(DangNhapActivity.this)
                        .setTitle("Facebook Login Failed")
                        .setMessage("Lỗi: " + error.getMessage())
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(DangNhapActivity.this, "Đăng nhập Facebook thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DangNhapActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(DangNhapActivity.this, "Xác thực Facebook thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            LoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgShowPassword.setImageResource(R.drawable.icon_password_login);
        } else {
            LoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgShowPassword.setImageResource(R.drawable.icon_password_login);
        }
        isPasswordVisible = !isPasswordVisible;
        LoginPassword.setSelection(LoginPassword.getText().length());
    }

    private void kiemTraQuyenAdmin(String userId) {
        FirebaseFirestore.getInstance().collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    String role = documentSnapshot.getString("userrole");
                    Intent intent = role != null && "1".equals(role)
                            ? new Intent(DangNhapActivity.this, AdminActivity.class)
                            : new Intent(DangNhapActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
    }
}
