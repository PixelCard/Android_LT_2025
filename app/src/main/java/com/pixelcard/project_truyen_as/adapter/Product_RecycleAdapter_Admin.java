package com.pixelcard.project_truyen_as.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.model.Product;

import java.util.ArrayList;
import java.util.List;

public class Product_RecycleAdapter_Admin extends RecyclerView.Adapter<Product_RecycleAdapter_Admin.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public Product_RecycleAdapter_Admin(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;

        LinearLayout itemContainer;
        TextView tvTenTruyen, tvId, tvAuthor, tvDescription, tvCreateDate;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvTenTruyen = itemView.findViewById(R.id.tvTenTruyen);
            tvId = itemView.findViewById(R.id.tvId);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            itemContainer=itemView.findViewById(R.id.itemContainerProduct_Admin);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCreateDate = itemView.findViewById(R.id.tvCreateDate);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_product_admin, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvTenTruyen.setText(product.getTentruyen());
        holder.tvId.setText("ID: " + product.getId());
        holder.tvAuthor.setText("Tác giả: " + product.getAuthor());
        holder.tvDescription.setText("Mô tả: " + product.getDescription());
        holder.tvCreateDate.setText("Ngày tạo: " + product.getCreateDate());
        Glide.with(context).load(product.getUrlhinhsp()).into(holder.imgProduct);
        holder.itemContainer.setOnClickListener(v -> {
            // Gọi Dialog
            showActionDialog(product);
        });
    }

    private void showActionDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn hành động cho: " + product.getTentruyen());
        builder.setItems(new CharSequence[]{"Sửa", "Xóa"}, (dialog, which) -> {
            switch (which) {
                case 0: // Sửa
                    Toast.makeText(context, "Sửa: " + product.getId(), Toast.LENGTH_SHORT).show();

                    showEditDialog(product);
                    break;
                case 1: // Xóa
                    deleteProductFromFirebase(product.getId());
                    break;
            }
        });
        builder.show();
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }



    //Delete Item product from firebase
    private void deleteProductFromFirebase(String productId) {
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Product").child(productId);
        DatabaseReference refChapter = FirebaseDatabase
                .getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Chapters").child(productId);

        ref.removeValue().addOnSuccessListener(unused -> {
            Toast.makeText(context, "Đã xóa sản phẩm và Chapter sản phẩm!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Lỗi xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });

        refChapter.removeValue().addOnSuccessListener(unused -> {

        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Lỗi xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }




    //Update Product from firebase
    private void showEditDialog(Product product) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_product_admin, null);

        EditText edtTenTruyen = dialogView.findViewById(R.id.edtTenTruyen);
        EditText edtAuthor = dialogView.findViewById(R.id.edtAuthor);
        EditText edtDescription = dialogView.findViewById(R.id.edtDescription);
        EditText edtImageUrl = dialogView.findViewById(R.id.edtImageUrl);
        Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);
        LinearLayout layoutCategoryCheckboxes = dialogView.findViewById(R.id.layoutCategoryCheckboxes_Update);

        edtTenTruyen.setTextColor(Color.BLACK);
        edtAuthor.setTextColor(Color.BLACK);
        edtDescription.setTextColor(Color.BLACK);
        edtImageUrl.setTextColor(Color.BLACK);

        // Gán dữ liệu cũ
        edtTenTruyen.setText(product.getTentruyen());
        edtAuthor.setText(product.getAuthor());
        edtDescription.setText(product.getDescription());
        edtImageUrl.setText(product.getUrlhinhsp());
        loadCategoryCheckboxesWithPreselect(layoutCategoryCheckboxes, product.getCategoryIds());


        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Cập nhật sản phẩm")
                .setView(dialogView)
                .create();

        btnUpdate.setOnClickListener(v -> {
            String newTenTruyen = edtTenTruyen.getText().toString();
            String newAuthor = edtAuthor.getText().toString();
            String newDesc = edtDescription.getText().toString();
            String newImg = edtImageUrl.getText().toString();

            // Cập nhật Firebase
            DatabaseReference ref = FirebaseDatabase
                    .getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Product").child(product.getId());

            product.setTentruyen(newTenTruyen);
            product.setAuthor(newAuthor);
            product.setDescription(newDesc);
            product.setUrlhinhsp(newImg);

            List<String> selectedCategory = new ArrayList<>();

            for (int i = 0; i < layoutCategoryCheckboxes.getChildCount(); i++) {
                View view = layoutCategoryCheckboxes.getChildAt(i);
                if (view instanceof CheckBox) {
                    CheckBox cb = (CheckBox) view;
                    if (cb.isChecked()) {
                        selectedCategory.add((String) cb.getTag());
                    }
                }
            }

            product.setCategoryIds(selectedCategory);

            ref.setValue(product)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        dialog.show();
    }


    private void loadCategoryCheckboxesWithPreselect(LinearLayout layout, List<String> selectedIds) {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("categories");

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                layout.removeAllViews(); // Clear nếu có
                for (DataSnapshot child : snapshot.getChildren()) {
                    String categoryId = child.child("id").getValue(String.class);
                    String categoryName = child.child("name").getValue(String.class);

                    if (categoryId != null && categoryName != null) {
                        CheckBox cb = new CheckBox(context);
                        cb.setText(categoryName);
                        cb.setTag(categoryId);

                        if (selectedIds != null && selectedIds.contains(categoryId)) {
                            cb.setChecked(true);
                        }

                        layout.addView(cb);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Lỗi tải category: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
























