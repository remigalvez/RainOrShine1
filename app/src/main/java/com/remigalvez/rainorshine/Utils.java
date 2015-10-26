package com.remigalvez.rainorshine;

import com.remigalvez.rainorshine.objects.DayWeather;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Remi on 10/23/15.
 */
public class Utils {
    private static final String TAG = "Utils";

    public static String parseQuery(double lat, double lon) {

        String url = Constants.WU_SEARCH_URL + Constants.API_KEY + Constants.API_FEATURES +
                Constants.API_SETTINGS + Constants.API_CONDITIONS + Constants.API_QUERY + lat + "," + lon + Constants.API_OUTPUT_FORMAT;
        return url;
    }

    public static String getWeatherData(double lat, double lon) {
        String queryUrl = parseQuery(lat, lon);
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(queryUrl);
        HttpResponse response;
        String data = "";

        try {
            response = client.execute(request);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public static List<DayWeather> parseJSONtoURL(String data) {

        List<DayWeather> weatherForecast = new ArrayList<>();

        try {
            JSONObject reader = new JSONObject(data);
            JSONArray dailyWeatherData = reader.getJSONObject("forecast")
                    .getJSONObject("simpleforecast")
                    .getJSONArray("forecastday");

            for (int i = 0; i < dailyWeatherData.length(); i++) {
                JSONObject d = dailyWeatherData.getJSONObject(i);

                String highFahrenheit = d.getJSONObject("high").getString("fahrenheit");
                String highCelsius = d.getJSONObject("high").getString("celsius");

                String lowFahrenheit = d.getJSONObject("low").getString("fahrenheit");
                String lowCelsius = d.getJSONObject("low").getString("celsius");

                String conditions = d.getString("conditions");

                String iconUrl = d.getString("icon_url");

                String weekday = d.getJSONObject("date").getString("weekday_short");
                String ampm = d.getJSONObject("date").getString("ampm");

                weatherForecast.add(new DayWeather(highFahrenheit, lowFahrenheit, highCelsius, lowCelsius, conditions, iconUrl, weekday, ampm));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherForecast;
    }
}
