package com.pixelcard.project_truyen_as.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelcard.project_truyen_as.model.Product;
import com.pixelcard.project_truyen_as.R;
import com.pixelcard.project_truyen_as.adapter.Product_RecycleAdapter_Admin;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Product_admin_Test_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Product_admin_Test_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    RecyclerView recyclerViewProduct;

    List<Product> datalist;

    DatabaseReference databaseReference;

    Product_RecycleAdapter_Admin productReycleAdapterAdmin;

    public Product_admin_Test_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Product_admin_Test_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Product_admin_Test_Fragment newInstance(String param1, String param2) {
        Product_admin_Test_Fragment fragment = new Product_admin_Test_Fragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_admin__test_, container, false);

        recyclerViewProduct=view.findViewById(R.id.recyclerViewProduct);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1);
        recyclerViewProduct.setLayoutManager(gridLayoutManager);
        datalist=new ArrayList<>();
        productReycleAdapterAdmin=new Product_RecycleAdapter_Admin(getContext(),datalist);
        recyclerViewProduct.setAdapter(productReycleAdapterAdmin);
        databaseReference= FirebaseDatabase.getInstance("https://freereadcomic-262e1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Product");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear();
                for(DataSnapshot itemSnapshot : snapshot.getChildren()){
                    Product product = itemSnapshot.getValue(Product.class);
                    datalist.add(product);
                }
                productReycleAdapterAdmin.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}