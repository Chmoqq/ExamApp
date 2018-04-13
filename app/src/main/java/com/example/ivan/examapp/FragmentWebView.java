package com.example.ivan.examapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FragmentWebView extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StringBuilder stringBuilder = new StringBuilder();
        final View root = inflater.from(getContext()).inflate(R.layout.webview_fragment, container, false);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getContext().getAssets().open("6.html"), "UTF-8"));
            String contents;
            while ((contents = reader.readLine()) != null) {
                stringBuilder.append(contents);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String html = stringBuilder.toString();
        String mime = "text/html";
        String encoding = "utf-8";
        WebView webView = root.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(null, html, mime, encoding, null);
        return root;
    }

    @Override
    public void onStart() {
        (this.getActivity().findViewById(R.id.spinner)).setEnabled(false);
        super.onStart();
    }
}
