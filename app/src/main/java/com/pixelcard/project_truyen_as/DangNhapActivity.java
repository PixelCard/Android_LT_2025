package com.pixelcard.project_truyen_as;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class DangNhapActivity extends AppCompatActivity {
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IS_LOGGED_IN = "isLogin";
    private static final String PREFS_USER = "UserPrefs";

    TextView textRegister;
    Button btn_LoginEmail, btnLoginFacebook;
    EditText LoginEmail, LoginPassword;
    private DatabaseHelper databaseHelper;
    private SharedPreferences userPreferences;
    private CallbackManager callbackManager;
    ImageView imgShowPassword;
    boolean isPasswordVisible = false;
    TextView txtForgotPassword;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);

        userPreferences = getSharedPreferences(PREFS_USER, MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.email_login_form), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo Database
        databaseHelper = new DatabaseHelper(this);




        // Khởi tạo CallbackManager
        callbackManager = CallbackManager.Factory.create();

        initViews();
        handleEvents_TextRegister();
        setupFacebookLogin();
        ShowPasslogin();
        checkEmailAndPassword();
        ForgotPass();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            checkUserRole(currentUser.getUid());
        }
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

    private void handleEvents_TextRegister() {
        textRegister.setOnClickListener(v -> {
            Log.d("DangNhapActivity", "Chuyển đến màn hình đăng ký thành coông");
            Intent intent = new Intent(DangNhapActivity.this, RegisterActivity.class);

            startActivity(intent);
            finish();
        });
    }

    private void checkEmailAndPassword() {
        btn_LoginEmail.setOnClickListener(v -> {
            String email = LoginEmail.getText().toString().trim();
            String password = LoginPassword.getText().toString().trim();

            Log.d("DangNhapActivity", "Email: " + email);
            Log.d("DangNhapActivity", "Mật khẩu: " + password);

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(DangNhapActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Lưu thông tin vào SharedPreferences
                                SharedPreferences.Editor editor = userPreferences.edit();
                                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                                editor.putString(KEY_EMAIL, user.getEmail());
                                editor.apply();

                                // Chuyển sang MainActivity
                                startActivity(new Intent(DangNhapActivity.this, MainActivity.class));
                                finish();
                            }
                        } else {
                            Toast.makeText(DangNhapActivity.this, "Đăng nhập thất bại! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        });

        btnLoginFacebook.setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(
                    DangNhapActivity.this,
                    Arrays.asList("email", "public_profile")
            );
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
                new AlertDialog.Builder(DangNhapActivity.this)
                        .setTitle("Facebook Login Failed")
                        .setMessage("Lỗi: " + error.getMessage() + "\n\n" + Log.getStackTraceString(error))
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();

                Log.e("FACEBOOK_LOGIN", "Facebook Login Error", error);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FACEBOOK_LOGIN", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        Toast.makeText(DangNhapActivity.this,
                                "Đăng nhập Facebook thành công!", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(DangNhapActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Log.w("FACEBOOK_LOGIN", "signInWithCredential:failure", task.getException());
                        Toast.makeText(DangNhapActivity.this,
                                "Xác thực Facebook thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void ShowPasslogin() {
        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

    public void ForgotPass() {
        txtForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(DangNhapActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

    }

    private void checkUserRole(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String role = document.getString("role");

                            if ("admin".equals(role)) {
                                startActivity(new Intent(DangNhapActivity.this, AdminActivity.class));
                                Toast.makeText(DangNhapActivity.this, "Chào mừng Admin!", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(DangNhapActivity.this, MainActivity.class));
                                Toast.makeText(DangNhapActivity.this, "Chào mừng User!", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        } else {
                            Toast.makeText(DangNhapActivity.this, "Không tìm thấy vai trò người dùng!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(DangNhapActivity.this, "Lỗi khi kiểm tra role!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
