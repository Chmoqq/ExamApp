package com.example.ivan.examapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


public class FragmentTestInfo extends Fragment {

    FrameLayout backToMain;
    FragmentManager fragmentManager;
    FragmentMain fragmentMain;

    TextView rightQuests;
    TextView totalTestQuests;
    TextView timeSpent;

    private AdView adView;

    ImageView star1;
    ImageView star2;
    ImageView star3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.from(getContext()).inflate(R.layout.total_stats, container, false);
        rightQuests = root.findViewById(R.id.right_quests_of_final_page);
        totalTestQuests = root.findViewById(R.id.total_quest_final_page);
        timeSpent = root.findViewById(R.id.total_time_spent);
        star1 = root.findViewById(R.id.star1_final_page);
        star2 = root.findViewById(R.id.star2_final_page);
        star3 = root.findViewById(R.id.star3_final_page);
        backToMain = root.findViewById(R.id.back_to_main_btn);
        setText();
        return root;
    }

    private void setText() {
        Bundle args = getArguments();
        rightQuests.setText(args.getString("right"));
        totalTestQuests.setText(args.getString("total"));
        int rightQuestint = Integer.parseInt(rightQuests.getText().toString());
        int totalQuestint = Integer.parseInt(totalTestQuests.getText().toString());
        Spanned hours = Html.fromHtml(String.format("<font color='#f06d4b'>%s</font>", "За " + args.getString("hours") + " часов, "));
        Spanned mins = Html.fromHtml(String.format("<font color='#f06d4b'>%s</font>", args.getString("mins") + "  минут, "));
        Spanned secs = Html.fromHtml(String.format("<font color='#f06d4b'>%s</font>", args.getString("secs") + " секунд"));
        CharSequence totalTime = TextUtils.concat(hours, mins, secs);
        timeSpent.setText(totalTime);
        int rating = rightQuestint / totalQuestint;
        star1.setImageDrawable(rating >= 0.33 ? getResources().getDrawable(R.drawable.ic_star_filled) : getResources().getDrawable(R.drawable.ic_star_empty));
        star2.setImageDrawable(rating >= 0.66 ? getResources().getDrawable(R.drawable.ic_star_filled) : getResources().getDrawable(R.drawable.ic_star_empty));
        star3.setImageDrawable(rating >= 0.1 ? getResources().getDrawable(R.drawable.ic_star_filled) : getResources().getDrawable(R.drawable.ic_star_empty));
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentMain = new FragmentMain();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentMain).commit();
            }
        });
    }
}
