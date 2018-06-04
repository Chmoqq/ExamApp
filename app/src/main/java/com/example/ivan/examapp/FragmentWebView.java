package com.example.ivan.examapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import com.example.ivan.examapp.DataBase.DataBase;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentWebView extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private WebView webView;

    private FragmentTestInfo fragmentTestInfo;
    private FragmentManager fragmentManager;
    private DataBase dataBase;

    private InterstitialAd interstitialAd;

    private FrameLayout nextQuest;
    private FrameLayout prevQuest;

    private Fragment currentFragment;
    private FragmentTransaction fragmentTransaction;

    private AdView adView;

    private RadioGroup radioGroup1;
    private RadioGroup radioGroup2;
    private RadioGroup radioGroup3;
    private RadioGroup radioGroup4;

    private List<List<Integer>> userAnswersList = new ArrayList<>();
    private List<RadioGroup> radioGroups = new ArrayList<>(Arrays.asList(
            radioGroup1, radioGroup2, radioGroup3, radioGroup4));
    private List<RadioButton> buttons = new ArrayList<>();

    private TextView endTest;

    private String[] files;
    private String[] fileList;

    private int questNum;
    private int test_id;

    private String mime = "text/html";
    private String encoding = "utf-8";
    private boolean answerTYPE;

    @SuppressLint({"SetJavaScriptEnabled", "CommitTransaction"})
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = layoutSetter(inflater, container);
        if (root != null) return root;
        return null;
    }

    static final ArrayList<Integer> buttons_list = new ArrayList<>(Arrays.asList(
        R.id.tg_btn_1,
        R.id.tg_btn_2,
        R.id.tg_btn_3,
        R.id.tg_btn_4,
        R.id.tg_btn_5,
        R.id.tg_btn_2_1,
        R.id.tg_btn_2_2,
        R.id.tg_btn_2_3,
        R.id.tg_btn_2_4,
        R.id.tg_btn_2_5,
        R.id.tg_btn_3_1,
        R.id.tg_btn_3_2,
        R.id.tg_btn_3_3,
        R.id.tg_btn_3_4,
        R.id.tg_btn_3_5,
        R.id.tg_btn_4_1,
        R.id.tg_btn_4_2,
        R.id.tg_btn_4_3,
        R.id.tg_btn_4_4,
        R.id.tg_btn_4_5
    ));

    private int getButtonID(int row, int cell) {
        return buttons_list.get(row * 5 + cell);
    }

    private class ButtonPosition {
        public int row;
        public int cell;

        public ButtonPosition(int row, int cell) {
            this.row = row;
            this.cell = cell;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ButtonPosition)) {
                return false;
            }

            ButtonPosition that = (ButtonPosition) obj;
            return this.row == that.row && this.cell == that.cell;
        }
    }

    private ButtonPosition getButtonPosition(int btn_id) {
        int btn_index = buttons_list.indexOf(btn_id);
        return new ButtonPosition(Math.round(Math.max(1, btn_index) / 5), btn_index % 5);
    }

    private RadioButton getButtonView(int row, int cell) {
        return this.getView().findViewById(this.getButtonID(row, cell));
    }

    @Nullable
    private View layoutSetter(LayoutInflater inflater, @Nullable ViewGroup container) {
        dataBase = new DataBase(getContext());
        dataBase.open();
        Bundle args = getArguments();
        int subjId = args.getInt("test_id");
        String request = "SELECT answer_2 FROM answers WHERE test_id=" + subjId + " AND question_id=" + (questNum + 1);

        answerTYPE = dataBase.getNote(request) == null;

        final View root = inflater.from(getContext()).inflate(
            answerTYPE ? R.layout.webview_fragment : R.layout.webview_fragment_grid ,
            container,
            false
        );
        adView = root.findViewById(R.id.ad_webView_port);
        webView = root.findViewById(R.id.webview);
        endTest = root.findViewById(R.id.end_test_btn);
        nextQuest = root.findViewById(R.id.next_quest_btn);
        prevQuest = root.findViewById(R.id.prev_quest_btn);
        radioGroup1 = root.findViewById(R.id.radio_group1);

        if (!answerTYPE) {
            radioGroup2 = root.findViewById(R.id.radio_group2);
            radioGroup3 = root.findViewById(R.id.radio_group3);
            radioGroup4 = root.findViewById(R.id.radio_group4);
        }

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
        if (answerTYPE) {
            adMobInit();
        }
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
        if (answerTYPE) {
            radioGroup1.setOnCheckedChangeListener(this);
        } else {
            radioGroup1.setOnCheckedChangeListener(this);
            radioGroup2.setOnCheckedChangeListener(this);
            radioGroup3.setOnCheckedChangeListener(this);
            radioGroup4.setOnCheckedChangeListener(this);
        }
        nextQuest.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {
                List<Integer> answers = new ArrayList<>();
                if (questNum != fileList.length - 1) {
                    questNum += 1;
                    btnsSetCheck();
                    fragmentTransaction.detach(currentFragment);
                    fragmentTransaction.attach(currentFragment);
                    fragmentTransaction.commit();
                    if (answerTYPE) {
                        int radioButtonID = radioGroup1.getCheckedRadioButtonId();
                        View radioButton = radioGroup1.findViewById(radioButtonID);
                        int idx = radioGroup1.indexOfChild(radioButton);
                        answers.add(idx);
                        if (userAnswersList.size() >= questNum) {
                            userAnswersList.set(questNum, answers);
                        } else {
                            userAnswersList.add(answers);
                        }
                    } else {
                        for (int i = 0; i < radioGroups.size(); i++) {
                            RadioGroup radioGroup = radioGroups.get(i);
                            int radioButtonID = radioGroup.getCheckedRadioButtonId();
                            View radioButton = radioGroup.findViewById(radioButtonID);
                            int idx = radioGroup.indexOfChild(radioButton);
                            answers.add(idx);
                        }
                        userAnswersList.set(questNum, answers);
                    }
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
                questNum = Math.max(0, questNum - 1);

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
        for (RadioGroup grp : new RadioGroup[]{radioGroup1, radioGroup2, radioGroup3, radioGroup4}) {
            if (grp == null)
                continue;

            grp.clearCheck();
        }
    }

    private void adMobInit() {
        MobileAds.initialize(getActivity(), "ca-app-pub-1703600089536161~4090197835");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("1234567").build();
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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int selected_btn_id) {
        ButtonPosition selected_btn_pos = this.getButtonPosition(selected_btn_id);

        for (int cell = 0; cell < 5; cell++) {
            RadioButton btn = this.getButtonView(selected_btn_pos.row, cell);
            btn.setTextColor(getResources().getColor(
                    selected_btn_pos.cell == cell ? R.color.white : R.color.black_spec
            ));
        }
    }
}
