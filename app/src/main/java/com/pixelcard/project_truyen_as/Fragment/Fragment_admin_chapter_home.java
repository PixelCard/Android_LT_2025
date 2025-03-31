package com.pixelcard.project_truyen_as.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.adapter.Chapter_Home_RecycleAdapter_Admin;
import com.pixelcard.project_truyen_as.adapter.Chapter_RecycleAdapter_Admin;
import com.pixelcard.project_truyen_as.adapter.Product_RecycleAdapter_Admin;
import com.pixelcard.project_truyen_as.model.Chapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_admin_chapter_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_admin_chapter_home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_admin_chapter_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_admin_chapter_home.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_admin_chapter_home newInstance(String param1, String param2) {
        Fragment_admin_chapter_home fragment = new Fragment_admin_chapter_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    RecyclerView recyclerViewChaper;

    List<Chapter> datalist;

    DatabaseReference databaseReference;

    Chapter_Home_RecycleAdapter_Admin chapter_home_recycleAdapter_admin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_chapter_home, container, false);
        recyclerViewChaper=view.findViewById(R.id.recyclerViewChapterAdmin);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1);
        recyclerViewChaper.setLayoutManager(gridLayoutManager);
        datalist=new ArrayList<>();
        chapter_home_recycleAdapter_admin=new Chapter_Home_RecycleAdapter_Admin(getContext(),datalist);
        recyclerViewChaper.setAdapter(chapter_home_recycleAdapter_admin);
        DatabaseReference chaptersRef = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Chapters");
        DatabaseReference productRef = FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Product");

        chaptersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear();
                for (DataSnapshot productChapterSnapshot : snapshot.getChildren()) {
                    String productID = productChapterSnapshot.getKey();
                    long chapterCount = productChapterSnapshot.getChildrenCount();
                    // Truy cập thông tin sản phẩm theo productID
                    productRef.child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                            String imageUrl = productSnapshot.child("urlhinhsp").getValue(String.class);
                            String tenTruyen = productSnapshot.child("tentruyen").getValue(String.class);
                            // Duyệt từng chương trong Chapters/{productID}
                            for (DataSnapshot chapterSnapshot : productChapterSnapshot.getChildren()) {
                                String chapterID = chapterSnapshot.child("chapterID").getValue(String.class);
                                String chapterContent = chapterSnapshot.child("chapterContent").getValue(String.class);
                                String chapterName = chapterSnapshot.child("chapterName").getValue(String.class);
                                Chapter chapter = new Chapter(tenTruyen, chapterCount, chapterContent, imageUrl,chapterID,productID,chapterName);
                                datalist.add(chapter);
                            }
                            chapter_home_recycleAdapter_admin.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Lỗi sản phẩm: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi chương: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}