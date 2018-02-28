package com.example.ivan.examapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentMain extends Fragment {

    private final String namedSharedPrefsKey = "namedPrefs";

    SharedPreferences namedPrefs;
    TextView percents_done;
    TextView completed_questions;
    TextView total_questions;

    CardView learnTickets;
    CardView testYourself;
    CardView chooseTopic;

    @Override
    public void onStart() {
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
        percents_done = root.findViewById(R.id.percentage_done);
        completed_questions = root.findViewById(R.id.completed_questions_exam);
        total_questions = root.findViewById(R.id.total_questions);
        namedPrefs = getActivity().getSharedPreferences(namedSharedPrefsKey, Context.MODE_PRIVATE);
        if (MainActivity.getSubject() == null) {
            readFromPreference(namedPrefs, "ukrainian");
        } else {
            readFromPreference(namedPrefs, MainActivity.getSubject());
        }
        return root;
    }

    private void readFromPreference(SharedPreferences preferences, String subject) {
        switch (subject) {
            case "math": {
                String percents_done_data = preferences.getString("percents_done_math", "0");
                percents_done.setText(percents_done_data);

                String completed_questions_data = preferences.getString("completed_questions_math", "0");
                completed_questions.setText(completed_questions_data);

                total_questions.setText("823");
                break;
            }
            case "english": {
                String percents_done_data = preferences.getString("percents_done_eng", "0");
                percents_done.setText(percents_done_data);

                String completed_questions_data = preferences.getString("completed_questions_eng", "0");
                completed_questions.setText(completed_questions_data);

                total_questions.setText("582");
                break;
            }
            case "ukrainian": {
                String percents_done_data = preferences.getString("percents_done_ukr", "0");
                percents_done.setText(percents_done_data);

                String completed_questions_data = preferences.getString("completed_questions_ukr", "0");
                completed_questions.setText(completed_questions_data);

                total_questions.setText("854");
                break;
            }
        }
    }
}
