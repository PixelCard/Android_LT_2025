package com.pixelcard.project_truyen_as.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.pixelcard.project_truyen_as.model.Chapter;
import com.pixelcard.project_truyen_as.model.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chapter_RecycleAdapter_Admin extends RecyclerView.Adapter<Chapter_RecycleAdapter_Admin.ChapterViewHolder> {
    private Context context;
    private List<Product> productList;

    private List<Chapter> chapterList;

    public Chapter_RecycleAdapter_Admin(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public static class ChapterViewHolder  extends RecyclerView.ViewHolder {
        ImageView imgProduct;

        LinearLayout itemContainer;

        TextView tvTenTruyen, tvId, tvAuthor, tvDescription, tvCreateDate;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProductForChapter);
            tvTenTruyen = itemView.findViewById(R.id.tvTenTruyenForChapter);
            tvId = itemView.findViewById(R.id.tvIdForChapter);
            tvAuthor = itemView.findViewById(R.id.tvAuthorForChapter);
            itemContainer=itemView.findViewById(R.id.itemContainerChapter_Admin);
            tvDescription = itemView.findViewById(R.id.tvDescriptionForChapter);
            tvCreateDate = itemView.findViewById(R.id.tvCreateDateForChapter);
        }
    }

    @NonNull
    @Override
    public Chapter_RecycleAdapter_Admin.ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_chapter_admin_create, parent, false);
        return new Chapter_RecycleAdapter_Admin.ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvTenTruyen.setText(product.getTentruyen());
        holder.tvId.setText("ID: " + product.getId());
        holder.tvAuthor.setText("Tác giả: " + product.getAuthor());
        holder.tvDescription.setText("Mô tả: " + product.getDescription());
        holder.tvCreateDate.setText("Ngày tạo: " + product.getCreateDate());
        Glide.with(context).load(product.getUrlhinhsp()).into(holder.imgProduct);
        holder.itemContainer.setOnClickListener(v -> {
            // Gọi Dialog
            showAddChapterDialog(product);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    //Chapter
    private void showAddChapterDialog(Product product) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_add_chapter, null);

        // Ánh xạ
        EditText edtName = dialogView.findViewById(R.id.edtChapterName);
        EditText edtContent = dialogView.findViewById(R.id.edtChapterContent);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thêm chương mới");
        builder.setView(dialogView);

        builder.setPositiveButton("Thêm", null); // set null để kiểm soát tự xử lý
        builder.setNegativeButton("Hủy", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Gán thủ công nút Thêm để tránh tự đóng dialog khi lỗi
        Button btnAdd = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnAdd.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String content = edtContent.getText().toString().trim();

            if (name.isEmpty()) {
                edtName.setError("Không được để trống tên chương");
                return;
            }

            if (content.isEmpty()) {
                edtContent.setError("Không được để trống nội dung");
                return;
            }

            // Gọi hàm thêm chương với đầy đủ product
            addChapterToFirebase(product, name, content);
            dialog.dismiss();
        });
    }

    private void addChapterToFirebase(Product product, String chapterName, String chapterContent) {
        String productID = product.getId();
        String chapterID = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().push().getKey();

        Chapter chapter = new Chapter(
                product.getTentruyen(),                     // tenTruyen
                Long.parseLong(chapterName),                // chapterNumber
                chapterContent,
                product.getUrlhinhsp(),                     // imageUrl
                chapterID,
                productID,
                chapterName
        );

        DatabaseReference ref = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Chapters")
                .child(productID)
                .child(chapterID);

        ref.setValue(chapter).addOnSuccessListener(unused -> {
            Toast.makeText(context, "Thêm chương thành công!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Lỗi thêm chương: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
