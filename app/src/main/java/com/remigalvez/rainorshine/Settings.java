package com.remigalvez.rainorshine;

import com.remigalvez.rainorshine.activity.MainActivity;

/**
 * Created by Remi on 10/24/15.
 */
public class Settings {

    public static String units = "F";

    public static MainActivity sMainActivity;

    public final static int MAX_DAYS = 10;
    public static int numDays = 5;

    public static void setMainActivity(MainActivity mainActivity) {
        sMainActivity = mainActivity;
    }

    public static void setUnits(String unit) {
        units = unit;
        sMainActivity.updateDisplay();
    }

    public static void setNumDays(int num) {
        numDays = num;
        System.out.println(numDays);
        sMainActivity.updateDisplay();
    }

}
