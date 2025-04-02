package com.pixelcard.project_truyen_as.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.model.Category;

import java.util.List;

public class CategoryAdapter_Admin extends RecyclerView.Adapter<CategoryAdapter_Admin.ViewHolder>{
    private List<Category> categoryList;
    private Context context;

    public CategoryAdapter_Admin(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
        }
    }

    @NonNull
    @Override
    public CategoryAdapter_Admin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter_Admin.ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.txtCategoryName.setText(category.getName());
        holder.itemView.setOnClickListener(v -> {
            showEditDialog(categoryList.get(position));
        });
    }

    private void showEditDialog(Category category) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_category, null);
        EditText edtEditCategoryName = dialogView.findViewById(R.id.edtEditCategoryName);
        Button btnUpdate = dialogView.findViewById(R.id.btnUpdateCategory);
        Button btnDelete = dialogView.findViewById(R.id.btnDeleteCategory);

        edtEditCategoryName.setText(category.getName());
        edtEditCategoryName.setTextColor(Color.BLACK);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Sửa thể loại")
                .setView(dialogView)
                .create();

        btnUpdate.setOnClickListener(v -> {
            String newName = edtEditCategoryName.getText().toString().trim();

            if (newName.isEmpty()) {
                Toast.makeText(context, "Tên thể loại không được để trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("categories");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean isDuplicate = false;

                    for (DataSnapshot child : snapshot.getChildren()) {
                        String existingName = child.child("name").getValue(String.class);
                        String existingId = child.child("id").getValue(String.class);

                        if (existingName != null && existingId != null &&
                                !existingId.equals(category.getId()) && // tránh trùng chính mình
                                existingName.equalsIgnoreCase(newName)) {
                            isDuplicate = true;
                            break;
                        }
                    }

                    if (isDuplicate) {
                        Toast.makeText(context, "Tên thể loại đã tồn tại!", Toast.LENGTH_SHORT).show();
                    } else {
                        category.setName(newName);
                        ref.child(category.getId()).setValue(category)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(context, "Đã cập nhật!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Lỗi Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


        btnDelete.setOnClickListener(v -> {
            FirebaseDatabase.getInstance()
                    .getReference("categories")
                    .child(category.getId())
                    .removeValue()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(context, "Đã xoá!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
        });

        dialog.show();
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
