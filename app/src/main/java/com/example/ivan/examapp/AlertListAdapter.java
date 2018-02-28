package com.example.ivan.examapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;


public class AlertListAdapter extends BaseAdapter {

    private List<String> elements = Arrays.asList("Английский язык", "Математика", "Украинский язык", "Биология",
            "География", "Право", "Физика", "Химия", "Немемцкий язык",
            "Французский язык", "Испанский язык", "История Украины");

    private List<Integer> subjectIcons = Arrays.asList(R.drawable.ic_english_language, R.drawable.ic_rulers,
            R.drawable.ic_book_ukr, R.drawable.ic_biology, R.drawable.ic_geography, R.drawable.ic_human_rights,
            R.drawable.ic_physics, R.drawable.ic_chemistry, R.drawable.ic_deuch, R.drawable.ic_french, R.drawable.ic_spanish, R.drawable.ic_history);

    private Context context;
    private LayoutInflater layoutInflater;

    AlertListAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int i) {
        return elements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.subject_list_item, viewGroup, false);
            TextView subjectName = view.findViewById(R.id.subject_name_text_view);
            ImageView subjectIcon = view.findViewById(R.id.subject_icon_list);
            subjectIcon.setImageResource(subjectIcons.get(i));
            subjectName.setText(elements.get(i));
        }
        return view;
    }
}
