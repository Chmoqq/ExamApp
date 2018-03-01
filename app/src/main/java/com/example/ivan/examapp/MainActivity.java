package com.example.ivan.examapp;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.example.ivan.examapp.RecyclerView.AlertListAdapter;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    AlertListAdapter alertListAdapter;

    FragmentMain fragmentMain;
    FragmentManager manager;
    FloatingActionButton fab;

    public static String subject;

    public static String getSubject() {
        return subject;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.subject1) {
            subject = "ukrainian";
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            ft.detach(currentFragment);
            ft.attach(currentFragment);
            ft.commit();
        } else if (id == R.id.subject2) {
            subject = "english";
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            ft.detach(currentFragment);
            ft.attach(currentFragment);
            ft.commit();
        } else if (id == R.id.subject3) {
            subject = "math";
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            ft.detach(currentFragment);
            ft.attach(currentFragment);
            ft.commit();
        } else if (id == R.id.subject4) {

        } else if (id == R.id.share) {

        } else if (id == R.id.settings) {

        } else if (id == R.id.subjects) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initAlertList();
            }
        });

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentMain = new FragmentMain();
        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fragment_container, fragmentMain).commit();
    }

    private void initAlertList() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(R.layout.alert_add_menu_layout);
        final AlertDialog ad = builder.show();
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int displayWidth = displayMetrics.widthPixels;
//        int displayHeight = displayMetrics.heightPixels;
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.copyFrom(ad.getWindow().getAttributes());
//        int dialogWindowWidth = (int) (displayWidth * 0.85f);
//        int dialogWindowHeight = (int) (displayHeight * 0.85f);
//        layoutParams.width = dialogWindowWidth;
//        layoutParams.height = dialogWindowHeight;
//        ad.getWindow().setAttributes(layoutParams);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        alertListAdapter = new AlertListAdapter(getApplicationContext());
        recyclerView = ad.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(alertListAdapter);
    }
}
