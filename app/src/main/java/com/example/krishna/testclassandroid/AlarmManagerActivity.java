package com.example.krishna.testclassandroid;

/**
 * Created by krishna on 10/10/2017.
 */



import android.os.Bundle;

import android.app.Activity;

public class AlarmManagerActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlarmStaticVariables.level = AlarmStaticVariables.level1;
        AlarmStaticVariables.partten = AlarmStaticVariables.partten1;

    }

}