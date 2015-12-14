package com.forchild.server;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocalDayWeatherForecast;
import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.forchild.data.RequestUpdateLocation;
import com.forchild000.surface.ServiceCore;

public class LocationServer implements AMapLocationListener, AMapLocalWeatherListener {
	private final long ONE_MINUTE = 60000;
	private LocationManagerProxy mLocationManagerProxy;
	private Context context;
	private boolean isRunning = false;
	private long time = 0;
	private Preferences preference;
	private double la;
	private double lo;
	private String address;

	public LocationServer(Context context) {
		this.context = context;
		this.start();
		preference = new Preferences(context);
	}

	public void start() {
		if (!isRunning) {
			mLocationManagerProxy = LocationManagerProxy.getInstance(context);
			mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, ONE_MINUTE, 0, this);
			mLocationManagerProxy.setGpsEnable(true);
			isRunning = true;
			// Log.e("LocationServer", "Location Server has been start");

			mLocationManagerProxy.requestWeatherUpdates(LocationManagerProxy.WEATHER_TYPE_FORECAST, this);
			// mLocationManagerProxy.requestWeatherUpdates(
			// LocationManagerProxy.WEATHER_TYPE_LIVE, this);
		}
	}

	public void stop() {
		if (isRunning == false) {
			return;
		}

		try {
			mLocationManagerProxy.removeUpdates(this);
			isRunning = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onLocationChanged(AMapLocation arg0) {
		// Log.e("LocationServer",
		// "onLocationChanged in Location Server has been start");
		// Log.e("LocationServer", "error code: " +
		// arg0.getAMapException().getErrorCode());
		if (arg0 != null && arg0.getAMapException().getErrorCode() == 0) {
			la = arg0.getLatitude();
			lo = arg0.getLongitude();
			address = arg0.getAddress();
			time = arg0.getTime();

			Intent locationBroadcastIntent = new Intent();
			locationBroadcastIntent.setAction("com.forchild.location");
			locationBroadcastIntent.putExtra("lo", lo);
			locationBroadcastIntent.putExtra("la", la);
			locationBroadcastIntent.putExtra("address", address);
			locationBroadcastIntent.putExtra("time", time);
			context.sendBroadcast(locationBroadcastIntent);

			if (preference == null) {
				preference = new Preferences(context);

			}

			if (!preference.getLoginState()) {
				this.stop();
			}
			if (preference.getSid() != null) {
				ServiceCore.addNetTask(new RequestUpdateLocation(preference.getSid(), System.currentTimeMillis(), la, lo));
			}
			
			ServiceCore.setAMapLocation(arg0);

			// if (preference.getSid() != null) {
			// RequestUpdateLocation rul = new
			// RequestUpdateLocation(preference.getSid(),
			// System.currentTimeMillis(), la, lo);
			// Intent locationIntent = new Intent();
			// locationIntent.setAction("com.forolder.sendHttpsBeans");
			// locationIntent.putExtra("bean", rul);
			// context.sendBroadcast(locationIntent);
			// }
		}
	}

	public boolean isAlive() {
		return this.isRunning;
	}

	public long getTime() {
		return time;
	}

	public double getLatitude() {
		return la;
	}

	public double getLongitude() {
		return lo;
	}

	public String getAddress() {
		return address;
	}

	@Override
	public void onLocationChanged(Location arg0) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onWeatherForecaseSearched(AMapLocalWeatherForecast arg0) {
		Log.e("Fololder.locationserver weather:", "weather listener is start");
		if (arg0 != null && arg0.getAMapException().getErrorCode() == 0) {
			List<AMapLocalDayWeatherForecast> forcasts = arg0.getWeatherForecast();
			for (int i = 0; i < forcasts.size(); i++) {
				AMapLocalDayWeatherForecast forcast = forcasts.get(i);
				switch (i) {
				case 0:
					Log.e("LocationServer:",
							forcast.getCity() + "今天 :" + forcast.getDate() + " )" + forcast.getDayWeather() + "    " + forcast.getDayTemp() + "℃/"
									+ forcast.getNightTemp() + "℃    " + forcast.getDayWindPower() + "级");
					preference.setTodayWeather(forcast.getDayWeather(), forcast.getDayTemp(), forcast.getNightTemp());
					break;
				case 1:
					// Log.e("LocationServer:",
					// forcast.getCity() + "明天 :" + forcast.getDate() + " )" +
					// forcast.getDayWeather() + "    " + forcast.getDayTemp() +
					// "℃/"
					// + forcast.getNightTemp() + "℃    " +
					// forcast.getDayWindPower() + "级");
					preference.setTomorrowWeather(forcast.getDayWeather(), forcast.getDayTemp(), forcast.getNightTemp());
					break;
				case 2:
					// Log.e("LocationServer:",
					// forcast.getCity() + "后天 :" + forcast.getDate() + " )" +
					// forcast.getDayWeather() + "    " + forcast.getDayTemp() +
					// "℃/"
					// + forcast.getNightTemp() + "℃    " +
					// forcast.getDayWindPower() + "级");
					preference.setThirdDayWeather(forcast.getDayWeather(), forcast.getDayTemp(), forcast.getNightTemp());
					break;
				}
			}
			// Intent mainUiRenewIntent = new Intent();
			// mainUiRenewIntent.setAction("com.forolder.main.ui.renew");
			// context.sendBroadcast(mainUiRenewIntent);
		}
	}

	@Override
	public void onWeatherLiveSearched(AMapLocalWeatherLive arg0) {
		if (arg0 != null && arg0.getAMapException().getErrorCode() == 0) {
			String city = arg0.getCity();// 城市
			String weather = arg0.getWeather();// 天气情况
			String windDir = arg0.getWindDir();// 风向
			String windPower = arg0.getWindPower();// 风力
			String humidity = arg0.getHumidity();// 空气湿度
			String reportTime = arg0.getReportTime();// 数据发布时间
			Log.e("Fololder.locationserver weather today:", city + weather);
		}
	}

}
