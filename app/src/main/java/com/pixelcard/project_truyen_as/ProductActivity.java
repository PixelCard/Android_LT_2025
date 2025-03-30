package com.pixelcard.project_truyen_as;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.adapter.Chapter_Home_RecycleAdapter_Admin;
import com.pixelcard.project_truyen_as.adapter.Chapter_Hone_RecycleAdapter_customer;
import com.pixelcard.project_truyen_as.model.Chapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductActivity extends AppCompatActivity {
    ImageView imgProduct;
    TextView tvTitle ;
    TextView tvAuthor ;
    TextView tvDescription ;
    TextView tvViewCount ;


    private RecyclerView recyclerViewChapters;
    private Chapter_Hone_RecycleAdapter_customer chapterHoneRecycleAdapterCustomer;
    private List<Chapter> chapterList = new ArrayList<>();

    private String productID;

    Button btnToggleDescription;

    Button btnReadBookFromBegin,btnReadBookFromLasted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addcontrol();
        handleEvent();
    }

    private void handleEvent() {

        //Liên quan đến layout hiển thị các nội dung cuủa product
        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String description = intent.getStringExtra("description");
        String view = intent.getStringExtra("view");

        tvTitle.setText("Tên truyện: " + title);
        tvAuthor.setText("Tác giả: " + author);
        tvDescription.setText("Description: " + description);
        tvViewCount.setText("View Peak: " + view + " lượt xem");

        // Load ảnh bằng Glide
        Glide.with(this).load(image).into(imgProduct);

        if (description.length() > 10) {
            tvDescription.setText("Description: " + description.substring(0, 10) + "...");
            btnToggleDescription.setVisibility(View.VISIBLE);
            btnToggleDescription.setOnClickListener(v -> {
                tvDescription.setText("Description: " + description);
                btnToggleDescription.setVisibility(View.GONE);
            });
        } else {
            tvDescription.setText("Description: " + description);
            btnToggleDescription.setVisibility(View.GONE);
        }



        //Liên quan đến View
        SharedPreferences sharedPreferences = getSharedPreferences("view_tracking", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        productID = getIntent().getStringExtra("id");

        // Lấy ngày hiện tại dưới dạng yyyy-MM-dd
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Tạo key duy nhất cho mỗi truyện mỗi ngày
        String key = productID + "_" + today;

        boolean hasViewed = sharedPreferences.getBoolean(key, false);

        if (!hasViewed) {
            // Chưa xem → tăng view lên Firebase
            DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product").child(productID);

            productRef.child("view").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String viewStr = snapshot.getValue(String.class);
                    int currentView = 0;

                    try {
                        currentView = Integer.parseInt(viewStr);
                    } catch (NumberFormatException e) { }

                    int newView = currentView + 1;
                    productRef.child("view").setValue(String.valueOf(newView));

                    // ⬇️ Cập nhật lại giao diện
                    tvViewCount.setText("View Peak: " + newView + " lượt xem");

                    // Lưu lại đã xem hôm nay
                    editor.putBoolean(key, true);
                    editor.apply();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("UPDATE_VIEW", "Lỗi cập nhật view: " + error.getMessage());
                }
            });
        }



        //Liên quan đến hiển thị số chapter tương ứng với product đó
        loadChaptersFromFirebase();

        //Viết sự kiện cho nút đọc từ đầu
        btnReadBookFromBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFirstChapterAndOpen();
            }
        });


        //Viết sự kiện cho nút đọc chapter truyện mới nhất
        btnReadBookFromLasted.setOnClickListener(v -> loadLatestChapterAndOpen());
    }

    private void loadLatestChapterAndOpen() {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Chapters")
                .child(productID);

        ref.orderByChild("chapterName").limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Chapter latestChapter = snap.getValue(Chapter.class);
                            if (latestChapter != null) {
                                Intent intent = new Intent(ProductActivity.this, ChapterActivity.class);
                                intent.putExtra("productID", productID);
                                intent.putExtra("chapterNameToOpen", latestChapter.getChapterName()); // 👈 truyền kèm tên chương
                                startActivity(intent);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProductActivity.this, "Lỗi khi đọc chương mới nhất", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadFirstChapterAndOpen() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chapters").child(productID);

        ref.orderByChild("chapterName").equalTo("1") // Tìm chương đầu tiên
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot chapterSnap : snapshot.getChildren()) {
                            Chapter chapter = chapterSnap.getValue(Chapter.class);
                            if (chapter != null) {
                                Intent intent = new Intent(ProductActivity.this, ChapterActivity.class);
                                intent.putExtra("productID", productID);
                                startActivity(intent);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProductActivity.this, "Lỗi khi mở chương đầu", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadChaptersFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chapters").child(productID);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chapterList.clear();
                for (DataSnapshot chapterSnap : snapshot.getChildren()) {
                    Chapter chapter = chapterSnap.getValue(Chapter.class);
                    chapterList.add(chapter);
                }
                Collections.sort(chapterList, new Comparator<Chapter>() {
                    @Override
                    public int compare(Chapter o1, Chapter o2) {
                        return Integer.compare(Integer.parseInt(o1.getChapterName()), Integer.parseInt(o2.getChapterName()));
                    }
                });
                chapterHoneRecycleAdapterCustomer.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductActivity.this, "Lỗi khi tải chương", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addcontrol() {
        imgProduct = findViewById(R.id.imageProduct);
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvDescription = findViewById(R.id.tvDescription);
        btnReadBookFromBegin=findViewById(R.id.btnReadFirst);
        tvViewCount = findViewById(R.id.tvViewCount);
        btnReadBookFromLasted=findViewById(R.id.btnReadLatest);
        btnToggleDescription=findViewById(R.id.btnToggleDescription);
        recyclerViewChapters = findViewById(R.id.recyclerViewChapters_customer);
        recyclerViewChapters.setLayoutManager(new LinearLayoutManager(this));
        chapterHoneRecycleAdapterCustomer = new Chapter_Hone_RecycleAdapter_customer(chapterList);
        recyclerViewChapters.setAdapter(chapterHoneRecycleAdapterCustomer);
    }
}