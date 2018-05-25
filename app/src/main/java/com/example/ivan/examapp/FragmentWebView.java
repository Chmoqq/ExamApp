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

import com.example.ivan.examapp.DataBase.NoteDataDelegate;
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

public class FragmentWebView extends Fragment {

    private WebView webView;

    private FragmentTestInfo fragmentTestInfo;
    private FragmentManager fragmentManager;
    private NoteDataDelegate noteDataDelegate;

    private InterstitialAd interstitialAd;

    private FrameLayout nextQuest;
    private FrameLayout prevQuest;

    private Fragment currentFragment;
    private FragmentTransaction fragmentTransaction;

    private AdView adView;

    private List<List<Integer>> userAnswersList = new ArrayList<>();
    private List<RadioGroup> radioGroups = new ArrayList<>();

    private RadioButton button1_1;
    private RadioButton button1_2;
    private RadioButton button1_3;
    private RadioButton button1_4;
    private RadioButton button1_5;
    private RadioGroup radioGroup1;

    private RadioButton button2_2;
    private RadioButton button2_1;
    private RadioButton button2_3;
    private RadioButton button2_4;
    private RadioButton button2_5;
    private RadioGroup radioGroup2;

    private RadioButton button3_1;
    private RadioButton button3_2;
    private RadioButton button3_3;
    private RadioButton button3_4;
    private RadioButton button3_5;
    private RadioGroup radioGroup3;

    private RadioButton button4_1;
    private RadioButton button4_2;
    private RadioButton button4_3;
    private RadioButton button4_4;
    private RadioButton button4_5;
    private RadioGroup radioGroup4;

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

