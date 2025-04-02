package com.pixelcard.project_truyen_as;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pixelcard.project_truyen_as.Fragment.FragmentSearchProduct;
import com.pixelcard.project_truyen_as.Fragment.Fragment_Thanks;
import com.pixelcard.project_truyen_as.Fragment.Fragment_filter_category_customer;
import com.pixelcard.project_truyen_as.Fragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private SharedPreferences userPreferences, settingPreferences;
    private FirebaseAuth mAuth;


    private static final String KEY_EMAIL = "email";
    private static final String PREFS_USER = "UserPrefs";
    private static final String PREFS_SETTING = "SettingPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userPreferences = getSharedPreferences(PREFS_USER, MODE_PRIVATE);
        settingPreferences = getSharedPreferences(PREFS_SETTING, MODE_PRIVATE);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
//        String adminId = userRef.push().getKey();

//        User adminUser = new User(adminId, "admin@gmail.com", "Admin", "admin");
//
//        userRef.child(Objects.requireNonNull(adminId)).setValue(adminUser)
//                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Admin created successfully!"))
//                .addOnFailureListener(e -> Log.e("Firebase", "Error: " + e.getMessage()));

        // Kiểm tra trạng thái đăng nhập Firebase
        checkUserLoginStatus();



        int nightMode = settingPreferences.getInt("nightMode", AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(nightMode);

        setContentView(R.layout.activity_main);

        initViews();
        setupToolbar();
        setupDrawer();
        setupHeaderData();
//        checkIfUserIsAdmin();

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), R.id.nav_home);
        }
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupHeaderData() {
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderEmail = headerView.findViewById(R.id.navheader_email);

        // Lấy email từ Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navHeaderEmail.setText(currentUser.getEmail());
        } else {
            navHeaderEmail.setText("Khách");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            loadFragment(new HomeFragment(), id);
        }

        else if (id == R.id.nav_logout) {
            handleLogout();
        }

        else if(id == R.id.nav_Thanks){
            loadFragment(new Fragment_Thanks(),id);
        }

        else if(id==R.id.nav_FilterCategory){
            loadFragment(new Fragment_filter_category_customer(),id);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment, int menuItemId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
        navigationView.setCheckedItem(menuItemId);
    }

    @Override
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
        } else if (id == R.id.icn_timkiem_navi) {
            loadFragment(new FragmentSearchProduct(),id);
            return true;
        }
        else if (id == R.id.nav_logout) {
            handleLogout();
            return true;
        } else if (id == R.id.action_profile) {
            startActivity(new Intent(this, AccountDetailsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleLogout() {
        mAuth.signOut();  // Đăng xuất Firebase

        SharedPreferences.Editor editor = userPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

        // Cập nhật giao diện sau khi đăng xuất
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderEmail = headerView.findViewById(R.id.navheader_email);
        navHeaderEmail.setText("Khách");

        Intent intent = new Intent(this, DangNhapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void checkUserLoginStatus() {
        FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putBoolean("isLogin", user != null);
        editor.apply();
    }
}
