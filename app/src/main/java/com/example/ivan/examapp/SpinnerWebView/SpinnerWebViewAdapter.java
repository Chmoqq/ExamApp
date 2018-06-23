package com.example.ivan.examapp.SpinnerWebView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivan.examapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpinnerWebViewAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    String[] lettersMass = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private List<String> letters = Arrays.asList(lettersMass);

    public SpinnerWebViewAdapter(Context context) {
        this.layoutInflater = (LayoutInflater.from(context));
    }

    @Override

    public int getCount() {
        return 8;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.spinner_webview_item, null);
        TextView letter = view.findViewById(R.id.webview_spinner_textview);
        letter.setText(letters.get(i));
        return view;
    }
}
