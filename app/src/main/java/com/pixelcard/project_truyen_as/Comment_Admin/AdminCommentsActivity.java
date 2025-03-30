package com.pixelcard.project_truyen_as.Comment_Admin;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.R;

import java.util.ArrayList;
import java.util.List;

public class AdminCommentsActivity extends AppCompatActivity {
    private RecyclerView recyclerAdminComments;
    private AdminCommentAdapter adapter;
    private List<Comment> commentList;
    private DatabaseReference commentRef;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_comment);

        recyclerAdminComments = findViewById(R.id.recyclerAdminComments);
        recyclerAdminComments.setLayoutManager(new LinearLayoutManager(this));
        commentList = new ArrayList<>();

        productId = getIntent().getStringExtra("productId");
        commentRef = FirebaseDatabase.getInstance().getReference("comments").child(productId);

        adapter = new AdminCommentAdapter(this, commentList, productId);
        recyclerAdminComments.setAdapter(adapter);

        loadComments();
    }

    private void loadComments() {
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Comment comment = data.getValue(Comment.class);
                    commentList.add(comment);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AdminComments", "Lỗi khi tải bình luận", error.toException());
            }
        });
    }
}
