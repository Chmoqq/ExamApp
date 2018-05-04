package com.example.ivan.examapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class FragmentTestInfo extends Fragment {

    TextView rightQuests;
    TextView totalTestQuests;

    ImageView star1;
    ImageView star2;
    ImageView star3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.from(getContext()).inflate(R.layout.total_stats, container, false);
        rightQuests = root.findViewById(R.id.right_quests_of_final_page);
        totalTestQuests = root.findViewById(R.id.total_quest_final_page);
        star1 = root.findViewById(R.id.star1_final_page);
        star2 = root.findViewById(R.id.star2_final_page);
        star3 = root.findViewById(R.id.star3_final_page);
        setText();
        return root;
    }

    private void setText() {
        Bundle args = getArguments();
        rightQuests.setText(args.getString("right"));
        totalTestQuests.setText(args.getString("total"));
        int rightQuestint = Integer.parseInt(rightQuests.getText().toString());
        int totalQuestint = Integer.parseInt(totalTestQuests.getText().toString());
        if (rightQuestint < totalQuestint / 3) {
            star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_empty));
            star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_empty));
            star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_empty));
        } else if (rightQuestint >= totalQuestint / 3 && rightQuestint < rightQuestint / 2) {
            star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_filled));
            star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_empty));
            star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_empty));
        } else if (rightQuestint >= totalQuestint / 2 && rightQuestint < totalQuestint) {
            star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_filled));
            star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_filled));
            star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_empty));
        } else if (rightQuestint == totalQuestint) {
            star1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_filled));
            star2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_filled));
            star3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_filled));
        }
    }
}
