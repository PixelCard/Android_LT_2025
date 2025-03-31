package com.pixelcard.project_truyen_as.Admin;

import static com.pixelcard.project_truyen_as.R.id.Account_navi;
import static com.pixelcard.project_truyen_as.R.id.Chapter_navi;
import static com.pixelcard.project_truyen_as.R.id.Comment_navi;
import static com.pixelcard.project_truyen_as.R.id.Product_navi;
import static com.pixelcard.project_truyen_as.R.id.ThongKe;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pixelcard.project_truyen_as.Fragment.AdminUser_Fragment;
import com.pixelcard.project_truyen_as.Fragment.Fragment_Admin_Comment;
import com.pixelcard.project_truyen_as.Fragment.Fragment_admin_chapter_home;
import com.pixelcard.project_truyen_as.Fragment.HomeFragment;
import com.pixelcard.project_truyen_as.Fragment.Product_admin_Test_Fragment;
import com.pixelcard.project_truyen_as.Fragment.StatisticsFragment;
import com.pixelcard.project_truyen_as.R;

public class Admin_Home_Activity extends AppCompatActivity {
    Button btnPageProduct,btnPageChapter,btnPageComment,btnPageThongKe,btnPageAccount;
    BottomNavigationView bottomNavigationView;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControl();
        handleEvent();
    }


    @SuppressLint("NonConstantResourceId")
    private void handleEvent() {
        replaceFragment(new Product_admin_Test_Fragment());

        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnItemSelectedListener(item -> {
                int id=item.getItemId();
                 if(id== Chapter_navi){
                     replaceFragment(new Fragment_admin_chapter_home());
                 }
                 if(id==Product_navi){
                     replaceFragment(new Product_admin_Test_Fragment());
                 }
                 if(id == Account_navi)
                     replaceFragment(new AdminUser_Fragment());
                if(id==Comment_navi){
                    replaceFragment(new AdminUser_Fragment());
                }
                return true;
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin_Home_Activity.this,Create_AdminActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControl() {
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        fab=findViewById(R.id.fab_create);
    }


    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragement_layout_admin, fragment);
        fragmentTransaction.commit();
    }
}