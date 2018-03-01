package com.example.ivan.examapp;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Spinner;

import com.example.ivan.examapp.Spinner.SpinnerAdapter;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private List<String> elements = Arrays.asList("Английский язык", "Математика", "Украинский язык", "Биология",
            "География", "Право", "Физика", "Химия", "Немемцкий язык",
            "Французский язык", "Испанский язык", "История Украины");

    private List<Integer> subjectIcons = Arrays.asList(R.drawable.ic_english_language, R.drawable.ic_rulers,
            R.drawable.ic_book_ukr, R.drawable.ic_biology, R.drawable.ic_geography, R.drawable.ic_human_rights,
            R.drawable.ic_physics, R.drawable.ic_chemistry, R.drawable.ic_deutch, R.drawable.ic_french, R.drawable.ic_spanish, R.drawable.ic_history);


    FragmentMain fragmentMain;
    FragmentManager manager;

    Spinner spinner;

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

        if (id == R.id.share) {

        } else if (id == R.id.settings) {
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        spinner = findViewById(R.id.spinner);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getApplicationContext(), subjectIcons, elements);
        spinner.setAdapter(spinnerAdapter);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentMain = new FragmentMain();
        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fragment_container, fragmentMain).commit();
    }

}
