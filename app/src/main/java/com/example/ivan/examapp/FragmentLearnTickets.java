package com.example.ivan.examapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
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

    FragmentManager fragmentManager;
    FragmentWebView fragmentWebView;

    NoteDataDelegate noteDataDelegate;
    RecyclerView ticketsList;
    TicketListAdapter adapter;

    int textidHelper;
    int ticketsCount;
    int ticketsCount2;
    List<Ticket> ticketList = new ArrayList<>();

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
//        ticketsCount = Integer.parseInt(noteDataDelegate.getAllNotes("SELECT COUNT(DISTINCT id) FROM tests WHERE test_id=" + ));
        ticketList = noteDataDelegate.getTicket("SELECT * FROM tests WHERE subject_id=" + MainActivity.getCurSubjectId());
        ticketsList = root.findViewById(R.id.list_tickets);
        adapter = new TicketListAdapter(ticketsCount);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        ticketsList.setLayoutManager(gridLayoutManager);
        ticketsList.setAdapter(adapter);
        return root;
    }

    private static class ListTicketsViewHolder1 extends RecyclerView.ViewHolder {

        CardView cardView1;
        CardView cardView2;
        TextView textView1;
        TextView textView2;
        ImageView imageView1_1;
        ImageView imageView1_2;
        ImageView imageView1_3;
        ImageView imageView2_1;
        ImageView imageView2_2;
        ImageView imageView2_3;

        public ListTicketsViewHolder1(View itemView) {
            super(itemView);
            cardView1 = itemView.findViewById(R.id.cardview_1);
            cardView2 = itemView.findViewById(R.id.cardview_2);
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

    private static class ListTicketsViewHolder2 extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textView;
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;

        public ListTicketsViewHolder2(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_solo);
            textView = itemView.findViewById(R.id.textview_solo);
            imageView1 = itemView.findViewById(R.id.imageview_1_solo);
            imageView2 = itemView.findViewById(R.id.imageview_2_solo);
            imageView3 = itemView.findViewById(R.id.imageview_3_solo);
        }
    }

    private class TicketListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        int ticketsCount;

        public TicketListAdapter(int ticketsCount) {
            this.ticketsCount = ticketsCount;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListTicketsViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_list_content1, parent, false));
        }


        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            viewHolder2(holder, position);

        }

        @Override
        public int getItemCount() {
            return ticketList.size();
        }

        private void viewHolder1(RecyclerView.ViewHolder holder, int position) {
            ListTicketsViewHolder1 listTicketsViewHolder1 = (ListTicketsViewHolder1) holder;
            listTicketsViewHolder1.textView1.setText(ticketList.get(position * 2).getName());
            listTicketsViewHolder1.textView2.setText(ticketList.get(position * 2 + 1).getName());
            listTicketsViewHolder1.imageView1_1.setImageResource(R.drawable.ic_star_filled);
            listTicketsViewHolder1.imageView1_2.setImageResource(R.drawable.ic_star_filled);
            listTicketsViewHolder1.imageView1_3.setImageResource(R.drawable.ic_star_empty);
            listTicketsViewHolder1.imageView2_1.setImageResource(R.drawable.ic_star_filled);
            listTicketsViewHolder1.imageView2_2.setImageResource(R.drawable.ic_star_empty);
            listTicketsViewHolder1.imageView2_3.setImageResource(R.drawable.ic_star_empty);
            listTicketsViewHolder1.cardView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentWebView = new FragmentWebView();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentWebView).addToBackStack("Tickets List").commit();
                }
            });
            listTicketsViewHolder1.cardView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentWebView = new FragmentWebView();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentWebView).addToBackStack("Tickets List").commit();
                }
            });
        }

        private void viewHolder2(RecyclerView.ViewHolder holder, int position) {
            ListTicketsViewHolder2 listTicketsViewHolder2 = (ListTicketsViewHolder2) holder;
            listTicketsViewHolder2.textView.setText(ticketList.get(position).getName());
            listTicketsViewHolder2.imageView1.setImageResource(R.drawable.ic_star_filled);
            listTicketsViewHolder2.imageView2.setImageResource(R.drawable.ic_star_filled);
            listTicketsViewHolder2.imageView3.setImageResource(R.drawable.ic_star_empty);
            listTicketsViewHolder2.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentWebView = new FragmentWebView();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentWebView).addToBackStack("Tickets List").commit();
                }
            });
        }
    }
}
