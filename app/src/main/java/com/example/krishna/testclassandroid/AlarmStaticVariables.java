package com.example.krishna.testclassandroid;

import java.util.ArrayList;

/**
 * Created by krishna on 10/10/2017.
 */


public class AlarmStaticVariables {
    public static int level;
    public static float absValue;
    public static boolean inProcess;
    public static int level0 = 0;// vibration
    public static int level1 = 500;
    public static int level2=700;
    public static int level3=900;

    public static float lvolumn = (float) 0.5;
    public static float rvolumn = (float) 0.5;
    public static long[] partten = { 100, 2000, 1000, 2000 };
    public static long[] partten1 = { 100, 200, 100, 200 };

    public static int sampleSize = 712548;



    public static ArrayList<short[]> inBuf = new ArrayList<short[]>();
    public static int rateX = 8;
    public static int rateY = 10;


    public static int snoringCount = 0;
    public static int sampleCount = 3;//alarm after no of count
}
