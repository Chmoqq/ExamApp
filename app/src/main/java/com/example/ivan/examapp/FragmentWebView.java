package com.example.ivan.examapp;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class FragmentWebView extends Fragment {

    private WebView webView;
    private FrameLayout nextQuest;
    private FrameLayout prevQuest;
    private Fragment currentFragment;
    private FragmentTransaction fragmentTransaction;
    private AdView adView;

    private TextView endTest;

    private String[] files;
    private String[] fileList;

    int questNum;
    int test_id;

    private String mime = "text/html";
    private String encoding = "utf-8";

    @SuppressLint({"SetJavaScriptEnabled", "CommitTransaction"})
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.from(getContext()).inflate(R.layout.webview_fragment, container, false);
        adView = root.findViewById(R.id.ad_view);
        webView = root.findViewById(R.id.webview);
        endTest = root.findViewById(R.id.end_test_btn);
        nextQuest = root.findViewById(R.id.next_quest_btn);
        prevQuest = root.findViewById(R.id.prev_quest_btn);
        currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        fragmentTransaction = getFragmentManager().beginTransaction();
        webView.getSettings().setJavaScriptEnabled(true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            adMobInit();
        }
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webViewContent(savedInstanceState);
        btnsInit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("questNum", questNum);
        outState.putInt("test_id", test_id);
    }


    private void btnsInit() {
        if (questNum != fileList.length - 1) {
            endTest.setText("Ответить");
        } else {
            endTest.setText("Завершить");
        }
        nextQuest.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {
                if (questNum != fileList.length - 1) {
                    questNum += 1;

                    fragmentTransaction.detach(currentFragment);
                    fragmentTransaction.attach(currentFragment);
                    fragmentTransaction.commit();
                } else if (questNum == fileList.length - 1) {
                    long endTime = System.currentTimeMillis();
                    endTime -= FragmentLearnTickets.getTimeStart();

                    long second = (endTime / 1000) % 60;
                    long minute = (endTime / (1000 * 60)) % 60;
                    long hour = (endTime / (1000 * 60 * 60)) % 24;

                    String time = String.format("%02d:%02d:%02d", hour, minute, second);
                    Toast toast = Toast.makeText(getContext(), time, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
        prevQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questNum != 0) {
                    questNum -= 1;
                } else {
                }
                fragmentTransaction.detach(currentFragment);
                fragmentTransaction.attach(currentFragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void adMobInit() {
        MobileAds.initialize(getActivity(), "ca-app-pub-1703600089536161~4090197835");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("").addTestDevice("1234567").build();
        adView.loadAd(adRequest);
    }

    private static String getStringFromIS(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }

    private void webViewContent(Bundle arguments) {
        try {
            Bundle args;
            if (arguments != null) {
                args = arguments;
            } else {
                args = getArguments();
            }
            if (args.containsKey("test_id"))
                test_id = args.getInt("test_id");

            if (args.containsKey("questNum"))
                questNum = args.getInt("questNum");

            fileList = getContext().getAssets().list(String.valueOf(test_id));
            files = new String[fileList.length];
            Collections.sort(Arrays.asList(fileList), new Comparator<String>() {
                public int compare(String o1, String o2) {
                    return extractInt(o1) - extractInt(o2);
                }

                int extractInt(String s) {
                    String num = s.replaceAll("\\D", "");
                    return num.isEmpty() ? 0 : Integer.parseInt(num);
                }
            });
            for (int i = 0; i < fileList.length; i++) {
                InputStream inputStream = getContext().getAssets().open(test_id + "/" + fileList[i]);
                files[i] = "<style>* { font-size: 1.1rem; } .q-number { background: #eeeeee; line-height: 27px; width: 27px;\n" +
                        "height: 27px;\n" +
                        "display: inline-block;\n" +
                        "margin-top: 5px;\n" +
                        "margin-right: 10px;\n" +
                        "text-align: center;\n" +
                        "font-weight: 700; padding: 8px;} img { width: 100%; }</style>" + getStringFromIS(inputStream);
            }
            webView.loadDataWithBaseURL("https://zno.osvita.ua", files[questNum], mime, encoding, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        (this.getActivity().findViewById(R.id.spinner)).setEnabled(false);
        super.onStart();
    }
}
