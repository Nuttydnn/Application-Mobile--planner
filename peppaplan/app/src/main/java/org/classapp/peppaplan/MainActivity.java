package org.classapp.peppaplan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    CalenderCustomView calenderCustomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calenderCustomView = (CalenderCustomView)findViewById(R.id.custom_calender_view);
    }
}