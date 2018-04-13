package com.example.ivan.examapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
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

    RecyclerView ticketsList;
    TicketListAdapter adapter;

    List<Ticket> ticketList = new ArrayList<>();
    NoteDataDelegate noteDataDelegate;

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
        ticketList = noteDataDelegate.getTicket("SELECT * FROM tests WHERE subject_id=" + MainActivity.getCurSubjectId());
        ticketsList = root.findViewById(R.id.list_tickets);
        adapter = new TicketListAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        ticketsList.setLayoutManager(gridLayoutManager);
        ticketsList.setAdapter(adapter);
        return root;
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


        public TicketListAdapter() {
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
