package com.cs446.covidtracer.ui.tester.ui.tester;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cs446.covidtracer.MainActivity;
import com.cs446.covidtracer.R;

import static androidx.core.content.ContextCompat.getSystemService;

public class SelfCheckFragment extends Fragment {

    public static SelfCheckFragment newInstance() {
        return new SelfCheckFragment();
    }

    private SelfCheckViewModel selfCheckViewModel;
    private Button quitButton;
    private TextView disclaimerText;
    private Button continueButton;
    private static int stage;
    private RadioGroup yesNo;
    private EditText age;
    private static int sick;

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

        disclaimerText = (TextView) root.findViewById(R.id.disclaimer);
        disclaimerText.setText("Complete this test and you will get a recommendation \n'" +
                "If this is a  medical emergency, call 911" +
                "Disclaimer: \n" +
                "This is not an official COVID-19 test. \n " +
                "This test will help you diagnose your current situation.\n " +
                "The test is based on information provided by the Government of Canada COVID-19 Website. \n" +
                "If you have questions, consult a health care provider. "); // todo: add links to public health providers


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


        return  root;
    }

    private void nextQuestion() {
        System.out.println("switch stage " + SelfCheckFragment.stage);
        if (SelfCheckFragment.stage == 0) {
            ((ViewGroup) disclaimerText.getParent()).addView(yesNo);

            disclaimerText.setText("Do you have any of these symptoms? \n " +
                    "- Tough time breathing \n" +
                    "- Chest pain\n" +
                    "- Confusion\n" +
                    "- Losing consciousness");
            continueButton.setText("Continue");
        } else if (SelfCheckFragment.stage == 1) {
            disclaimerText.setText("Do you have any of these symptoms? \n " +
                    "- Cough\n" +
                    "- Pink eye \n" +
                    "- Fever\n" +
                    "- Chills \n" +
                    "- Shortness of breath \n" +
                    "- Sore throat");
            yesNo.clearCheck();
        } else if (SelfCheckFragment.stage == 2 && SelfCheckFragment.sick ==0){
            // ask age
            disclaimerText.setText("What is your age? \n" +
                    "You may leave this blank.\n" +
                    "Leaving it blank will affect the accuracy of the test" );
            ((ViewGroup) yesNo.getParent()).addView(age);
            ((ViewGroup) yesNo.getParent()).removeView(yesNo);

        } else if (SelfCheckFragment.stage == -1) {
            System.out.println("case -1 " + SelfCheckFragment.stage);
            disclaimerText.setText("Please visit your local health care provider ASAP.");
            ((ViewGroup) yesNo.getParent()).removeView(yesNo);
            ((ViewGroup) continueButton.getParent()).removeView(continueButton);
        } else if (SelfCheckFragment.sick == 1) {
            age.setInputType(0);

            disclaimerText.setText("You are showing some symptoms of COVID-19.\n\n" +
                    "We recommend you should get an official COVID-19 Test.\n" +
                    "Click here to find out how you can get tested");//todo
            ((ViewGroup) yesNo.getParent()).removeView(yesNo);
            ((ViewGroup) continueButton.getParent()).removeView(continueButton);
        }  else if(SelfCheckFragment.stage == 3){
            age.setInputType(0);
            disclaimerText.setText("You are in good health. \n\n" +
                    "Stay healthy by practicing social distancing, wearing a mask, and washing your hands frequently");
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
        selfCheckViewModel = ViewModelProviders.of(this).get(SelfCheckViewModel.class);
        // TODO: Use the ViewModel
    }



}