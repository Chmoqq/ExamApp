package com.example.ivan.examapp.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivan.examapp.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    ImageView subjectIcon;
    ImageView subjectAddedIcon;
    TextView subjectName;
    FrameLayout frameLayout;
    boolean checkable;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        checkable = false;
        frameLayout = itemView.findViewById(R.id.recycler_view_frame_item);
        subjectIcon = itemView.findViewById(R.id.subject_icon_list);
        subjectName = itemView.findViewById(R.id.subject_name_text_view);
        subjectAddedIcon = itemView.findViewById(R.id.subject_add_list_btn);
    }
}
