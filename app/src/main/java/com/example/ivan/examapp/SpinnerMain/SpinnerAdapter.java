package com.example.ivan.examapp.SpinnerMain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivan.examapp.R;

import java.util.List;


public class SpinnerAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    Context context;
    List<String> titles;
    List<Integer> icons;

    public SpinnerAdapter(Context applicationContext, List<Integer> icons, List<String> titles) {
        this.context = applicationContext;
        this.icons = icons;
        this.titles = titles;
        layoutInflater = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int i) {
        return icons.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.subject_list_item, null);
        ImageView icon = view.findViewById(R.id.subject_icon_list);
        TextView title = view.findViewById(R.id.subject_name_text_view);
        icon.setImageResource(icons.get(i));
        title.setText(titles.get(i));
        return view;
    }
}