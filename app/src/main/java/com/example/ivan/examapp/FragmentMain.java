package com.example.ivan.examapp;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ivan.examapp.DataBase.DataBase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class FragmentMain extends Fragment {

    private FragmentLearnTickets fragmentLearnTickets;
    private FragmentStats fragmentStats;
    private FragmentManager fragmentManager;
    private DataBase dataBase;

    private AdView adView;

    private CardView learnTickets;
    private CardView testYourself;
    private CardView chooseTopic;
    private CardView stats;

    @Override
    public void onStart() {
        (this.getActivity().findViewById(R.id.spinner)).setEnabled(true);
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.from(getContext()).inflate(R.layout.content_main, container, false);
        learnTickets = root.findViewById(R.id.card_view_tickets_learn);
        stats = root.findViewById(R.id.stats_cardview);
        dataBase = new DataBase(getContext());
        dataBase.open();
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] data = dataBase.getStats(MainActivity.getCurSubjectId(), 1);
                if (data[0] == 0 && data[1] == 0) {
                    try {
                        throw new Exception();
                    } catch (Exception e) {
                        Snackbar snackbar =  Snackbar.make(getView(), "Статистика по данному предмету не доступна", Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.gray));
                        TextView tv = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                        Typeface font = ResourcesCompat.getFont(getContext(), R.font.roboto_regular);
                        tv.setTypeface(font);
                        tv.setTextColor(getResources().getColor(R.color.black_spec));
                        snackbar.show();
                    }
                } else {
                    fragmentStats = new FragmentStats();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentStats).addToBackStack("stats_fragment").commit();
                }
            }
        });
        learnTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentLearnTickets = new FragmentLearnTickets();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentLearnTickets).addToBackStack("main_fragment").commit();
            }
        });
        adView = root.findViewById(R.id.main_page_ad);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("1234567").build();
        adView.loadAd(adRequest);
        return root;
    }
}
