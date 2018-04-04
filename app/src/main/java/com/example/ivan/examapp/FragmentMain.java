package com.example.ivan.examapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentMain extends Fragment {

    private static final String namedSharedPrefsKey = "namedPrefs";

    FragmentLearnTickets fragmentLearnTickets;
    FragmentManager fragmentManager;


    static SharedPreferences namedPrefs;
    TextView percents_done;
    TextView completed_questions;
    TextView total_questions;

    CardView learnTickets;
    CardView testYourself;
    CardView chooseTopic;

    @Override
    public void onStart() {
        (this.getActivity().findViewById(R.id.spinner)).setEnabled(true);
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.from(getContext()).inflate(R.layout.content_main, container, false);
        learnTickets = root.findViewById(R.id.card_view_tickets_learn);
        learnTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentLearnTickets = new FragmentLearnTickets();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentLearnTickets).addToBackStack("main_fragment").commit();
            }
        });
        percents_done = root.findViewById(R.id.percentage_done);
        completed_questions = root.findViewById(R.id.completed_questions_exam);
        total_questions = root.findViewById(R.id.total_questions);
        namedPrefs = getActivity().getSharedPreferences(namedSharedPrefsKey, Context.MODE_PRIVATE);

        readFromPreference(namedPrefs, MainActivity.getSubject());
        return root;
    }

    private void readFromPreference(SharedPreferences preferences, String subject) {
        String percents_done_data = preferences.getString("percents_done_" + subject, "0");
        percents_done.setText(percents_done_data);

        String completed_questions_data = preferences.getString("completed_questions_" + subject, "0");
        completed_questions.setText(completed_questions_data);

        int resourceId = this.getResources().getIdentifier("total_questions_" + subject, "string", getActivity().getPackageName());
        total_questions.setText(resourceId);
    }
}
