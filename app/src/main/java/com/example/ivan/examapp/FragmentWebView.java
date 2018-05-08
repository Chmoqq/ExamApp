package com.example.ivan.examapp;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
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

    private FragmentTestInfo fragmentTestInfo;
    private FragmentManager fragmentManager;

    private InterstitialAd interstitialAd;

    private FrameLayout nextQuest;
    private FrameLayout prevQuest;

    private Fragment currentFragment;
    private FragmentTransaction fragmentTransaction;

    private AdView adViewPort;

    private RadioButton button1;
    private RadioButton button2;
    private RadioButton button3;
    private RadioButton button4;
    private RadioGroup radioGroup;

    private TextView endTest;

    private String[] files;
    private String[] fileList;

    private int questNum;
    private int test_id;

    private String mime = "text/html";
    private String encoding = "utf-8";

    @SuppressLint({"SetJavaScriptEnabled", "CommitTransaction"})
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.from(getContext()).inflate(R.layout.webview_fragment, container, false);
        adViewPort = root.findViewById(R.id.ad_webView_port);
        webView = root.findViewById(R.id.webview);
        endTest = root.findViewById(R.id.end_test_btn);
        nextQuest = root.findViewById(R.id.next_quest_btn);
        prevQuest = root.findViewById(R.id.prev_quest_btn);
        button1 = root.findViewById(R.id.tg_btn_1);
        button2 = root.findViewById(R.id.tg_btn_2);
        button3 = root.findViewById(R.id.tg_btn_3);
        button4 = root.findViewById(R.id.tg_btn_4);
        radioGroup = root.findViewById(R.id.radio_group);
        currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        fragmentTransaction = getFragmentManager().beginTransaction();
        webView.getSettings().setJavaScriptEnabled(true);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webViewContent(savedInstanceState);
        btnsInit();
        pageAdSetter();
        adMobInit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void btnsInit() {
        if (questNum != fileList.length - 1) {
            endTest.setText("Ответить");
        } else {
            endTest.setText("Завершить");
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                button1.setTextColor(getResources().getColor(R.color.black_spec));
                button2.setTextColor(getResources().getColor(R.color.black_spec));
                button3.setTextColor(getResources().getColor(R.color.black_spec));
                button4.setTextColor(getResources().getColor(R.color.black_spec));
                rBtnChecked(i);
            }
        });
        nextQuest.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {
                if (questNum != fileList.length - 1) {
                    questNum += 1;
                    btnsSetCheck();
                    fragmentTransaction.detach(currentFragment);
                    fragmentTransaction.attach(currentFragment);
                    fragmentTransaction.commit();
                } else if (questNum == fileList.length - 1) {
                    long endTime = System.currentTimeMillis();
                    endTime -= FragmentLearnTickets.getTimeStart();

                    long second = (endTime / 1000) % 60;
                    long minute = (endTime / (1000 * 60)) % 60;
                    long hour = (endTime / (1000 * 60 * 60)) % 24;

                    Bundle args = new Bundle();
                    args.putString("total", String.valueOf(fileList.length - 1));
                    args.putString("right", "0");
                    args.putString("hours", String.valueOf(hour));
                    args.putString("mins", String.valueOf(minute));
                    args.putString("secs", String.valueOf(second));
                    fragmentTestInfo.setArguments(args);
                    interstitialAd.show();
                }

            }
        });
        prevQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questNum != 0) {
                    questNum -= 1;
                }
                fragmentTransaction.detach(currentFragment);
                fragmentTransaction.attach(currentFragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void pageAdSetter() {
        fragmentTestInfo = new FragmentTestInfo();
        fragmentManager = getActivity().getSupportFragmentManager();
        interstitialAd = new InterstitialAd(getActivity());
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentTestInfo).commit();
            }
        });
    }

    private void btnsSetCheck() {
        button1.setChecked(false);
        button2.setChecked(false);
        button3.setChecked(false);
        button4.setChecked(false);
    }

    private void rBtnChecked(int i) {
        switch (i) {
            case R.id.tg_btn_1:
                button1.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_2:
                button2.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_3:
                button3.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_4:
                button4.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void adMobInit() {
        MobileAds.initialize(getActivity(), "ca-app-pub-1703600089536161~4090197835");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("1234567").build();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            adViewPort.loadAd(adRequest);
        }
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
