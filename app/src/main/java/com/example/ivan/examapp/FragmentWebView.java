package com.example.ivan.examapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.NumberKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ivan.examapp.DataBase.DataBase;
import com.example.ivan.examapp.SpinnerMain.SpinnerAdapter;
import com.example.ivan.examapp.SpinnerWebView.SpinnerWebViewAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

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

    private Spinner spinner;

    private RadioGroup radioGroup1;
    private RadioGroup radioGroup2;
    private RadioGroup radioGroup3;
    private RadioGroup radioGroup4;

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;

    private TextView truee;
    private TextView falsee;
    private TextView endTest;

    private List<List<String>> userAnswersList = new ArrayList<>();
    private List<Ticket> ticketList = new ArrayList<>();
    List<List<String>> rightAnswers = new ArrayList<>();
    List<int[]> questionData = new ArrayList<>();


    private Ticket ticket;

    private String[] files;
    private String[] fileList;

    private int questNum;
    private int test_id;
    private int testCounter;

    private String mime = "text/html";
    private String encoding = "utf-8";

    @SuppressLint({"SetJavaScriptEnabled", "CommitTransaction"})
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            final View root = layoutSetter(inflater, container);
            if (root != null) return root;
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private View layoutSetter(LayoutInflater inflater, @Nullable ViewGroup container) throws IOException {
        dataBase = new DataBase(getContext());
        dataBase.open();
        Bundle args = getArguments();
        if (args.containsKey("test_id"))
            test_id = args.getInt("test_id");
        if (args.containsKey("questNum"))
            questNum = args.getInt("questNum");
        if (args.containsKey("test_yourself")) {
            ticketList = dataBase.getTicket();
            ticket = ticketList.get(new Random().nextInt(ticketList.size()));
            test_id = ticket.getId();
            questNum = new Random().nextInt(ticket.getSize() - 1);
            rightAnswers.add(dataBase.getAnswer(test_id, questNum));
        } else {
            if (rightAnswers.size() == 0) {
                rightAnswers = dataBase.getAnswers(test_id);
            }
        }
        fileList = getContext().getAssets().list(String.valueOf(test_id));
        final View root = inflater.from(getContext()).inflate(setLayoutType(), container, false
        );
        root.setTag(setLayoutType());
        adView = root.findViewById(R.id.ad_webView_port);
        webView = root.findViewById(R.id.webview);
        endTest = root.findViewById(R.id.end_test_btn);
        nextQuest = root.findViewById(R.id.next_quest_btn);
        prevQuest = root.findViewById(R.id.prev_quest_btn);
        contentSetter(root);
        btnsInit(root);

        currentFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        fragmentTransaction = getFragmentManager().beginTransaction();
        webView.getSettings().setJavaScriptEnabled(true);
        return root;
    }

    private void contentSetter(View root) {
        radioGroup1 = root.findViewById(R.id.radio_group1);
        radioGroup2 = root.findViewById(R.id.radio_group2);
        radioGroup3 = root.findViewById(R.id.radio_group3);
        radioGroup4 = root.findViewById(R.id.radio_group4);

        editText1 = root.findViewById(R.id.edit_text1);
        editText2 = root.findViewById(R.id.edit_text2);
        editText3 = root.findViewById(R.id.edit_text3);

        spinner = root.findViewById(R.id.spinner_webview);

        truee = root.findViewById(R.id.textview_webview_t);
        falsee = root.findViewById(R.id.textview_webview_f);

        switch ((Integer) root.getTag()) {
            case R.layout.webview_fragment_grid:
                radioGroup2.setOnCheckedChangeListener(this);
                radioGroup3.setOnCheckedChangeListener(this);
                radioGroup4.setOnCheckedChangeListener(this);
            case R.layout.webview_fragment_true_false:
            case R.layout.webview_fragment:
                radioGroup1.setOnCheckedChangeListener(this);
                break;
        }
        SpinnerWebViewAdapter spinnerAdapter = new SpinnerWebViewAdapter(getContext());
        if (spinner != null) {
            spinner.setAdapter(spinnerAdapter);
        }
    }

    @Nullable
    private int setLayoutType() {
        if (!getArguments().containsKey("test_yourself")) {
            switch (test_id) {
                case 191:
                case 247:
                case 298:
                case 256:
                    if (rightAnswers.get(questNum).get(0).matches("[01234]") && rightAnswers.get(questNum).get(1) == null) {
                        return R.layout.webview_fragment;
                    } else if (rightAnswers.get(questNum).get(3) != null) {
                        return R.layout.webview_fragment_grid;
                    } else if (rightAnswers.get(questNum).get(1) != null && rightAnswers.get(questNum).get(2) == null) {
                        return R.layout.webview_fragment_edittext_two;
                    } else if (rightAnswers.get(questNum).get(0) == null || rightAnswers.get(questNum).get(0).equals("")) {
                        return R.layout.webview_fragment_skip;
                    } else if (!rightAnswers.get(questNum).get(0).matches("[01234]")) {
                        return R.layout.webview_fragment_edittext;
                    }
                    break;
                case 299:
                case 240:
                case 254:
                case 189:
                    if (String.valueOf(questNum).matches("re{11,23}") || String.valueOf(questNum).matches("re{34,58}")) {
                        if (rightAnswers.get(questNum).get(2) != null && rightAnswers.get(questNum).get(3) == null) {
                            return R.layout.webview_fragment_edittext_three;
                        } else if (rightAnswers.get(questNum).get(0).matches("[0123]") && rightAnswers.get(questNum).get(1) == null) {
                            return R.layout.webview_fragment;
                        } else if (rightAnswers.get(questNum).get(3) != null) {
                            return R.layout.webview_fragment_grid;
                        } else if (rightAnswers.get(questNum).get(1) != null && rightAnswers.get(questNum).get(2) == null) {
                            return R.layout.webview_fragment_edittext_two;
                        } else if (rightAnswers.get(questNum).get(0) == null || rightAnswers.get(questNum).get(0).equals("")) {
                            return R.layout.webview_fragment_skip;
                        } else if (!rightAnswers.get(questNum).get(0).matches("[01234]")) {
                            return R.layout.webview_fragment_edittext;
                        }
                    } else {
                        if (rightAnswers.get(questNum).get(2) != null && rightAnswers.get(questNum).get(3) == null) {
                            return R.layout.webview_fragment_edittext_three;
                        } else if (rightAnswers.get(questNum).get(0).matches("[0123]") && rightAnswers.get(questNum).get(1) == null) {
                            return R.layout.webview_fragment;
                        } else if (rightAnswers.get(questNum).get(3) != null) {
                            return R.layout.webview_fragment_grid;
                        } else if (rightAnswers.get(questNum).get(1) != null && rightAnswers.get(questNum).get(2) == null) {
                            return R.layout.webview_fragment_edittext_two;
                        } else if (rightAnswers.get(questNum).get(0) == null || rightAnswers.get(questNum).get(0).equals("")) {
                            return R.layout.webview_fragment_skip;
                        } else if (!rightAnswers.get(questNum).get(0).matches("[01234]")) {
                            return R.layout.webview_fragment_edittext;
                        }
                    }
                    break;
                case 250:
                case 258:
                case 200:
                    if (String.valueOf(questNum).matches("re{47,50}")) {
                        return R.layout.webview_fragment_edittext_three;
                    } else {
                        if (rightAnswers.get(questNum).get(0).matches("[01234]") && rightAnswers.get(questNum).get(1) == null) {
                            return R.layout.webview_fragment_abcd;
                        } else if (rightAnswers.get(questNum).get(3) != null) {
                            return R.layout.webview_fragment_grid;
                        } else if (rightAnswers.get(questNum).get(1) != null && rightAnswers.get(questNum).get(2) == null) {
                            return R.layout.webview_fragment_edittext_two;
                        } else if (rightAnswers.get(questNum).get(0) == null || rightAnswers.get(questNum).get(0).equals("")) {
                            return R.layout.webview_fragment_skip;
                        } else if (!rightAnswers.get(questNum).get(0).matches("[01234]")) {
                            return R.layout.webview_fragment_edittext;
                        }
                    }
                    break;
                case 304:
                    if (String.valueOf(questNum).matches("re{45,48}")) {
                        return R.layout.webview_fragment_edittext_three;
                    } else {
                        if (rightAnswers.get(questNum).get(0).matches("[01234]") && rightAnswers.get(questNum).get(1) == null) {
                            return R.layout.webview_fragment_abcd;
                        } else if (rightAnswers.get(questNum).get(3) != null) {
                            return R.layout.webview_fragment_grid;
                        } else if (rightAnswers.get(questNum).get(1) != null && rightAnswers.get(questNum).get(2) == null) {
                            return R.layout.webview_fragment_edittext_two;
                        } else if (rightAnswers.get(questNum).get(0) == null || rightAnswers.get(questNum).get(0).equals("")) {
                            return R.layout.webview_fragment_skip;
                        } else if (!rightAnswers.get(questNum).get(0).matches("[01234]")) {
                            return R.layout.webview_fragment_edittext;
                        }
                    }
                    break;
                case 302:
                    if (String.valueOf(questNum).matches("re{7,11}")) {
                        truee.setText("verdaderas");
                        falsee.setText("falsas");
                        return R.layout.webview_fragment_true_false;
                    }
                case 300:
                    if (String.valueOf(questNum).matches("re{7,11}")) {
                        truee.setText("richtig");
                        falsee.setText("falsch");
                        return R.layout.webview_fragment_true_false;
                    }
                case 303:
                    if (String.valueOf(questNum).matches("re{7,11}")) {
                        return R.layout.webview_fragment_true_false;
                    } else if (String.valueOf(questNum).matches("re{17,21}") ||
                            String.valueOf(questNum).matches("re{27,38}")) {
                        return R.layout.spinner_webview_item;
                    } else if (String.valueOf(questNum).matches("re{39,58}") ||
                            String.valueOf(questNum).matches("re{22,26}")) {
                        return R.layout.webview_fragment_abcd;
                    } else {
                        //FIXME
                    }
                case 301:
                    if (String.valueOf(questNum).matches("re{32,38}")) {
                        return R.layout.spinner_webview_item;
                    } else {
                        return R.layout.webview_fragment_abcd;
                    }

            }
            if (rightAnswers.get(0).get(2) != null && rightAnswers.get(0).get(3) == null) {
                return R.layout.webview_fragment_edittext_three;
            } else if (rightAnswers.get(0).get(0).matches("[01234]") && rightAnswers.get(0).get(1) == null) {
                return R.layout.webview_fragment_abcd;
            } else if (rightAnswers.get(0).get(3) != null) {
                return R.layout.webview_fragment_grid;
            } else if (rightAnswers.get(0).get(1) != null && rightAnswers.get(0).get(2) == null) {
                return R.layout.webview_fragment_edittext_two;
            } else if (rightAnswers.get(0).get(0) == null || rightAnswers.get(0).get(0).equals("")) {
                return R.layout.webview_fragment_skip;
            } else if (!rightAnswers.get(0).get(0).matches("[01234]")) {
                return R.layout.webview_fragment_edittext;
            }
        } else {
            switch (test_id) {
                case 191:
                case 247:
                case 298:
                case 256:
                    if (rightAnswers.get(0).get(0).matches("[01234]") && rightAnswers.get(0).get(1) == null) {
                        return R.layout.webview_fragment;
                    } else if (rightAnswers.get(0).get(3) != null) {
                        return R.layout.webview_fragment_grid;
                    } else if (rightAnswers.get(0).get(1) != null && rightAnswers.get(0).get(2) == null) {
                        return R.layout.webview_fragment_edittext_two;
                    } else if (rightAnswers.get(0).get(0) == null || rightAnswers.get(0).get(0).equals("")) {
                        return R.layout.webview_fragment_skip;
                    } else if (!rightAnswers.get(0).get(0).matches("[01234]")) {
                        return R.layout.webview_fragment_edittext;
                    }
                    break;
                case 299:
                case 240:
                case 254:
                case 189:
                    if (String.valueOf(0).matches("re{11,23}") || String.valueOf(0).matches("re{34,58}")) {
                        if (rightAnswers.get(0).get(2) != null && rightAnswers.get(0).get(3) == null) {
                            return R.layout.webview_fragment_edittext_three;
                        } else if (rightAnswers.get(0).get(0).matches("[0123]") && rightAnswers.get(0).get(1) == null) {
                            return R.layout.webview_fragment;
                        } else if (rightAnswers.get(0).get(3) != null) {
                            return R.layout.webview_fragment_grid;
                        } else if (rightAnswers.get(0).get(1) != null && rightAnswers.get(0).get(2) == null) {
                            return R.layout.webview_fragment_edittext_two;
                        } else if (rightAnswers.get(0).get(0) == null || rightAnswers.get(0).get(0).equals("")) {
                            return R.layout.webview_fragment_skip;
                        } else if (!rightAnswers.get(0).get(0).matches("[01234]")) {
                            return R.layout.webview_fragment_edittext;
                        }
                    } else {
                        if (rightAnswers.get(0).get(2) != null && rightAnswers.get(0).get(3) == null) {
                            return R.layout.webview_fragment_edittext_three;
                        } else if (rightAnswers.get(0).get(0).matches("[0123]") && rightAnswers.get(0).get(1) == null) {
                            return R.layout.webview_fragment;
                        } else if (rightAnswers.get(0).get(3) != null) {
                            return R.layout.webview_fragment_grid;
                        } else if (rightAnswers.get(0).get(1) != null && rightAnswers.get(0).get(2) == null) {
                            return R.layout.webview_fragment_edittext_two;
                        } else if (rightAnswers.get(0).get(0) == null || rightAnswers.get(0).get(0).equals("")) {
                            return R.layout.webview_fragment_skip;
                        } else if (!rightAnswers.get(0).get(0).matches("[01234]")) {
                            return R.layout.webview_fragment_edittext;
                        }
                    }
                    break;
                case 250:
                case 258:
                case 200:
                    if (String.valueOf(0).matches("re{47,50}")) {
                        return R.layout.webview_fragment_edittext_three;
                    } else {
                        if (rightAnswers.get(0).get(0).matches("[01234]") && rightAnswers.get(0).get(1) == null) {
                            return R.layout.webview_fragment_abcd;
                        } else if (rightAnswers.get(0).get(3) != null) {
                            return R.layout.webview_fragment_grid;
                        } else if (rightAnswers.get(0).get(1) != null && rightAnswers.get(0).get(2) == null) {
                            return R.layout.webview_fragment_edittext_two;
                        } else if (rightAnswers.get(0).get(0) == null || rightAnswers.get(0).get(0).equals("")) {
                            return R.layout.webview_fragment_skip;
                        } else if (!rightAnswers.get(0).get(0).matches("[01234]")) {
                            return R.layout.webview_fragment_edittext;
                        }
                    }
                    break;
                case 304:
                    if (String.valueOf(0).matches("re{45,48}")) {
                        return R.layout.webview_fragment_edittext_three;
                    } else {
                        if (rightAnswers.get(0).get(0).matches("[01234]") && rightAnswers.get(0).get(1) == null) {
                            return R.layout.webview_fragment_abcd;
                        } else if (rightAnswers.get(0).get(3) != null) {
                            return R.layout.webview_fragment_grid;
                        } else if (rightAnswers.get(0).get(1) != null && rightAnswers.get(0).get(2) == null) {
                            return R.layout.webview_fragment_edittext_two;
                        } else if (rightAnswers.get(0).get(0) == null || rightAnswers.get(0).get(0).equals("")) {
                            return R.layout.webview_fragment_skip;
                        } else if (!rightAnswers.get(0).get(0).matches("[01234]")) {
                            return R.layout.webview_fragment_edittext;
                        }
                    }
                    break;
                case 302:
                    if (String.valueOf(0).matches("re{7,11}")) {
                        truee.setText("verdaderas");
                        falsee.setText("falsas");
                        return R.layout.webview_fragment_true_false;
                    }
                case 300:
                    if (String.valueOf(0).matches("re{7,11}")) {
                        truee.setText("richtig");
                        falsee.setText("falsch");
                        return R.layout.webview_fragment_true_false;
                    }
                case 303:
                    if (String.valueOf(0).matches("re{7,11}")) {
                        return R.layout.webview_fragment_true_false;
                    } else if (String.valueOf(0).matches("re{17,21}") ||
                            String.valueOf(0).matches("re{27,38}")) {
                        return R.layout.spinner_webview_item;
                    } else if (String.valueOf(0).matches("re{39,58}") ||
                            String.valueOf(0).matches("re{22,26}")) {
                        return R.layout.webview_fragment_abcd;
                    } else {
                        //FIXME
                    }
                case 301:
                    if (String.valueOf(0).matches("re{32,38}")) {
                        return R.layout.spinner_webview_item;
                    } else {
                        return R.layout.webview_fragment_abcd;
                    }

            }
            if (rightAnswers.get(0).get(2) != null && rightAnswers.get(0).get(3) == null) {
                return R.layout.webview_fragment_edittext_three;
            } else if (rightAnswers.get(0).get(0).matches("[01234]") && rightAnswers.get(0).get(1) == null) {
                return R.layout.webview_fragment_abcd;
            } else if (rightAnswers.get(0).get(3) != null) {
                return R.layout.webview_fragment_grid;
            } else if (rightAnswers.get(0).get(1) != null && rightAnswers.get(0).get(2) == null) {
                return R.layout.webview_fragment_edittext_two;
            } else if (rightAnswers.get(0).get(0) == null || rightAnswers.get(0).get(0).equals("")) {
                return R.layout.webview_fragment_skip;
            } else if (!rightAnswers.get(0).get(0).matches("[01234]")) {
                return R.layout.webview_fragment_edittext;
            }
        }

        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dataBase.close();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webViewContent();
        pageAdSetter();
        if (!getView().getTag().equals(R.layout.webview_fragment_grid)) {
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


    private void btnsInit(final View root) {
        if (!getArguments().containsKey("test_yourself")) {
            if (questNum == fileList.length - 1) {
                endTest.setText("Завершить");
            } else if (rightAnswers.get(questNum).get(0) == null || rightAnswers.get(questNum).get(0).equals("")) {
                endTest.setText("Пропустить");
            } else {
                endTest.setText("Ответить");
            }
            nextQuest.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onClick(View view) {
                    if (getArguments().containsKey("test_yourself")) {
                        questionData.add(new int[]{test_id, questNum});
                        if (testCounter != 20) {
                            testCounter += 1;
                            fragmentTransaction.detach(currentFragment);
                            fragmentTransaction.attach(currentFragment);
                            fragmentTransaction.commit();
                            userAnswersList.add(userAnswerGetter(root));
                        } else {
                            userAnswersList.add(userAnswerGetter(root));
                            interstitialAd.show();
                            fragmentTestInfo.setArguments(answerControl());
                        }
                    } else {
                        if (questNum != fileList.length - 1) {
                            questNum += 1;
                            fragmentTransaction.detach(currentFragment);
                            fragmentTransaction.attach(currentFragment);
                            fragmentTransaction.commit();

                            if (userAnswersList.size() > questNum) {
                                userAnswersList.set(questNum, userAnswerGetter(root));
                            } else {
                                userAnswersList.add(userAnswerGetter(root));
                            }
                        } else if (questNum == fileList.length - 1) {
                            userAnswersList.add(userAnswerGetter(root));
                            interstitialAd.show();
                            fragmentTestInfo.setArguments(answerControl());
                        }
                        btnsSetCheck();
                    }
                }

            });
        } else {
            if (testCounter == 20) {
                endTest.setText("Завершить");
            } else if (rightAnswers.get(0).get(0) == null || rightAnswers.get(0).get(0).equals("")) {
                endTest.setText("Пропустить");
            } else {
                endTest.setText("Ответить");
            }
            nextQuest.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onClick(View view) {
                    if (getArguments().containsKey("test_yourself")) {
                        questionData.add(new int[]{test_id, questNum});
                        if (testCounter != 20) {
                            testCounter += 1;
                            fragmentTransaction.detach(currentFragment);
                            fragmentTransaction.attach(currentFragment);
                            fragmentTransaction.commit();
                            userAnswersList.add(userAnswerGetter(root));
                        } else {
                            userAnswersList.add(userAnswerGetter(root));
                            interstitialAd.show();
                            fragmentTestInfo.setArguments(answerControl());
                        }
                    } else {
                        if (questNum != fileList.length - 1) {
                            questNum += 1;
                            fragmentTransaction.detach(currentFragment);
                            fragmentTransaction.attach(currentFragment);
                            fragmentTransaction.commit();

                            if (userAnswersList.size() > questNum) {
                                userAnswersList.set(questNum, userAnswerGetter(root));
                            } else {
                                userAnswersList.add(userAnswerGetter(root));
                            }
                        } else if (questNum == fileList.length - 1) {
                            userAnswersList.add(userAnswerGetter(root));
                            interstitialAd.show();
                            fragmentTestInfo.setArguments(answerControl());
                        }
                        btnsSetCheck();
                    }
                }

            });
        }
        if (questNum != 0) {
            if (getArguments().containsKey("test_yourself")) {
            } else {
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
        }
    }

    private List<String> userAnswerGetter(View root) {
        List<String> answerValues = new ArrayList<String>() {{
            add(null);
            add(null);
            add(null);
            add(null);
        }};
        RadioGroup[] radioGroups = new RadioGroup[]{radioGroup1, radioGroup2, radioGroup3, radioGroup4};
        if (root.getTag().equals(R.layout.webview_fragment) || root.getTag().equals(R.layout.webview_fragment_abcd)) {
            for (int i = 0; i < 1; i++) {
                RadioGroup radioGroup = radioGroups[i];
                RadioButton radioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                if (radioGroup.indexOfChild(radioButton) == -1)
                    continue;

                answerValues.set(i, String.valueOf(radioGroup.indexOfChild(radioButton)));
            }
        } else if (root.getTag().equals(R.layout.webview_fragment_grid)) {
            for (int i = 0; i < radioGroups.length; i++) {
                RadioGroup radioGroup = radioGroups[i];
                RadioButton radioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                if (radioGroup.indexOfChild(radioButton) == -1)
                    continue;

                answerValues.set(i, String.valueOf(radioGroup.indexOfChild(radioButton)));
            }
        } else if (root.getTag().equals(R.layout.webview_fragment_edittext_three)) {
            answerValues.set(0, editText1.getText().toString());
            answerValues.set(1, editText2.getText().toString());
            answerValues.set(2, editText3.getText().toString());
        } else if (root.getTag().equals(R.layout.webview_fragment_edittext_two)) {
            answerValues.set(0, editText1.getText().toString());
            answerValues.set(1, editText2.getText().toString());
        } else if (root.getTag().equals(R.layout.webview_fragment_skip)) {
            answerValues.set(0, null);
        } else if (root.getTag().equals(R.layout.webview_fragment_edittext)) {
            answerValues.set(1, editText1.getText().toString());
        } else if (root.getTag().equals(R.layout.webview_fragment_true_false)) {
            for (int i = 0; i < 1; i++) {
                RadioGroup radioGroup = radioGroups[i];
                RadioButton radioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                if (radioGroup.indexOfChild(radioButton) == -1)
                    continue;

                answerValues.set(i, String.valueOf(radioGroup.indexOfChild(radioButton)));
            }
        } else if (root.getTag().equals(R.layout.webview_fragment_spinner)) {
            answerValues.set(0, String.valueOf(spinner.getSelectedItemPosition()));
        }
        return answerValues;
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

    private Bundle answerControl() {
        long endTime = System.currentTimeMillis();
        endTime -= FragmentLearnTickets.getTimeStart();

        long second = (endTime / 1000) % 60;
        long minute = (endTime / (1000 * 60)) % 60;
        long hour = (endTime / (1000 * 60 * 60)) % 24;
        int rightAns = 0;

        Bundle args = new Bundle();
        args.putString("hours", String.valueOf(hour));
        args.putString("mins", String.valueOf(minute));
        args.putString("secs", String.valueOf(second));
        for (int i = 0; i < rightAnswers.size(); i++) {
            List<String> userQuestAns = userAnswersList.get(i);
            List<String> rightQuestAns = rightAnswers.get(i);

            boolean is_wrong = false;
            for (int b = 0; b < 4; b++) {
                if (userQuestAns.get(b) == null) {
                    userQuestAns.set(b, "");
                }
                if (rightQuestAns.get(0).equals("")) {
                    userQuestAns.set(0, null);
                    is_wrong = true;
                    break;
                }
                if (rightQuestAns.get(b) == null) {
                    rightQuestAns.set(b, "");
                }
                if (!userQuestAns.get(b).equals(rightQuestAns.get(b))) {
                    is_wrong = true;
                    for (int c = 0; c < 4; c++) {
                        if (userQuestAns.get(c) != null) {
                            if (userQuestAns.get(c).equals("")) {
                                userQuestAns.set(c, null);
                            }
                        }
                        if (rightQuestAns.get(c) != null) {
                            if (rightQuestAns.get(c).equals("")) {
                                rightQuestAns.set(c, null);
                            }
                        }
                    }
                    break;
                }
                for (int c = 0; c < 4; c++) {
                    if (userQuestAns.get(c) != null) {
                        if (userQuestAns.get(c).equals("")) {
                            userQuestAns.set(c, null);
                        }
                    }
                    if (rightQuestAns.get(c) != null) {
                        if (rightQuestAns.get(c).equals("")) {
                            rightQuestAns.set(c, null);
                        }
                    }
                }
            }

            if (!is_wrong)
                rightAns += 1;
            dataBase.userAnswerInsert(test_id, i + 1, userQuestAns);
        }
        if (!getArguments().containsKey("test_yourself")) {
            args.putString("total", String.valueOf(fileList.length));
        } else {
            args.putString("total", String.valueOf(20));
        }
        args.putString("right", String.valueOf(rightAns));
        return args;
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
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private void webViewContent() {
        try {
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
            webView.getSettings().setBuiltInZoomControls(true);
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
