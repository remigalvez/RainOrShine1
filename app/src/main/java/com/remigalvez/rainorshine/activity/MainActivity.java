package com.remigalvez.rainorshine.activity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.remigalvez.rainorshine.R;
import com.remigalvez.rainorshine.Settings;
import com.remigalvez.rainorshine.asynctasks.WeatherQueryAsyncTask;
import com.remigalvez.rainorshine.objects.DayWeather;
import com.remigalvez.rainorshine.sensor.LocationFinder;

import java.util.List;

public class MainActivity extends AppCompatActivity implements WeatherQueryAsyncTask.QueryCompletionListener, LocationFinder.LocationDetector {

    private final String TAG = "MainActivity";
    private ImageButton mRefreshBtn;
    private ProgressBar spinner;
    private TextView todayTxt;

    private List<DayWeather> mForecast;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieving location
        LocationFinder locationFinder = new LocationFinder(this, this);
        locationFinder.detectLocation();

        // Hide "Today" text
        todayTxt = (TextView) findViewById(R.id.todayTxt);
        todayTxt.setVisibility(View.GONE);

        spinner = (ProgressBar) findViewById(R.id.progressBar1);

        mRefreshBtn = (ImageButton) findViewById(R.id.refreshBtn);
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Refresh button pressed");
                refreshData(mLocation.getLatitude(), mLocation.getLongitude());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshData(double lat, double lon) {
        WeatherQueryAsyncTask weatherData = new WeatherQueryAsyncTask(this, this);
        weatherData.execute(lat, lon);
    }

    @Override
    public void locationFound(Location location) {
        // TODO: handle location success
        Log.d(TAG, "location found");
        mLocation = location;
        refreshData(mLocation.getLatitude(), mLocation.getLongitude());
    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {
        // TODO: hand location failure
        Log.d(TAG, "location not found");
    }

    @Override
    public void dataFound(List<DayWeather> forecast) {
        Log.d(TAG, "data found");
        mForecast = forecast;
        updateDisplay(forecast);

        spinner.setVisibility(View.GONE);
        todayTxt.setVisibility(View.VISIBLE);

    }

    private void updateDisplay(List<DayWeather> forecast) {
        final ImageView todayIcon = (ImageView) findViewById(R.id.todayIcon);
        final TextView todayHighTemp = (TextView) findViewById(R.id.todayHighTemp);
        final TextView todayLowTemp = (TextView) findViewById(R.id.todayLowTemp);

        DayWeather today = forecast.get(0);

        Ion.with(todayIcon).load(today.getIconUrl());

        todayHighTemp.setText(today.getHighDegrees() + " ˚" + Settings.units);
        todayLowTemp.setText(today.getLowDegrees() + " ˚" + Settings.units);


        final LinearLayout weatherDataLayout = (LinearLayout) findViewById(R.id.weatherDataLayout);
        weatherDataLayout.removeAllViews();

        for (int i = 1; i < Settings.numDays; i++) {
            DayWeather currentDay = forecast.get(i);

            LinearLayout dayWrapper = new LinearLayout(this);
            dayWrapper.setOrientation(LinearLayout.VERTICAL);
            dayWrapper.setPadding(30, 15, 30, 0);

            TextView weekday = new TextView(this);
            weekday.setText(currentDay.getWeekday());
            weekday.setTextSize(30);
            weekday.setTextColor(Color.WHITE);
            dayWrapper.addView(weekday);

            ImageView icon = new ImageView(this);
            LayoutParams iconParams = new LayoutParams(150, 150);
            Ion.with(icon).load(currentDay.getIconUrl());
            icon.setLayoutParams(iconParams);
            dayWrapper.addView(icon);

            TextView temperatureHigh = new TextView(this);
            temperatureHigh.setText(currentDay.getHighDegrees() + " ˚" + Settings.units);
            temperatureHigh.setTextSize(20);
            temperatureHigh.setTextColor(Color.WHITE);
            dayWrapper.addView(temperatureHigh);

            TextView temperatureLow = new TextView(this);
            temperatureLow.setText(currentDay.getLowDegrees() + " ˚" + Settings.units);
            temperatureLow.setTextSize(10);
            temperatureLow.setTextColor(Color.WHITE);
            dayWrapper.addView(temperatureLow);

            TextView description = new TextView(this);
            description.setText(currentDay.getDescription());
            description.setTextSize(15);
            description.setTextColor(Color.WHITE);
            dayWrapper.addView(description);

            weatherDataLayout.addView(dayWrapper);
        }
    }

    @Override
    public void dataNotFound() {
        Log.d(TAG, "data not found");
    }
}




























