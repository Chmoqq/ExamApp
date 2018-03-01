package com.example.ivan.examapp.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ivan.examapp.R;

import java.util.Arrays;
import java.util.List;


public class AlertListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<String> elements = Arrays.asList("Английский язык", "Математика", "Украинский язык", "Биология",
            "География", "Право", "Физика", "Химия", "Немемцкий язык",
            "Французский язык", "Испанский язык", "История Украины");

    private List<Integer> subjectIcons = Arrays.asList(R.drawable.ic_english_language, R.drawable.ic_rulers,
            R.drawable.ic_book_ukr, R.drawable.ic_biology, R.drawable.ic_geography, R.drawable.ic_human_rights,
            R.drawable.ic_physics, R.drawable.ic_chemistry, R.drawable.ic_deuch, R.drawable.ic_french, R.drawable.ic_spanish, R.drawable.ic_history);

    private Context context;
    private LayoutInflater layoutInflater;

    public AlertListAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_list_item, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.subjectName.setText(elements.get(position));
        holder.subjectIcon.setImageResource(subjectIcons.get(position));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (holder.checkable != true) {
                    holder.checkable = true;
                    holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.gray));
                    holder.frameLayout.setBackgroundColor(context.getResources().getColor(R.color.gray));
                } else {
                    holder.checkable = false;
                    holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.frameLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                }
                return true;
            }
        });
        holder.subjectAddedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.subjectAddedIcon.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }
}
