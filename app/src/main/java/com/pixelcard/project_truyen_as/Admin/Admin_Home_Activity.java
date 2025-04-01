package com.pixelcard.project_truyen_as.Admin;

import static com.pixelcard.project_truyen_as.R.id.Account_navi;
import static com.pixelcard.project_truyen_as.R.id.Chapter_navi;
import static com.pixelcard.project_truyen_as.R.id.Comment_navi;
import static com.pixelcard.project_truyen_as.R.id.Product_navi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pixelcard.project_truyen_as.AccountDetailsActivity;
import com.pixelcard.project_truyen_as.DangNhapActivity;
import com.pixelcard.project_truyen_as.Fragment.AdminUser_Fragment;
import com.pixelcard.project_truyen_as.Fragment.FragmentSearchProduct;
import com.pixelcard.project_truyen_as.Fragment.Fragment_Admin_Comment;
import com.pixelcard.project_truyen_as.Fragment.Fragment_admin_chapter_home;
import com.pixelcard.project_truyen_as.Fragment.HomeFragment;
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

    private Toolbar toolbaradmin;
    private SharedPreferences userPreferences;
    private FirebaseAuth mAuth;
    private static final String KEY_EMAIL = "email";
    private static final String PREFS_USER = "UserPrefs";
    private static final String PREFS_SETTING = "SettingPrefs";

    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


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

        // Kiểm tra trạng thái đăng nhập Firebase
        checkUserLoginStatus();


        // Ánh xạ các view từ layout
        addControl();
        handleEvent();
        setupToolbar();
        setupDrawer();
        setupHeaderData();

        // Load Fragment mặc định
        if (savedInstanceState == null) {
            loadFragment(new Product_admin_Test_Fragment());
            bottomNavigationView.setSelectedItemId(R.id.Product_navi);
        }
    }

    private void handleEvent() {
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
            } else if (id == R.id.Comment_navi) {

                loadFragment(new Fragment_Admin_Comment());
                return true;
            }
            return false;
        });

        // Xử lý FloatingActionButton (ví dụ chuyển đến màn hình thêm sản phẩm)
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_Home_Activity.this, Create_AdminActivity.class);
            startActivity(intent);
        });
    }

    private void addControl() {
        drawerLayout = findViewById(R.id.drawer_layout_admin);
        navigationView = findViewById(R.id.nav_view_admin);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab_create);
        toolbaradmin=findViewById(R.id.toolbar_admin);
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
        if (item.getItemId() == R.id.nav_logout_admin) {
            handleLogout();
        }
        else if(item.getItemId() == R.id.ThongKe)
            loadFragment(new StatisticsFragment());
        else if(item.getItemId()==R.id.nav_home_admin){
            loadFragment(new Product_admin_Test_Fragment());
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleLogout() {
        mAuth.signOut();  // Đăng xuất Firebase

        SharedPreferences.Editor editor = userPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, DangNhapActivity.class);
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

    private void checkUserLoginStatus() {
        FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences.Editor editor = userPreferences.edit();
        if (user == null) {
            return;
        }

        userRef.child(user.getUid()).child("role").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String role = task.getResult().getValue(String.class);
                if ("0".equals(role)) {
                    Intent intent = new Intent(Admin_Home_Activity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Đóng trang admin
                }
            }
        });


        editor.putBoolean("isLogin", user != null);
        editor.apply();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbaradmin);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbaradmin, R.string.open_nav, R.string.close_nav
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupHeaderData() {
        View headerView = navigationView.getHeaderView(0);
        TextView navheaderEmailAdmin = headerView.findViewById(R.id.navheader_email_admin);

        // Lấy email từ Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navheaderEmailAdmin.setText(currentUser.getEmail());
        } else {
            navheaderEmailAdmin.setText("Khách");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem loginItem = menu.findItem(R.id.action_login);
        MenuItem profileItem = menu.findItem(R.id.action_profile);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            loginItem.setVisible(false);
            profileItem.setVisible(true);
        } else {
            loginItem.setVisible(true);
            profileItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_login) {
            startActivity(new Intent(this, DangNhapActivity.class));
            return true;
        }

        else if (id == R.id.nav_logout) {
            handleLogout();
            return true;
        }

        else if (id == R.id.action_profile) {
            startActivity(new Intent(this, AccountDetailsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
