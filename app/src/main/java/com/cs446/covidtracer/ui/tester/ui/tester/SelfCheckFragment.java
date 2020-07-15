package com.cs446.covidtracer.ui.tester.ui.tester;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cs446.covidtracer.MainActivity;
import com.cs446.covidtracer.R;

public class SelfCheckFragment extends Fragment {

    public static SelfCheckFragment newInstance() {
        return new SelfCheckFragment();
    }

    private SelfCheckViewModel selfCheckViewModel;
    private Button quitButton;
    private TextView disclaimerText;
    private Button continueButton;
    private int stage;
    private RadioGroup yesNo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        stage = 0;

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
                "If this isa  medical emergency, call 911" +
                "Disclaimer: \n" +
                "TThis is not an official COVID-19 test. \n " +
                "....\n " +
                "Test is based on information provided by the Government of Canada COVID-19 Website. \n" +
                "If you have questions, consult a health care provider <link to public health units> ");


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
                System.out.println("stage " + stage);
                if(checkedId  == R.id.yesRadio){
                    Log.d("myapp", "clicked yes");
                    if(stage != 1){
                       stage = -2;
                    } else
                        stage = -1;
                    System.out.println("stage after clicked yes " + stage);

                } else if (checkedId == R.id.noRadio){
                    System.out.println("clicked No");
                    stage = 1;
                    System.out.println("stage after clicked no " + stage);

                }
            }
        });

        ((ViewGroup) yesNo.getParent()).removeView(yesNo);

        return  root;
    }

    private void nextQuestion() {
        System.out.println("switch stage " + stage);
        if(stage == 0) {
            //((ViewGroup) disclaimerText.getParent()).removeView(disclaimerText);
            ((ViewGroup) disclaimerText.getParent()).addView(yesNo);

            disclaimerText.setText("Do you have any of these symptoms? \n " +
                    "cant breathe (RIP) \n" +
                    "chest pain");
            continueButton.setText("Continue");
        } else if (stage == 1) {
            disclaimerText.setText("Do you have any of these symptons? \n cough");

        } else if (stage == -1) {
            System.out.println("case -1 " + stage);
            disclaimerText.setText("Stop this and go to the hospital");
            ((ViewGroup) yesNo.getParent()).removeView(yesNo);
            ((ViewGroup) continueButton.getParent()).removeView(continueButton);
        } else if (stage == -2) {
            disclaimerText.setText("yes get tested");
            ((ViewGroup) yesNo.getParent()).removeView(yesNo);
            ((ViewGroup) continueButton.getParent()).removeView(continueButton);
            System.out.println("Finally ");
        }
        stage++;
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