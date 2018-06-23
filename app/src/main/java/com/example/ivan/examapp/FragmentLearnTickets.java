package com.example.ivan.examapp;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
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

import com.example.ivan.examapp.DataBase.DataBase;

import java.util.ArrayList;
import java.util.List;


public class FragmentLearnTickets extends Fragment {


    private RecyclerView ticketsList;
    private TicketListAdapter adapter;

    private List<Ticket> ticketList = new ArrayList<>();

    private DataBase dataBase;

    private static long timeStart;

    public static long getTimeStart() {
        return timeStart;
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

    @Override
    public void onStart() {
        (this.getActivity().findViewById(R.id.spinner)).setEnabled(false);
        super.onStart();
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.from(getContext()).inflate(R.layout.fragment_learn_tickets, container, false);
        dataBase = new DataBase(getActivity());
        dataBase.open();
        ticketList = dataBase.getTicket("SELECT * FROM tests WHERE subject_id=" + MainActivity.getCurSubjectId());
        ticketsList = root.findViewById(R.id.list_tickets);
        adapter = new TicketListAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        ticketsList.setLayoutManager(gridLayoutManager);
        ticketsList.setAdapter(adapter);
        return root;
    }

    private static class ListTicketsViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textView;
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;

        public ListTicketsViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_solo);
            textView = itemView.findViewById(R.id.textview_solo);
            imageView1 = itemView.findViewById(R.id.imageview_1_solo);
            imageView2 = itemView.findViewById(R.id.imageview_2_solo);
            imageView3 = itemView.findViewById(R.id.imageview_3_solo);
        }
    }

    private class TicketListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListTicketsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_list_content, parent, false));
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

        private void viewHolder2(RecyclerView.ViewHolder holder, final int position) {
            ListTicketsViewHolder listTicketsViewHolder = (ListTicketsViewHolder) holder;
            listTicketsViewHolder.textView.setText(ticketList.get(position).getName());
            listTicketsViewHolder.imageView1.setImageResource(R.drawable.ic_star_filled);
            listTicketsViewHolder.imageView2.setImageResource(R.drawable.ic_star_filled);
            listTicketsViewHolder.imageView3.setImageResource(R.drawable.ic_star_empty);
            listTicketsViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentWebView fragmentWebView = new FragmentWebView();
                    Bundle testId = new Bundle();
                    testId.putInt("test_id", ticketList.get(position).getId());
                    fragmentWebView.setArguments(testId);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    timeStart = System.currentTimeMillis();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentWebView, "webView").addToBackStack("Tickets List").commit();
                }
            });
        }
    }
}
