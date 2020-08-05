package com.cs446.covidtracer.ui.tester;

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

import com.cs446.covidtracer.MainActivity;
import com.cs446.covidtracer.R;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

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
                    }
                    else if (SelfCheckFragment.stage == 2)
                        SelfCheckFragment.sick = 1;
                    else if (SelfCheckFragment.sick == 1 && SelfCheckFragment.stage == 3)
                        SelfCheckFragment.stage = -2;
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

        ((ViewGroup) yesNo.getParent()).removeView(yesNo);

        age = (EditText) root.findViewById(R.id.age);
        ((ViewGroup) age.getParent()).removeView(age);

        assessmentLink = (TextView) root.findViewById(R.id.assessmentLink);
        assessmentLink.setMovementMethod(LinkMovementMethod.getInstance());
        ((ViewGroup) assessmentLink.getParent()).removeView(assessmentLink);

        return  root;
    }

    private void nextQuestion() {
        System.out.println("switch stage " + SelfCheckFragment.stage);
        if (SelfCheckFragment.stage == 0) {
            ((ViewGroup) titleText.getParent()).addView(yesNo);


            titleText.setText("Do you have any of these symptoms?\n\n");
            bodyText.setText("- Tough time breathing \n" +
                            "- Chest pain\n" +
                            "- Confusion\n" +
                            "- Losing consciousness");
            continueButton.setText("Continue");
        } else if (SelfCheckFragment.stage == 1) {
            titleText.setText("Do you have any of these symptoms? \n ");

            bodyText.setText("- Cough\n" +
                    "- Pink eye \n" +
                    "- Fever\n" +
                    "- Chills \n" +
                    "- Shortness of breath \n" +
                    "- Sore throat");
            yesNo.clearCheck();
        } else if (SelfCheckFragment.stage == 2 && SelfCheckFragment.sick ==0){
            // ask age
            titleText.setText("What is your age?");
            bodyText.setText("You may leave this blank. Leaving it blank will affect the accuracy of the test" );
            ((ViewGroup) yesNo.getParent()).addView(age);
            ((ViewGroup) yesNo.getParent()).removeView(yesNo);

        } else if (SelfCheckFragment.stage == -1) {
            System.out.println("case -1 " + SelfCheckFragment.stage);
            titleText.setText("Please visit your local health care provider ASAP.");
            ((ViewGroup) yesNo.getParent()).removeView(yesNo);
            ((ViewGroup) continueButton.getParent()).removeView(continueButton);
            ((ViewGroup) quitButton.getParent()).removeView(bodyText);
        } else if (SelfCheckFragment.sick == 1) {
            age.setInputType(0);

            titleText.setText("You are showing some symptoms of COVID-19.");
            bodyText.setText("\nWe recommend you to get an official COVID-19 Test.\n\n" +
                    "Click the link below to find an assessment center.");
            ((ViewGroup) yesNo.getParent()).addView(assessmentLink);




            ((ViewGroup) yesNo.getParent()).removeView(yesNo);
            ((ViewGroup) continueButton.getParent()).removeView(continueButton);


        }  else if(SelfCheckFragment.stage == 3){
            age.setInputType(0);
            titleText.setText("You are not at risk for COVID-19.");
            bodyText.setText("Stay healthy by practicing social distancing, wearing a mask, and washing your hands frequently.");
            ((ViewGroup) continueButton.getParent()).removeView(continueButton);
            ((ViewGroup) age.getParent()).removeView(age);

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