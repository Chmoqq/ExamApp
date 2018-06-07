package com.example.ivan.examapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ivan.examapp.DataBase.DataBase;


public class FragmentMain extends Fragment {

    private FragmentLearnTickets fragmentLearnTickets;
    private FragmentManager fragmentManager;
    private DataBase dataBase;
    private Fragment currentFragment;
    private FragmentTransaction fragmentTransaction;

    private TextView percents_done;
    private TextView completed_questions;
    private TextView total_questions;

    private CardView learnTickets;
    private CardView testYourself;
    private CardView chooseTopic;

    @Override
    public void onStart() {
        (this.getActivity().findViewById(R.id.spinner)).setEnabled(true);
        currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        fragmentTransaction = getFragmentManager().beginTransaction();
        completed_questions.setText(String.valueOf(dataBase.getCompletedAnswers()));
        total_questions.setText(dataBase.getNote(dataBase.getAnswersCount()));
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
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
        dataBase = new DataBase(getContext());
        dataBase.open();
        percents_done.setText("0");
        return root;
    }
}
