package com.pixelcard.project_truyen_as;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AuthHelper {
    private FirebaseAuth auth;

    public AuthHelper() {
        auth = FirebaseAuth.getInstance();
    }

    // Đăng ký người dùng mới
    public void registerUser(String email, String password, final AuthCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        Log.d("AuthHelper", "Đăng ký thành công: " + user.getEmail());
                        callback.onSuccess(user);
                    } else {
                        Log.e("AuthHelper", "Đăng ký thất bại", task.getException());
                        callback.onFailure(task.getException());
                    }
                });
    }

    // Đăng nhập người dùng
    public void loginUser(String email, String password, final AuthCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        Log.d("AuthHelper", "Đăng nhập thành công: " + user.getEmail());
                        callback.onSuccess(user);
                    } else {
                        Log.e("AuthHelper", "Đăng nhập thất bại", task.getException());
                        callback.onFailure(task.getException());
                    }
                });
    }

    // Kiểm tra người dùng hiện tại
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    // Đăng xuất người dùng
    public void logoutUser() {
        auth.signOut();
        Log.d("AuthHelper", "Đã đăng xuất thành công");
    }

    // Interface để xử lý kết quả đăng nhập/đăng ký
    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception e);
    }
}
