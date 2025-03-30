package com.pixelcard.project_truyen_as;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
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
import com.pixelcard.project_truyen_as.model.Chapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChapterActivity extends AppCompatActivity {
    private TextView txtChapterTitle, txtChapterContent;
    private Button btnPrev, btnNext;

    private String productID;
    private List<Chapter> chapterList = new ArrayList<>();
    private int currentIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        addControl();
        
        handleEvent();
    }

    private void handleEvent() {
        // Nhận productID truyền từ màn trước
        productID = getIntent().getStringExtra("productID");

        loadAllChapters();

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                displayChapter();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentIndex < chapterList.size() - 1) {
                currentIndex++;
                displayChapter();
            }
        });
    }

    private void loadAllChapters() {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Chapters").child(productID);

        //Sắp xếp lại các node theo chapter từ bé -> lớn
        ref.orderByChild("chapterName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chapterList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Chapter chapter = snap.getValue(Chapter.class);
                    if (chapter != null) {
                        chapterList.add(chapter);
                    }
                }

                // Sort nếu cần
                Collections.sort(chapterList, (a, b) ->
                        Integer.compare(Integer.parseInt(a.getChapterName()), Integer.parseInt(b.getChapterName()))
                );

                // Kiểm tra nếu có chương cụ thể cần mở
                String chapterNameToOpen = getIntent().getStringExtra("chapterNameToOpen");
                if (chapterNameToOpen != null) {
                    for (int i = 0; i < chapterList.size(); i++) {
                        if (chapterList.get(i).getChapterName().equals(chapterNameToOpen)) {
                            currentIndex = i;
                            break;
                        }
                    }
                }

                displayChapter(); // Hiển thị chương đầu tiên
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChapterActivity.this, "Lỗi khi tải chương", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayChapter() {
        if (chapterList.size() == 0) return;

        Chapter current = chapterList.get(currentIndex);
        txtChapterTitle.setText("Chương " + current.getChapterName());
        txtChapterContent.setText(current.getChapterContent());
        Log.d("ChapterDebug", "Content = " + current.getChapterContent());
        // Disable nút nếu đang ở đầu/cuối
        btnPrev.setEnabled(currentIndex > 0);
        btnNext.setEnabled(currentIndex < chapterList.size() - 1);
    }

    private void addControl() {
        txtChapterTitle = findViewById(R.id.txtChapterTitle_Customer);
        txtChapterContent = findViewById(R.id.txtChapterContent_Customer);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
    }
}