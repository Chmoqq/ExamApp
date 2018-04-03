package com.example.ivan.examapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class FragmentWebView extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.from(getContext()).inflate(R.layout.webview_fragment, container, false);
        String html = "<p align=\"center\"><strong>Hören</strong></p>\n" +
                "\n" +
                "<p align=\"center\"><strong>Teil 1</strong></p>\n" +
                "\n" +
                "<p align=\"center\"><strong>Sie hören nun sechs kurze Texte. Sie hören jeden Text zweimal.</strong><br>\n" +
                "<strong>Zu jedem Text lösen Sie eine Aufgabe.</strong><br>\n" +
                "<strong>Wählen Sie bei jeder Aufgabe die richtige Lösung, A, B oder C.</strong></p>\n" +
                "\n" +
                "<p>Was wollte Andreas mitteilen?</p>\n" +
                "\n" +
                "<p><iframe allowfullscreen=\"\" frameborder=\"0\" height=\"315\" src=\"https://www.youtube.com/embed/SiXSfyePaPM\" width=\"620\"></iframe></p>";
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
