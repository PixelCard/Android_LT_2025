package com.pixelcard.project_truyen_as.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.pixelcard.project_truyen_as.DangNhapActivity;
import com.pixelcard.project_truyen_as.Fragment.AdminUser_Fragment;
import com.pixelcard.project_truyen_as.Fragment.Fragment_admin_chapter_home;
import com.pixelcard.project_truyen_as.Fragment.Product_admin_Test_Fragment;
import com.pixelcard.project_truyen_as.Fragment.StatisticsFragment;
import com.pixelcard.project_truyen_as.MainActivity;
import com.pixelcard.project_truyen_as.R;

public class Admin_Home_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;
    private NavigationView navigationView;
    private ImageView btnOpenDrawer;

    private SharedPreferences userPreferences;
    private FirebaseAuth mAuth;
    private static final String PREFS_USER = "UserPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // Ánh xạ các view từ layout
        drawerLayout = findViewById(R.id.drawer_layout_admin);
        navigationView = findViewById(R.id.nav_view_admin);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab_create);
//        btnOpenDrawer = findViewById(R.id.);

        userPreferences = getSharedPreferences(PREFS_USER, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        // Xử lý mở Navigation Drawer bằng nút riêng (ImageView) nếu có
        if (btnOpenDrawer != null) {
            btnOpenDrawer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        // Xử lý sự kiện cho NavigationView
        navigationView.setNavigationItemSelectedListener(this);

        // Xử lý Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.Account_navi) {
                loadFragment(new AdminUser_Fragment());
                return true;
            } else if (id == R.id.Product_navi) {
                loadFragment(new Product_admin_Test_Fragment());
                return true;
            } else if (id == R.id.Chapter_navi) {
                loadFragment(new Fragment_admin_chapter_home());
                return true;
//            } else if (id == R.id.Comment_navi) {

//                loadFragment(new Comment_Fragment());
//                return true;
            }
            return false;
        });

        // Xử lý FloatingActionButton (ví dụ chuyển đến màn hình thêm sản phẩm)
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_Home_Activity.this, DangNhapActivity.class);
            // Hoặc: new Intent(Admin_Home_Activity.this, AdminAddProductActivity.class)
            startActivity(intent);
        });

        // Load Fragment mặc định
        if (savedInstanceState == null) {
            loadFragment(new Product_admin_Test_Fragment());
            bottomNavigationView.setSelectedItemId(R.id.Product_navi);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Xử lý các mục trong Navigation Drawer (ví dụ: logout)
        if (item.getItemId() == R.id.nav_logout) {
            handleLogout();
        }
        else if(item.getItemId() == R.id.ThongKe)
            loadFragment(new StatisticsFragment());
        // ...
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleLogout() {
        mAuth.signOut();  // Đăng xuất Firebase
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Admin_Home_Activity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
