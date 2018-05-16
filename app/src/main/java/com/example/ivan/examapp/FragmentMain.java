package com.example.ivan.examapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ivan.examapp.DataBase.NoteDataDelegate;



public class FragmentMain extends Fragment {

    private FragmentLearnTickets fragmentLearnTickets;
    private FragmentManager fragmentManager;
    private NoteDataDelegate noteDataDelegate;

    private TextView percents_done;
    private TextView completed_questions;
    private TextView total_questions;

    private CardView learnTickets;
    private CardView testYourself;
    private CardView chooseTopic;

    @Override
    public void onStart() {
        (this.getActivity().findViewById(R.id.spinner)).setEnabled(true);
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
        noteDataDelegate = new NoteDataDelegate(getContext());
        noteDataDelegate.open();
        readFromPreference();
        return root;
    }

    private void readFromPreference() {
        String dbRequest = "SELECT questions_complete FROM user_answers WHERE subject_id=" + MainActivity.getCurSubjectId();
        String subjectIds = "SELECT id FROM tests WHERE subject_id=" + MainActivity.getCurSubjectId();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < noteDataDelegate.getList(subjectIds).size(); i++) {
            stringBuilder.append("'" + noteDataDelegate.getList(subjectIds).get(i)+ "'" + ", ");
        }
        stringBuilder.setLength(stringBuilder.length() - 2);
        String questionsCount = "SELECT COUNT(question_id) FROM answers WHERE test_id in (" + stringBuilder.toString() + ")";
        //String percentQuestionsDone = String.valueOf(Integer.parseInt(noteDataDelegate.getNote(questionsCount)) / 100 * Integer.parseInt(noteDataDelegate.getNote(dbRequest)));
        percents_done.setText("0");

//        completed_questions.setText(noteDataDelegate.getNote(dbRequest));
        completed_questions.setText("0");

        total_questions.setText(noteDataDelegate.getNote(questionsCount));
    }
}
