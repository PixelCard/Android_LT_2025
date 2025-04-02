package com.pixelcard.project_truyen_as.Category_Admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.model.Category;

import java.util.UUID;

public class Create_Category_AdminActivity extends AppCompatActivity {
    private EditText edtCategoryName;
    private Button btnAddCategory;
    private DatabaseReference categoryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_category_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControl();
        HandleEvent();
    }

    private void HandleEvent() {
        categoryRef = FirebaseDatabase.getInstance().getReference("categories");

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = edtCategoryName.getText().toString().trim();

                if (TextUtils.isEmpty(categoryName)) {
                    Toast.makeText(Create_Category_AdminActivity.this, "Vui lòng nhập tên thể loại", Toast.LENGTH_SHORT).show();
                    return;
                }

                categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean isDuplicate = false;

                        for (DataSnapshot child : snapshot.getChildren()) {
                            String existingName = child.child("name").getValue(String.class);
                            if (existingName != null && existingName.equalsIgnoreCase(categoryName)) {
                                isDuplicate = true;
                                break;
                            }
                        }

                        if (isDuplicate) {
                            Toast.makeText(Create_Category_AdminActivity.this, "Tên thể loại đã tồn tại!", Toast.LENGTH_SHORT).show();
                        } else {
                            String id = categoryRef.push().getKey();
                            Category category = new Category(id, categoryName);

                            categoryRef.child(id).setValue(category)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(Create_Category_AdminActivity.this, "Thêm thể loại thành công", Toast.LENGTH_SHORT).show();
                                        edtCategoryName.setText("");
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(Create_Category_AdminActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Create_Category_AdminActivity.this, "Lỗi Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void addControl() {
        edtCategoryName = findViewById(R.id.editTextCategoryName);
        btnAddCategory = findViewById(R.id.buttonAddCategory);
    }
}


