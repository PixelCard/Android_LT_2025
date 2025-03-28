package com.pixelcard.project_truyen_as.Account_Admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.adapter.UserAdapter;
import com.pixelcard.project_truyen_as.model.User;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private List<User> userList;
    private DatabaseReference userRef;
    private Button btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.pixelcard.project_truyen_as.R.layout.activity_admin);

        recyclerViewUsers = findViewById(com.pixelcard.project_truyen_as.R.id.recyclerViewUsers);
        btnAddUser = findViewById(R.id.btnAddUser);

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList);
        recyclerViewUsers.setAdapter(userAdapter);

        userRef = FirebaseDatabase.getInstance().getReference("users");

        loadUsers();

//        btnAddUser.setOnClickListener(v -> {
//            startActivity(new Intent(AdminActivity.this, AddUserActivity.class));
//        });
    }

    private void loadUsers() {
        userRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}