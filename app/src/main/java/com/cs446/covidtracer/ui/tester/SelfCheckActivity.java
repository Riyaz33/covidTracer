package com.cs446.covidtracer.ui.tester;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.cs446.covidtracer.ui.tester.ui.tester.SelfCheckFragment;
import com.cs446.covidtracer.R;

public class SelfCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_check_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, SelfCheckFragment.newInstance())
                .commitNow();
        }
    }


}