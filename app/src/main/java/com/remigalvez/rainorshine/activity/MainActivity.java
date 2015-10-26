package com.remigalvez.rainorshine.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.remigalvez.rainorshine.R;
import com.remigalvez.rainorshine.Settings;
import com.remigalvez.rainorshine.SettingsActivity;
import com.remigalvez.rainorshine.asynctasks.WeatherQueryAsyncTask;
import com.remigalvez.rainorshine.objects.DayWeather;
import com.remigalvez.rainorshine.sensor.LocationFinder;

import java.util.List;

public class MainActivity extends AppCompatActivity implements WeatherQueryAsyncTask.QueryCompletionListener, LocationFinder.LocationDetector {
    private final String TAG = "MainActivity";

    private LinearLayout weatherDataLayout;
    private TextView cityAndState;
    private TextView todayDescription;
    private ImageView todayIcon;
    private TextView todayHighTemp;
    private TextView todayLowTemp;
    private ProgressBar spinner;
    private TextView todayTxt;

    private List<DayWeather> mForecast;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link xml elements
        todayTxt = (TextView) findViewById(R.id.todayTxt);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        weatherDataLayout = (LinearLayout) findViewById(R.id.weatherDataLayout);
        cityAndState = (TextView) findViewById(R.id.cityAndState);
        todayDescription = (TextView) findViewById(R.id.todayDescription);
        todayIcon = (ImageView) findViewById(R.id.todayIcon);
        todayHighTemp = (TextView) findViewById(R.id.todayHighTemp);
        todayLowTemp = (TextView) findViewById(R.id.todayLowTemp);

        // Retrieve location & weather data
        getLocationAndData();

        // Hide "Today" text until data is shown
        todayTxt.setVisibility(View.GONE);

        // Link main activity to settings
        Settings.setMainActivity(this);
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
            openSettings();
            return true;
        } else if (id == R.id.action_refresh) {
            refreshData(mLocation.getLatitude(), mLocation.getLongitude());
        }

        return super.onOptionsItemSelected(item);
    }

    public void getLocationAndData() {
        mForecast = null;
        // Retrieve location & weather data
        LocationFinder locationFinder = new LocationFinder(this, this);
        locationFinder.detectLocation();
        //
        todayTxt.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        weatherDataLayout.removeAllViews();
        cityAndState.setText("");
        todayDescription.setText("");
        todayIcon.setVisibility(View.GONE);
    }

    public void refreshData(double lat, double lon) {
        // Retrieve weather forecast from Weather Underground and create DayWeather objects
        WeatherQueryAsyncTask weatherData = new WeatherQueryAsyncTask(this, this);
        weatherData.execute(lat, lon);
    }

    public void refreshData(String zipcode) {
        double zip = Double.parseDouble(zipcode);
        // Retrieve weather forecast from Weather Underground and create DayWeather objects
        WeatherQueryAsyncTask weatherData = new WeatherQueryAsyncTask(this, this);
        weatherData.execute(zip);
    }

    @Override
    public void locationFound(Location location) {
        Log.d(TAG, "location found");
        mLocation = location;
        // Call weather api once location is found
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
        todayIcon.setVisibility(View.VISIBLE);

    }

    @Override
    public void dataNotFound() {
        Log.d(TAG, "data not found");

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setMessage(R.string.weather_data_not_found)
                .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Retrieve location & weather data
                        getLocationAndData();
                    }
                }).setNegativeButton(R.string.zipcode, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        openSettings();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void updateDisplay() {
        updateDisplay(mForecast);
    }

    private void updateDisplay(List<DayWeather> forecast) {

        DayWeather today = forecast.get(0);

        cityAndState.setText(today.getCityAndState());

        todayDescription.setText(today.getDescription());

        Ion.with(todayIcon).load(today.getIconUrl());

        todayHighTemp.setText(today.getHighDegrees() + " ˚" + Settings.units);
        todayLowTemp.setText(today.getLowDegrees() + " ˚" + Settings.units);

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
            description.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            dayWrapper.addView(description);

            weatherDataLayout.addView(dayWrapper);
        }
    }

    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
    }
}
