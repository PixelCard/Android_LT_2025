package com.pixelcard.project_truyen_as.Product_Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.Admin.Admin_Home_Activity;
import com.pixelcard.project_truyen_as.model.Product;
import com.pixelcard.project_truyen_as.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Create_Product_Admin extends AppCompatActivity {
    EditText edtProductID,edtProductName,edtProductDescription,edtProductAuthor,edtImgURL;

    Button btnInsert;

    TextInputLayout txtErrorProductID,txtErrorProductName,txtErrorProductDescription,txtErrorProductAuthor,txtErrorImgURL;

    DatabaseReference databaseReference;

    ImageButton imgbuttoniconhome;

    LinearLayout layoutCategoryCheckboxes;

    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()); //Lấy ra ngày hiện hành theo định dạng "yyyy-MM-dd"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_product_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControl();
        loadCategoryCheckboxes();
        handleEvent();
    }

    private void handleEvent() {
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ các EditText
                String ma = edtProductID.getText().toString().trim();
                String tensp = edtProductName.getText().toString().trim();
                String motasp = edtProductDescription.getText().toString().trim();
                String urlhinhsp = edtImgURL.getText().toString().trim();
                String tacgiasanpham = edtProductAuthor.getText().toString().trim();
                String viewpeak = "0";

                // Xóa thông báo cũ
                clearAllErrors();

                // Kiểm tra từng ô có bị trống không
                boolean isValid = true;

                if (ma.isEmpty()) {
                    txtErrorProductID.setError("Mã sản phẩm không được để trống");
                    isValid = false;
                }
                if (tensp.isEmpty()) {
                    txtErrorProductName.setError("Tên sản phẩm không được để trống");
                    isValid = false;
                }
                if (motasp.isEmpty()) {
                    txtErrorProductDescription.setError("Mô tả không được để trống");
                    isValid = false;
                }
                if (tacgiasanpham.isEmpty()) {
                    txtErrorProductAuthor.setError("Tác giả không được để trống");
                    isValid = false;
                }
                if (urlhinhsp.isEmpty()) {
                    txtErrorImgURL.setError("URL hình ảnh không được để trống");
                    isValid = false;
                }

                if (!isValid) {
                    Toast.makeText(Create_Product_Admin.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Nếu hợp lệ thì tiếp tục thêm vào Firebase
                databaseReference = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference("Product");

                String view = "0";

                //Lấy ra Checkbox đã chọn thêm vào list selectedCategory
                List<String> selectedCategory = new ArrayList<>();

                for (int i = 0; i < layoutCategoryCheckboxes.getChildCount(); i++) {
                    View viewCheckbox = layoutCategoryCheckboxes.getChildAt(i);
                    if (viewCheckbox instanceof CheckBox) {
                        CheckBox cb = (CheckBox) viewCheckbox;
                        if (cb.isChecked()) {
                            selectedCategory.add((String) cb.getTag());
                        }
                    }
                }

                Product product = new Product(tensp, currentDate, ma, tacgiasanpham, motasp, urlhinhsp, viewpeak, selectedCategory);

                addProductToFirebase(product);
            }
        });


        imgbuttoniconhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Create_Product_Admin.this, Admin_Home_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void clearAllErrors() {
        txtErrorProductID.setError(null);
        txtErrorProductName.setError(null);
        txtErrorProductDescription.setError(null);
        txtErrorProductAuthor.setError(null);
        txtErrorImgURL.setError(null);
    }

    private void addControl() {
        edtImgURL=findViewById(R.id.edtImgURL);
        edtProductAuthor=findViewById(R.id.edtTextTacGia);
        edtProductID=findViewById(R.id.edtProductID);
        edtProductName=findViewById(R.id.edtTextTenProduct);
        edtProductDescription=findViewById(R.id.edtDescriptionProduct);
        btnInsert=findViewById(R.id.btnInsertProduct_Admin);
        txtErrorProductID=findViewById(R.id.txtErrorProductID);
        txtErrorProductAuthor=findViewById(R.id.txtErrorAuthor);
        txtErrorImgURL=findViewById(R.id.txtErrorURLImg);
        txtErrorProductName=findViewById(R.id.txtErrorTenProduct);
        txtErrorProductDescription=findViewById(R.id.txtErrorDescription);
        imgbuttoniconhome=findViewById(R.id.imgbuttonIconHome);
        layoutCategoryCheckboxes = findViewById(R.id.layoutCategoryCheckboxes);
    }


    private void addProductToFirebase(Product product) {
        String id = product.getId(); // ID bạn truyền vào, dùng làm key
        DatabaseReference productRef = databaseReference.child(id);

        productRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    // ID đã tồn tại
                    Toast.makeText(this, "ID đã tồn tại! Không thể thêm.", Toast.LENGTH_SHORT).show();
                    Cleartext();
                } else {
                    // ID chưa tồn tại → Thêm mới
                    productRef.setValue(product)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                                layoutCategoryCheckboxes.removeAllViews();
                                loadCategoryCheckboxes(); // Tải lại các checkbox rỗng
                                Cleartext();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Lỗi khi thêm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Cleartext();
                            });
                }
            } else {
                Toast.makeText(this, "Lỗi kiểm tra tồn tại: " + task.getException(), Toast.LENGTH_SHORT).show();
                Cleartext();
            }
        });
    }


    private void loadCategoryCheckboxes() {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("categories");

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    TextView txtNoCategory = new TextView(Create_Product_Admin.this);
                    txtNoCategory.setText("Chưa có thể loại nào. Vui lòng tạo trước.");
                    txtNoCategory.setPadding(16, 16, 16, 16);
                    layoutCategoryCheckboxes.addView(txtNoCategory);
                    return;
                }

                for (DataSnapshot child : snapshot.getChildren()) {
                    String categoryId = child.child("id").getValue(String.class);
                    String categoryName = child.child("name").getValue(String.class);

                    if (categoryId != null && categoryName != null) {
                        CheckBox checkBox = new CheckBox(Create_Product_Admin.this);
                        checkBox.setText(categoryName);
                        checkBox.setTag(categoryId); // Dùng tag để lấy ID khi lưu
                        layoutCategoryCheckboxes.addView(checkBox);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Create_Product_Admin.this, "Lỗi tải category: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void Cleartext(){
        edtProductID.setText("");
        edtProductDescription.setText("");
        edtProductName.setText("");
        edtImgURL.setText("");
        edtProductAuthor.setText("");
    }
}