package com.pixelcard.project_truyen_as.Admin;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.pixelcard.project_truyen_as.Fragment.HomeFragment;
import com.pixelcard.project_truyen_as.R;

public class Admin_HomeActivity extends AppCompatActivity {

    FloatingActionButton fab;
    DrawerLayout drawerLayout;

    BottomNavigationView bottomNavigationView;

    NavigationView navigationView;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);

        addControl();
        setupToolbar();
        setupDrawer();

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

//            switch (item.getItemId()) {
//                case R.id.home:
//                    replaceFragment(new HomeFragment());
//                    break;
//                case R.id.shorts:
//                    replaceFragment(new ShortsFragment());
//                    break;
//                case R.id.subscriptions:
//                    replaceFragment(new SubscriptionFragment());
//                    break;
//                case R.id.library:
//                    replaceFragment(new LibraryFragment());
//                    break;
//            }

            return true;
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });
    }

    private void addControl() {
        bottomNavigationView=findViewById(R.id.bottomNavigationView_admin);
        fab=findViewById(R.id.fab_admin);
        drawerLayout=findViewById(R.id.drawer_layout_admin);
        navigationView = findViewById(R.id.nav_view_admin);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar_admin);
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
    }


    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout_admin);

        LinearLayout CreateLayout = dialog.findViewById(R.id.layoutCreate);
        LinearLayout DetailsLayout = dialog.findViewById(R.id.layoutDetals);
        LinearLayout UpdateLayout = dialog.findViewById(R.id.layoutUpdate);
        LinearLayout DeleteLayout = dialog.findViewById(R.id.layoutDelete);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        CreateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(Admin_HomeActivity.this,"Create ne",Toast.LENGTH_SHORT).show();

            }
        });

        UpdateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(Admin_HomeActivity.this,"Update ne",Toast.LENGTH_SHORT).show();

            }
        });

        DetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(Admin_HomeActivity.this,"Details Ne",Toast.LENGTH_SHORT).show();

            }
        });

        DeleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(Admin_HomeActivity.this,"Delete ne",Toast.LENGTH_SHORT).show();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}