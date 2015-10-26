package com.remigalvez.rainorshine.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.remigalvez.rainorshine.Utils;
import com.remigalvez.rainorshine.objects.DayWeather;

import java.util.List;

/**
 * Created by Remi on 10/23/15.
 */
public class WeatherQueryAsyncTask extends AsyncTask<Double, Integer, List<DayWeather>> {
    private static final String TAG = "WeatherQueryAsyncTask";

    private Context mContext;
    private QueryCompletionListener mCompletionListener;

    public interface QueryCompletionListener {
        public void dataFound(List<DayWeather> forecast);
        public void dataNotFound();
    }

    public WeatherQueryAsyncTask(Context context, QueryCompletionListener completionListener) {
        Log.d(TAG, "constructing WeatherQueryAsyncTask");
        mContext = context;
        mCompletionListener = completionListener;
    }

    @Override
    protected List<DayWeather> doInBackground(Double... query) {
        Log.d(TAG, "WeatherQueryAsyncTask launched");

        List<DayWeather> dailyWeather = null;
        String data = Utils.getWeatherData(query[0], query[1]);
        try {
            dailyWeather = Utils.parseJSONtoURL(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dailyWeather;
    }

    @Override
    protected void onPostExecute(List<DayWeather> forecast) {
        if (forecast != null) {
            mCompletionListener.dataFound(forecast);
        } else {
            mCompletionListener.dataNotFound();
        }
    }
}
