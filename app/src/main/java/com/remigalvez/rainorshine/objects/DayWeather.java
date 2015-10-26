package com.remigalvez.rainorshine.objects;

import com.remigalvez.rainorshine.Settings;

/**
 * Created by Remi on 10/24/15.
 */
public class DayWeather {

    private final String mCityAndState;
    private String mHighFarenheit;
    private String mHighCelsius;

    private String mLowFarenheit;
    private String mLowCelsius;

    private String mDescription;

    private String mIconUrl;

    private String mWeekday;
    private String mAmpm;

    public DayWeather(String cityAndState, String highFarenheit, String lowFarenheit, String highCelsius, String lowCelsius, String description, String iconUrl, String weekday, String ampm) {
        mCityAndState = cityAndState;

        mHighFarenheit = highFarenheit;
        mLowFarenheit = lowFarenheit;

        mHighCelsius = highCelsius;
        mLowCelsius = lowCelsius;

        mDescription = description;

        mIconUrl = iconUrl;

        mWeekday = weekday;
        mAmpm = ampm;
    }

    public String getHighDegrees() {
        switch (Settings.units) {
            case "C":
                return mHighCelsius;
            case "F":
                return mHighFarenheit;
            default:
                break;
        }
        return "N/A";
    }

    public String getLowDegrees() {
        switch (Settings.units) {
            case "C":
                return mLowCelsius;
            case "F":
                return mLowFarenheit;
            default:
                break;
        }
        return "...";
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public String getWeekday() {
        return mWeekday;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getCityAndState() {
        return mCityAndState;
    }

}
