package com.cs446.covidtracer.ui.tester;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cs446.covidtracer.MainActivity;
import com.cs446.covidtracer.R;

import static androidx.core.content.ContextCompat.getSystemService;

public class SelfCheckFragment extends Fragment {

    public static SelfCheckFragment newInstance() {
        return new SelfCheckFragment();
    }

    private Button quitButton;
    private TextView titleText;
    private TextView bodyText;
    private Button continueButton;
    private static int stage;
    private RadioGroup yesNo;
    private EditText age;
    private static int sick;
    private TextView assessmentLink;
    private Button clipboardCopy;
    private ClipboardManager clipboardManager;
    private SelfCheck selfCheckInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        selfCheckInfo = new SelfCheck();

        SelfCheckFragment.stage = 0;
        SelfCheckFragment.sick = 0;
        View root = inflater.inflate(R.layout.self_check_fragment, container, false);

        quitButton = (Button) root.findViewById(R.id.quitButton);
        quitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                returnHome();
            }
        });

        bodyText = (TextView) root.findViewById(R.id.bodyText);
        titleText = (TextView) root.findViewById(R.id.disclaimer);

        titleText.setText("Complete this test to do a self-assessment \n'"); // todo: add links to public health providers
        bodyText.setText("If this is a  medical emergency, call 911\n\n" +
                "Disclaimer:\n" +
                "This is not an official COVID-19 test. It will help you diagnose your current situation.\n\n" +
                "The test is based on information provided by the Government of Canada COVID-19 Website.\n" +
                "If you have questions, consult a health care provider.");

        continueButton = (Button) root.findViewById(R.id.startTest);

       continueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               nextQuestion();
            }
        });


        yesNo = (RadioGroup) root.findViewById(R.id.yesNo);

        yesNo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                System.out.println("stage " + SelfCheckFragment.stage);
                System.out.println("sick " + SelfCheckFragment.sick);
                if(checkedId  == R.id.yesRadio){
                    Log.d("myapp", "clicked yes");
                    if(SelfCheckFragment.stage == 1){
                        SelfCheckFragment.stage = -1;
                        selfCheckInfo.setHasSevereSymptons(true);

                    }
                    else if (SelfCheckFragment.stage == 2) {
                        SelfCheckFragment.sick = 1;
                        selfCheckInfo.setHasCovidSymptoms(true);

                    }
                    else if (SelfCheckFragment.stage == 4) {
                        SelfCheckFragment.sick = 1;
                        selfCheckInfo.setInAtRiskGroup(true);
                    }
                    else if (SelfCheckFragment.sick == 1 && SelfCheckFragment.stage == 3)
                        SelfCheckFragment.stage = -2;
                    //else if (SelfCheckFragment.sick == 1 && SelfCheckFragment.stage == 4)
                      //  SelfCheckFragment.stage = -2;
                    else if(SelfCheckFragment.sick == 0 && SelfCheckFragment.stage == 3){
                        SelfCheckFragment.stage = 3;
                    }
                    System.out.println("stage after clicked yes " + SelfCheckFragment.stage);

                } else if (checkedId == R.id.noRadio){
                    System.out.println("clicked No");
                    if(SelfCheckFragment.stage == 0){
                        SelfCheckFragment.stage = 1;
                    }
                    else if(SelfCheckFragment.stage == 1){
                        SelfCheckFragment.sick = 0;

                    }
                    System.out.println("stage after clicked no " + SelfCheckFragment.stage);

                }

            }
        });

        clipboardCopy = (Button) root.findViewById(R.id.clipboardCopy);
        clipboardCopy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                copyToClipboard(selfCheckInfo.selfTestReport());
            }
        });

        ((ViewGroup) clipboardCopy.getParent()).removeView(clipboardCopy);

        ((ViewGroup) yesNo.getParent()).removeView(yesNo);

        age = (EditText) root.findViewById(R.id.age);
        ((ViewGroup) age.getParent()).removeView(age);

        assessmentLink = (TextView) root.findViewById(R.id.assessmentLink);
        assessmentLink.setMovementMethod(LinkMovementMethod.getInstance());
        ((ViewGroup) assessmentLink.getParent()).removeView(assessmentLink);

        return  root;
    }

    private void copyToClipboard(String copyText){
        if(!copyText.isEmpty()){
            clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

            ClipData clipData = ClipData.newPlainText("key", copyText);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getContext(), "Copied results to clipboard", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Error: Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void nextQuestion() {
        System.out.println("switch stage " + SelfCheckFragment.stage);
        if (SelfCheckFragment.stage == 0) {
            ((ViewGroup) titleText.getParent()).addView(yesNo);


            titleText.setText("Do you have any of these symptoms?\n\n");
            bodyText.setText("\u2022 Tough time breathing \n" +
                            "\u2022 Chest pain\n" +
                            "\u2022 Confusion\n" +
                            "\u2022 Losing consciousness");
            continueButton.setText("Continue");
        } else if (SelfCheckFragment.stage == 1) {
            titleText.setText("Do you have any of these symptoms? \n ");

            bodyText.setText("\u2022 Cough\n" +
                    "\u2022 Pink eye \n" +
                    "\u2022 Fever\n" +
                    "\u2022 Chills \n" +
                    "\u2022 Shortness of breath \n" +
                    "\u2022 Sore throat");
            yesNo.clearCheck();
        } else if (SelfCheckFragment.stage == 2 && SelfCheckFragment.sick ==0) {
            // ask age
            titleText.setText("What is your age?");
            bodyText.setText("You may leave this blank. Leaving it blank will affect the accuracy of the test");
            ((ViewGroup) yesNo.getParent()).addView(age);
            ((ViewGroup) yesNo.getParent()).removeView(yesNo);
        } else if(SelfCheckFragment.stage == 3 && SelfCheckFragment.sick ==0){
            titleText.setText("Are you in any of these at-risk groups?");
            bodyText.setText("\u2022 Getting treatment that compromises (weakens) your immune system\n" +
                    "\u2022 Having a condition that compromises (weakens) your immune system \n" +
                    "\u2022 Having a chronic (long-lasting) health condition\n");
            ((ViewGroup) titleText.getParent()).addView(yesNo);
            ((ViewGroup) titleText.getParent()).removeView(age);
            yesNo.clearCheck();

        } else if (SelfCheckFragment.stage == -1) {
            System.out.println("case -1 " + SelfCheckFragment.stage);
            titleText.setText("Please visit your local health care provider ASAP.");
            ((ViewGroup) yesNo.getParent()).removeView(yesNo);
            ((ViewGroup) continueButton.getParent()).removeView(continueButton);
            ((ViewGroup) quitButton.getParent()).removeView(bodyText);
            ((ViewGroup) titleText.getParent()).addView(clipboardCopy);
            selfCheckInfo.setTestCompleted(true);

        } else if (SelfCheckFragment.sick == 1) {
            //age.setInputType(0);

            titleText.setText("You are showing some symptoms of COVID-19.");
            bodyText.setText("\nWe recommend you to get an official COVID-19 Test.\n\n" +
                    "Click the link below to find an assessment center.");
            ((ViewGroup) yesNo.getParent()).addView(assessmentLink);

            ((ViewGroup) yesNo.getParent()).removeView(yesNo);
            ((ViewGroup) continueButton.getParent()).removeView(continueButton);
            ((ViewGroup) assessmentLink.getParent()).addView(clipboardCopy);
            selfCheckInfo.setTestCompleted(true);



        }  else if(SelfCheckFragment.stage == 4){
            age.setInputType(0);
            titleText.setText("You are not currently at risk for COVID-19.");
            bodyText.setText("Stay healthy by practicing social distancing, wearing a mask, and washing your hands frequently.");
            ((ViewGroup) continueButton.getParent()).removeView(continueButton);
            ((ViewGroup) titleText.getParent()).removeView(yesNo);
            ((ViewGroup) titleText.getParent()).addView(clipboardCopy);
            selfCheckInfo.setTestCompleted(true);

        }
        SelfCheckFragment.stage++;
    }

    private void returnHome() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



}