package com.pixelcard.project_truyen_as;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.pixelcard.project_truyen_as.Fragment.AboutFragment;
import com.pixelcard.project_truyen_as.Fragment.HomeFragment;
import com.pixelcard.project_truyen_as.Fragment.SettingFragment;
import com.pixelcard.project_truyen_as.Fragment.ShareFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private SharedPreferences userPreferences, settingPreferences;

    private static final String KEY_EMAIL = "email";
    private static final String PREFS_USER = "UserPrefs";
    private static final String PREFS_SETTING = "SettingPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userPreferences = getSharedPreferences(PREFS_USER, MODE_PRIVATE);
        settingPreferences = getSharedPreferences(PREFS_SETTING, MODE_PRIVATE);

        int nightmode = settingPreferences.getInt("nightMode", AppCompatDelegate.MODE_NIGHT_NO);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_main);

        initViews();
        setupToolbar();
        setupDrawer();
        setupHeaderData();

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
        TextView navheaderEmail = headerView.findViewById(R.id.navheader_email);
        String email = userPreferences.getString(KEY_EMAIL, "");
        Log.d("MainActivity", "Email: " + email);
        navheaderEmail.setText(email);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            loadFragment(new HomeFragment(), id);
        } else if (id == R.id.nav_settings) {
            loadFragment(new SettingFragment(), id);
        } else if (id == R.id.nav_share) {
            loadFragment(new ShareFragment(), id);
        } else if (id == R.id.nav_about) {
            loadFragment(new AboutFragment(), id);
        } else if (id == R.id.nav_logout) {
            handleLogout();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_login) {
            startActivity(new Intent(this, DangNhapActivity.class));
            return true;
        } else if (id == R.id.action_search) {
            startActivity(new Intent(this, DangNhapActivity.class));
            return true;
        }
        else if (id == R.id.action_theme) {
            handleTheme();
            return true;
        }
        else if ( id == R.id.nav_logout)
        {
          handleLogout();
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(this, AccountDetailsActivity.class)); // Mở trang cá nhân
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Lấy trạng thái đăng nhập từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLogin", false);

        // Lấy item Đăng nhập và Trang cá nhân
        MenuItem loginItem = menu.findItem(R.id.action_login);
        MenuItem profileItem = menu.findItem(R.id.action_profile);

        if (isLoggedIn) {
            loginItem.setVisible(false); // Ẩn Đăng nhập
            profileItem.setVisible(true); // Hiện Trang cá nhân
        } else {
            loginItem.setVisible(true); // Hiện Đăng nhập
            profileItem.setVisible(false); // Ẩn Trang cá nhân
        }
        return true;
    }

    public void handleTheme() {
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        int newNightMode;

        if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            newNightMode = AppCompatDelegate.MODE_NIGHT_NO;
        } else {
            newNightMode = AppCompatDelegate.MODE_NIGHT_YES;
        }

        // Lưu trạng thái chế độ vào SharedPreferences
        SharedPreferences.Editor editor = settingPreferences.edit();
        editor.putInt("nightMode", newNightMode);
        editor.apply();

        // Cập nhật chế độ mới
        AppCompatDelegate.setDefaultNightMode(newNightMode);

        restartApp();
    }

    private void restartApp() {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        assert i != null;
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    private void handleLogout() {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.clear();
        editor.apply(); // Đảm bảo lưu ngay lập tức

        Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

        // Cập nhật giao diện
        View headerView = navigationView.getHeaderView(0);
        TextView navheaderEmail = headerView.findViewById(R.id.navheader_email);
        navheaderEmail.setText(""); // Xóa email trong header

        // Chuyển về màn hình đăng nhập
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}