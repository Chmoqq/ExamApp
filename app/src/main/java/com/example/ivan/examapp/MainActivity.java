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
import android.view.MenuItem;
import android.view.View;

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
            subject = FragmentMain.namedPrefs.getString("subject1_favourites", "");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            ft.detach(currentFragment);
            ft.attach(currentFragment);
            ft.commit();
        } else if (id == R.id.subject2) {
            subject = FragmentMain.namedPrefs.getString("subject2_favourites", "");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            ft.detach(currentFragment);
            ft.attach(currentFragment);
            ft.commit();
        } else if (id == R.id.subject3) {
            subject = FragmentMain.namedPrefs.getString("subject3_favourites", "");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            ft.detach(currentFragment);
            ft.attach(currentFragment);
            ft.commit();
        } else if (id == R.id.subject4) {
            subject = FragmentMain.namedPrefs.getString("subject4_favourites", "");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            ft.detach(currentFragment);
            ft.attach(currentFragment);
            ft.commit();
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        alertListAdapter = new AlertListAdapter(getApplicationContext());
        recyclerView = ad.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(alertListAdapter);
    }
}
