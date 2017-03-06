package com.darelbitsy.dbweather.helper.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.darelbitsy.dbweather.adapters.DatabaseOperation;
import com.darelbitsy.dbweather.controller.api.adapters.WeatherAdapter;
import com.darelbitsy.dbweather.helper.ConstantHolder;
import com.darelbitsy.dbweather.helper.utility.WeatherUtil;
import com.darelbitsy.dbweather.model.weather.Daily;
import com.darelbitsy.dbweather.model.weather.Hourly;
import com.darelbitsy.dbweather.model.weather.Weather;
import com.darelbitsy.dbweather.services.WeatherDatabaseService;

import io.reactivex.Single;

/**
 * Created by Darel Bitsy on 20/02/17.
 * This class help get Weather in background
 * Subclass will on override the onPostExecution
 * to choose of to display the data
 */

public class GetWeatherHelper {
    private final Context mContext;
    private static WeatherAdapter mWeatherAdapter;

    public GetWeatherHelper(Context context) {
        mContext = context;
        if (mWeatherAdapter == null) {
            mWeatherAdapter = new WeatherAdapter();
        }
    }

    public Single<Weather> getObservableWeatherFromApi(final DatabaseOperation database) {

        return io.reactivex.Single.create(emitter -> {
            try {
                Double[] coordinates = WeatherUtil.getCoordinates(database);
                    final Weather weather = mWeatherAdapter.getWeather(coordinates[0],
                            coordinates[1]);

                weather.setCityName(WeatherUtil.getLocationName(mContext,
                        coordinates[0],
                        coordinates[1]));

                Intent intent = new Intent(mContext, WeatherDatabaseService.class);
                intent.putExtra(ConstantHolder.WEATHER_DATA_KEY, weather);
                mContext.startService(intent);

                if (!emitter.isDisposed()) { emitter.onSuccess(weather); }

            } catch (Exception e) { if (!emitter.isDisposed()) { emitter.onError(e); } }
        });
    }

    public Single<Weather> getObservableWeatherFromDatabase(final DatabaseOperation database) {
        return Single.create(emitter -> {
           try {
               Weather weather = database.getWeatherData();
               weather.setCurrently(database.getCurrentWeatherFromDatabase());

               weather.setDaily(new Daily());
               weather.getDaily().setData(database.getDailyWeatherFromDatabase());

               weather.setHourly(new Hourly());
               weather.getHourly().setData(database.getHourlyWeatherFromDatabase());

               weather.setAlerts(database.getAlerts());

               if (!emitter.isDisposed()) { emitter.onSuccess(weather); }

           } catch (Exception e) {
               Log.i(ConstantHolder.TAG, "Error from getObservableWeatherFromDatabase: "
                       + e.getMessage());
               if (!emitter.isDisposed()) { emitter.onError(e); }
           }
        });
    }
}