package com.cs446.covidtracer.ui.information;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs446.covidtracer.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

public class InformationFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_information, container, false);

        TextView d1 = (TextView) root.findViewById(R.id.whoAdvice);
        d1.setMovementMethod(LinkMovementMethod.getInstance());

        TextView d2 = (TextView) root.findViewById(R.id.whoAdvice2);
        d2.setMovementMethod(LinkMovementMethod.getInstance());

        TextView d3 = (TextView) root.findViewById(R.id.whoAdvice3);
        d3.setMovementMethod(LinkMovementMethod.getInstance());

        TextView d4 = (TextView) root.findViewById(R.id.whoAdvice4);
        d4.setMovementMethod(LinkMovementMethod.getInstance());


        TextView d6 = (TextView) root.findViewById(R.id.whoAdvice6);
        d6.setMovementMethod(LinkMovementMethod.getInstance());

        TextView d7 = (TextView) root.findViewById(R.id.whoAdvice7);
        d7.setMovementMethod(LinkMovementMethod.getInstance());

        TextView d8 = (TextView) root.findViewById(R.id.whoAdvice8);
        d8.setMovementMethod(LinkMovementMethod.getInstance());

        TextView d9 = (TextView) root.findViewById(R.id.whoAdvice9);
        d9.setMovementMethod(LinkMovementMethod.getInstance());

        TextView d10 = (TextView) root.findViewById(R.id.whoAdvice10);
        d10.setMovementMethod(LinkMovementMethod.getInstance());

        return root;
    }
}
