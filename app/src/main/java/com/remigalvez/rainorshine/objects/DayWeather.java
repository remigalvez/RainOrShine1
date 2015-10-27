package com.remigalvez.rainorshine.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.remigalvez.rainorshine.Settings;

/**
 * Created by Remi on 10/24/15.
 */
public class DayWeather implements Parcelable {

    private final String mCityAndState;
    private String mHighFarenheit;
    private String mHighCelsius;

    private String mLowFarenheit;
    private String mLowCelsius;

    private String mDescription;

    private String mIconUrl;

    private String mWeekday;

    public DayWeather(String cityAndState, String highFarenheit, String lowFarenheit, String highCelsius, String lowCelsius, String description, String iconUrl, String weekday) {
        mCityAndState = cityAndState;

        mHighFarenheit = highFarenheit;
        mLowFarenheit = lowFarenheit;

        mHighCelsius = highCelsius;
        mLowCelsius = lowCelsius;

        mDescription = description;

        mIconUrl = iconUrl;

        mWeekday = weekday;
    }

    public DayWeather(Parcel in) {
        mCityAndState = in.readString();
        mHighFarenheit = in.readString();
        mHighCelsius = in.readString();
        mLowFarenheit = in.readString();
        mLowCelsius = in.readString();
        mDescription = in.readString();
        mIconUrl = in.readString();
        mWeekday = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCityAndState);
        dest.writeString(mHighFarenheit);
        dest.writeString(mLowFarenheit);
        dest.writeString(mHighCelsius);
        dest.writeString(mLowCelsius);
        dest.writeString(mDescription);
        dest.writeString(mIconUrl);
        dest.writeString(mWeekday);
    }

    public static final Parcelable.Creator<DayWeather> CREATOR = new Parcelable.Creator<DayWeather>() {
        public DayWeather createFromParcel(Parcel in) {
            return new DayWeather(in);
        }

        public DayWeather[] newArray(int size) {
            return new DayWeather[size];
        }
    };
}