    @Nullable
    private View layoutSetter(LayoutInflater inflater, @Nullable ViewGroup container) {
        noteDataDelegate = new NoteDataDelegate(getContext());
        noteDataDelegate.open();
        Bundle args = getArguments();
        int subjId = args.getInt("test_id");
        String request = "SELECT answer_2 FROM answers WHERE test_id=" + subjId + " AND question_id=" + (questNum + 1);
        if (noteDataDelegate.getNote(request) == null) {
            answerTYPE = true;
            final View root = inflater.from(getContext()).inflate(R.layout.webview_fragment, container, false);
            adView = root.findViewById(R.id.ad_webView_port);
            webView = root.findViewById(R.id.webview);
            endTest = root.findViewById(R.id.end_test_btn);
            nextQuest = root.findViewById(R.id.next_quest_btn);
            prevQuest = root.findViewById(R.id.prev_quest_btn);
            button1_1 = root.findViewById(R.id.tg_btn_1);
            button1_2 = root.findViewById(R.id.tg_btn_2);
            button1_3 = root.findViewById(R.id.tg_btn_3);
            button1_4 = root.findViewById(R.id.tg_btn_4);
            button1_5 = root.findViewById(R.id.tg_btn_5);
            radioGroup1 = root.findViewById(R.id.radio_group);
            currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
            fragmentTransaction = getFragmentManager().beginTransaction();
            webView.getSettings().setJavaScriptEnabled(true);
            return root;
        } else {
            answerTYPE = false;
            final View root = inflater.from(getContext()).inflate(R.layout.webview_fragment_grid, container, false);
            adView = root.findViewById(R.id.ad_webView_port);
            webView = root.findViewById(R.id.webview);
            endTest = root.findViewById(R.id.end_test_btn);
            nextQuest = root.findViewById(R.id.next_quest_btn);
            prevQuest = root.findViewById(R.id.prev_quest_btn);
            button1_1 = root.findViewById(R.id.tg_btn_1);
            button1_2 = root.findViewById(R.id.tg_btn_2);
            button1_3 = root.findViewById(R.id.tg_btn_3);
            button1_4 = root.findViewById(R.id.tg_btn_4);
            button1_5 = root.findViewById(R.id.tg_btn_5);
            radioGroup1 = root.findViewById(R.id.radio_group1);

            button2_1 = root.findViewById(R.id.tg_btn_2_1);
            button2_2 = root.findViewById(R.id.tg_btn_2_2);
            button2_3 = root.findViewById(R.id.tg_btn_2_3);
            button2_4 = root.findViewById(R.id.tg_btn_2_4);
            button2_5 = root.findViewById(R.id.tg_btn_2_5);
            radioGroup2 = root.findViewById(R.id.radio_group2);

            button3_1 = root.findViewById(R.id.tg_btn_3_1);
            button3_2 = root.findViewById(R.id.tg_btn_3_2);
            button3_3 = root.findViewById(R.id.tg_btn_3_3);
            button3_4 = root.findViewById(R.id.tg_btn_3_4);
            button3_5 = root.findViewById(R.id.tg_btn_3_5);
            radioGroup3 = root.findViewById(R.id.radio_group3);

            button4_1 = root.findViewById(R.id.tg_btn_4_1);
            button4_2 = root.findViewById(R.id.tg_btn_4_2);
            button4_3 = root.findViewById(R.id.tg_btn_4_3);
            button4_4 = root.findViewById(R.id.tg_btn_4_4);
            button4_5 = root.findViewById(R.id.tg_btn_4_5);
            radioGroup4 = root.findViewById(R.id.radio_group4);
            radioGroups.add(radioGroup1);
            radioGroups.add(radioGroup2);
            radioGroups.add(radioGroup3);
            radioGroups.add(radioGroup4);
            currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
            fragmentTransaction = getFragmentManager().beginTransaction();
            webView.getSettings().setJavaScriptEnabled(true);
            return root;
        }
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
            radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    button1_1.setTextColor(getResources().getColor(R.color.black_spec));
                    button1_2.setTextColor(getResources().getColor(R.color.black_spec));
                    button1_3.setTextColor(getResources().getColor(R.color.black_spec));
                    button1_4.setTextColor(getResources().getColor(R.color.black_spec));
                    button1_5.setTextColor(getResources().getColor(R.color.black_spec));
                    rBtnChecked(i);
                }
            });
        } else {
            radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    button1_1.setTextColor(getResources().getColor(R.color.black_spec));
                    button1_2.setTextColor(getResources().getColor(R.color.black_spec));
                    button1_3.setTextColor(getResources().getColor(R.color.black_spec));
                    button1_4.setTextColor(getResources().getColor(R.color.black_spec));
                    button1_5.setTextColor(getResources().getColor(R.color.black_spec));
                    rBtnChecked(i);
                }
            });
            radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    button2_1.setTextColor(getResources().getColor(R.color.black_spec));
                    button2_2.setTextColor(getResources().getColor(R.color.black_spec));
                    button2_3.setTextColor(getResources().getColor(R.color.black_spec));
                    button2_4.setTextColor(getResources().getColor(R.color.black_spec));
                    button2_5.setTextColor(getResources().getColor(R.color.black_spec));
                    rBtnChecked(i);
                }
            });
            radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    button3_1.setTextColor(getResources().getColor(R.color.black_spec));
                    button3_2.setTextColor(getResources().getColor(R.color.black_spec));
                    button3_3.setTextColor(getResources().getColor(R.color.black_spec));
                    button3_4.setTextColor(getResources().getColor(R.color.black_spec));
                    button3_5.setTextColor(getResources().getColor(R.color.black_spec));
                    rBtnChecked(i);
                }
            });
            radioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    button4_1.setTextColor(getResources().getColor(R.color.black_spec));
                    button4_2.setTextColor(getResources().getColor(R.color.black_spec));
                    button4_3.setTextColor(getResources().getColor(R.color.black_spec));
                    button4_4.setTextColor(getResources().getColor(R.color.black_spec));
                    button4_5.setTextColor(getResources().getColor(R.color.black_spec));
                    rBtnChecked(i);
                }
            });
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
                        userAnswersList.set(questNum, answers);
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
        if (answerTYPE) {
            button1_1.setChecked(false);
            button1_2.setChecked(false);
            button1_3.setChecked(false);
            button1_4.setChecked(false);
            button1_5.setChecked(false);
        } else {
            button1_1.setChecked(false);
            button1_2.setChecked(false);
            button1_3.setChecked(false);
            button1_4.setChecked(false);
            button1_5.setChecked(false);

            button2_1.setChecked(false);
            button2_2.setChecked(false);
            button2_3.setChecked(false);
            button2_4.setChecked(false);
            button2_5.setChecked(false);

            button3_1.setChecked(false);
            button3_2.setChecked(false);
            button3_3.setChecked(false);
            button3_4.setChecked(false);
            button3_5.setChecked(false);

            button4_1.setChecked(false);
            button4_2.setChecked(false);
            button4_3.setChecked(false);
            button4_4.setChecked(false);
            button4_5.setChecked(false);
        }
    }

    private void rBtnChecked(int i) {
        switch (i) {
            case R.id.tg_btn_1:
                button1_1.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_2:
                button1_2.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_3:
                button1_3.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_4:
                button1_4.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_5:
                button1_5.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_2_1:
                button2_1.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_2_2:
                button2_2.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_2_3:
                button2_3.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_2_4:
                button2_4.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_2_5:
                button2_5.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_3_1:
                button3_1.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_3_2:
                button3_2.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_3_3:
                button3_3.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_3_4:
                button3_4.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_3_5:
                button3_5.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_4_1:
                button4_1.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_4_2:
                button4_2.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_4_3:
                button4_3.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_4_4:
                button4_4.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tg_btn_4_5:
                button4_5.setTextColor(getResources().getColor(R.color.white));
                break;
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
}
