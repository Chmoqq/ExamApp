package com.example.ivan.examapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ivan.examapp.DataBase.NoteDataDelegate;

public class LearnTicketsFragment extends Fragment {

    NoteDataDelegate noteDataDelegate;

    @Override
    public void onStart() {
        (this.getActivity().findViewById(R.id.spinner)).setEnabled(false);
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.from(getContext()).inflate(R.layout.learn_tickets_fragment, container, false);
        noteDataDelegate = new NoteDataDelegate(getActivity());
        noteDataDelegate.open();
        noteDataDelegate.getAllNotes("SELECT COUNT(DISTINCT test_id) FROM answers");
        return root;
    }
}
