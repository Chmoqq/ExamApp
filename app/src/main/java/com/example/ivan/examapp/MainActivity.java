package com.example.ivan.examapp;

import android.app.FragmentTransaction;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.ivan.examapp.DataBase.NoteDataDelegate;
import com.example.ivan.examapp.Spinner.SpinnerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private List<String> elements = Arrays.asList("Украинский язык", "Математика", "История Украины", "География",
            "Биология", "Физика", "Химия", "Английский язык", "Немемцкий язык",
            "Французский язык", "Испанский язык");

    private List<String> elementsId = Arrays.asList("ukrainian", "math", "history", "geography",
            "biology", "physics", "chemistry",  "english", "dutch",
            "french", "spanish");

    private List<Integer> subjectIcons = Arrays.asList(R.drawable.ic_book_ukr, R.drawable.ic_rulers, R.drawable.ic_history,  R.drawable.ic_geography,
            R.drawable.ic_biology, R.drawable.ic_physics, R.drawable.ic_chemistry, R.drawable.ic_english_language, R.drawable.ic_deutch,
            R.drawable.ic_french, R.drawable.ic_spanish);


    FragmentMain fragmentMain;
    FragmentManager manager;

    Spinner spinner;

    public static String subject = "ukrainian";
    public static int curSubjectId = 1;

    public static int getCurSubjectId() {
        return curSubjectId;
    }

    public static String getSubject() {
        return subject;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        try {
            copyDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subject = elementsId.get(i);
                curSubjectId = i + 1;
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.detach(currentFragment);
                fragmentTransaction.attach(currentFragment);
                fragmentTransaction.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentMain = new FragmentMain();
        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fragment_container, fragmentMain).commit();
    }

    private void copyDB() throws IOException {

        String destPath = "/data/data/" + getPackageName()
                + "/databases/answers";

        File f = new File(destPath);
        InputStream in = getAssets().open("answers");
        OutputStream out = new FileOutputStream(destPath);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        in.close();
        out.close();

    }

}
