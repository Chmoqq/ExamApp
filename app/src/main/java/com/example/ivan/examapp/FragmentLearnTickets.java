package com.example.ivan.examapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivan.examapp.DataBase.NoteDataDelegate;

import java.util.ArrayList;
import java.util.List;


public class FragmentLearnTickets extends Fragment {

    NoteDataDelegate noteDataDelegate;
    RecyclerView ticketsList;
    TicketListAdapter adapter;

    int ticketsCount;
    List<Integer> test_idValues = new ArrayList<>();

    @Override
    public void onStart() {
        (this.getActivity().findViewById(R.id.spinner)).setEnabled(false);
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.from(getContext()).inflate(R.layout.fragment_learn_tickets, container, false);
        noteDataDelegate = new NoteDataDelegate(getActivity());
        noteDataDelegate.open();
        ticketsCount = Integer.parseInt(noteDataDelegate.getAllNotes("SELECT COUNT(DISTINCT test_id) FROM answers"));
        test_idValues = noteDataDelegate.getValues("SELECT test_id from answers");
        ticketsList = root.findViewById(R.id.list_tickets);
        adapter = new TicketListAdapter(ticketsCount);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        ticketsList.setLayoutManager(linearLayoutManager);
        ticketsList.setAdapter(adapter);
        return root;
    }

    private static class ListTicketsViewHolder extends RecyclerView.ViewHolder {

        TextView textView1;
        TextView textView2;
        ImageView imageView1_1;
        ImageView imageView1_2;
        ImageView imageView1_3;
        ImageView imageView2_1;
        ImageView imageView2_2;
        ImageView imageView2_3;

        public ListTicketsViewHolder(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textview_1);
            textView2 = itemView.findViewById(R.id.textview_2);
            imageView1_1 = itemView.findViewById(R.id.imageview_1_1);
            imageView1_2 = itemView.findViewById(R.id.imageview_1_2);
            imageView1_3 = itemView.findViewById(R.id.imageview_1_3);
            imageView2_1 = itemView.findViewById(R.id.imageview_2_1);
            imageView2_2 = itemView.findViewById(R.id.imageview_2_2);
            imageView2_3 = itemView.findViewById(R.id.imageview_2_3);
        }
    }

    private class TicketListAdapter extends RecyclerView.Adapter<ListTicketsViewHolder> {

        int ticketsCount;

        public TicketListAdapter(int ticketsCount) {
            this.ticketsCount = ticketsCount;
        }

        @Override
        public ListTicketsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tickts_content, parent, false);
            return new ListTicketsViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(ListTicketsViewHolder holder, int position) {
            holder.textView1.setText(test_idValues.get(position).toString());
            holder.textView2.setText(test_idValues.get(position + 1).toString());
        }

        @Override
        public int getItemCount() {
           return ticketsCount;
        }
    }
}
