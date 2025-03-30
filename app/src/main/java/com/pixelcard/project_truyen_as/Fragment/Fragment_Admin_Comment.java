package com.pixelcard.project_truyen_as.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.Comment_Admin.AdminCommentAdapter;
import com.pixelcard.project_truyen_as.Comment_Admin.Comment;
import com.pixelcard.project_truyen_as.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Admin_Comment extends Fragment {
    private RecyclerView recyclerAdminComments;
    private AdminCommentAdapter adapter;
    private List<Comment> commentList;
    private DatabaseReference commentRef;
    private String productId,userID;

    public Fragment_Admin_Comment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__admin__comment, container, false);
        recyclerAdminComments = view.findViewById(R.id.recyclerAdminComments);
        recyclerAdminComments.setLayoutManager(new LinearLayoutManager(getContext()));
        commentList = new ArrayList<>();


        // Trong Fragment (onCreateView hoặc onCreate)
        if (getArguments() != null) {
            productId = getArguments().getString("productId");
            userID=getArguments().getString("userID");
        }


        commentRef = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("comments").child(productId);

        adapter = new AdminCommentAdapter(getContext(), commentList, productId,userID);
        recyclerAdminComments.setAdapter(adapter);

        loadComments();
        return view;
    }

    private void loadComments() {
        commentRef = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("comments");

        commentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String userId = userSnapshot.getKey();

                    if (userSnapshot.hasChild(productId)) {
                        DataSnapshot productSnapshot = userSnapshot.child(productId);

                        for (DataSnapshot commentSnapshot : productSnapshot.getChildren()) {
                            Comment comment = commentSnapshot.getValue(Comment.class);

                            if (comment != null) {
                                comment.setCommentId(commentSnapshot.getKey());
                                comment.setUserId(userId);
                                comment.setProductID(productId);
                                commentList.add(comment);
                            }
                        }
                    }
                }

                if (adapter == null) {
                    adapter = new AdminCommentAdapter(getContext(), commentList, productId,userID);
                    recyclerAdminComments.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AdminComments", "Lỗi khi tải bình luận", error.toException());
            }
        });
    }
}