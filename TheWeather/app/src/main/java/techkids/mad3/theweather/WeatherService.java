package techkids.mad3.theweather;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.GregorianCalendar;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WeatherService extends IntentService {
    private Bundle bundleAlarm;
    private Intent intentAlarm;

    public WeatherService() {
        super("WeatherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
            URL url = null;

            try {
                url = new URL("http://api.openweathermap.org/data/2.5/weather?q=Hanoi&appid=688a892e665de9e2f5057f2b48e79ddd");
                InputStreamReader reader = new InputStreamReader(url.openStream(),"UTF-8");
                char[] buff = new char[64];
                StringBuilder x = new StringBuilder();
                int numRead = 0;
                while ((numRead = reader.read(buff)) >= 0) {
                    x.append(new String(buff, 0, numRead));
                }
                Log.d("TAGGG",x.toString());

                String result = x.toString();
                JSONObject resultObject = new JSONObject(result);
                JSONArray weatherArray = resultObject.getJSONArray("weather");
                JSONObject description = weatherArray.getJSONObject(0);
                String weatherDescription = description.getString("description");
                Log.d("Testtttt", weatherDescription);

                JSONObject main = resultObject.getJSONObject("main");
                String strtempMin = main.getString("temp_min");
                String strtempMax = main.getString("temp_max");
                String strDisplayTempMin = String.valueOf(Float.parseFloat(strtempMin) - 273.15);
                String strDisplayTempMax = String.valueOf(Float.parseFloat(strtempMax) - 273.15);
                String strTemp = main.getString("temp");
                String strDisplayMainTemp = String.valueOf(Float.parseFloat(strTemp) - 273.15);

                Log.d("min temp: ", strDisplayTempMin);


                // Set Notification Title
                String strweatherDescription = weatherDescription;

                // time at which alarm will be scheduled here alarm is scheduled at 1 day from current time,
                // we fetch  the current time in milliseconds and added 1 day time
                // i.e. 24*60*60*1000= 86,400,000   milliseconds in a day
                Long time = new GregorianCalendar().getTimeInMillis()+24*60*60*1000;

                intentAlarm = new Intent(WeatherService.this, MainActivity.class);
                bundleAlarm = new Bundle();
                bundleAlarm.putString("WeatherDescription", strweatherDescription);
                bundleAlarm.putString("TempMin", strDisplayTempMin);
                bundleAlarm.putString("TempMax", strDisplayTempMax);
                bundleAlarm.putString("TempMain", strDisplayMainTemp);

                intentAlarm.putExtra("DataTemp", bundleAlarm);
                intentAlarm.setAction("FILTER_ALARM");
                sendBroadcast(intentAlarm);

                // create the object
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                //set the alarm for particular time
                alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
                Toast.makeText(this, "Alarm Scheduled for Tommrrow", Toast.LENGTH_LONG).show();



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println(e.toString());
            }

    }
}
