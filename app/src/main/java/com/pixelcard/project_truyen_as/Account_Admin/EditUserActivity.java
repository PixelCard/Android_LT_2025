package com.pixelcard.project_truyen_as.Account_Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pixelcard.project_truyen_as.R;

public class EditUserActivity extends AppCompatActivity {
    private EditText edtName, edtEmail;
    private Button btnSave;
    private DatabaseReference userRef;
    private String userId, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        btnSave = findViewById(R.id.btnSave);

        userRef = FirebaseDatabase.getInstance().getReference("users");

        userId = getIntent().getStringExtra("userId");
        Log.d("DEBUG", "userId nhận được: " + userId);

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Lỗi: userId không hợp lệ!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        role = getIntent().getStringExtra("role");

        edtName.setText(name);
        edtEmail.setText(email);

        btnSave.setOnClickListener(v -> {
            String newName = edtName.getText().toString().trim();
            String newEmail = edtEmail.getText().toString().trim();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(EditUserActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            userRef.child(userId).child("hoten").setValue(newName);
            userRef.child(userId).child("email").setValue(newEmail)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("DEBUG", "Cập nhật Firebase thành công");
                        Toast.makeText(EditUserActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Log.e("DEBUG", "Lỗi cập nhật Firebase", e));
        });
    }
}
