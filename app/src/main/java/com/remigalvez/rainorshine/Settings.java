package com.remigalvez.rainorshine;

import com.remigalvez.rainorshine.activity.MainActivity;

/**
 * Created by Remi on 10/24/15.
 */
public class Settings {

    public static String units = "F";

    public static MainActivity MAIN_ACTIVITY;

    public final static int MAX_DAYS = 10;
    public static int numDays = 7;

    public static void setMainActivity(MainActivity mainActivity) {
        MAIN_ACTIVITY = mainActivity;
    }

    public static void setUnits(String unit) {
        units = unit;
        MAIN_ACTIVITY.updateDisplay();
    }

    public static void setNumDays(int num) {
        if (num > 10) num = 10;
        numDays = num;
        System.out.println(numDays);
        MAIN_ACTIVITY.updateDisplay();
    }

}
