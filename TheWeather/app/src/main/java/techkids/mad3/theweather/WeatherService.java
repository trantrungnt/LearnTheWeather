package techkids.mad3.theweather;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
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

                // time at which alarm will be scheduled here alarm is scheduled at 1 day from current time,
                // we fetch  the current time in milliseconds and added 1 day time
                // i.e. 24*60*60*1000= 86,400,000   milliseconds in a day
                //Long time = new GregorianCalendar().getTimeInMillis()+24*60*60*1000;
                long time = SystemClock.elapsedRealtime() + 10000;

                intentAlarm = new Intent(WeatherService.this, NotificationInformation.class);
                intentAlarm.putExtra(NotificationInformation.NOTIFICATION_ID, 1);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                intentAlarm.putExtra(NotificationInformation.NOTIFICATION, getNotification(weatherDescription,strDisplayMainTemp, strDisplayTempMin, strDisplayTempMax, pendingIntent));

                // create the object
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                //set the alarm for particular time
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time, PendingIntent.getBroadcast(this,0,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
                //Toast.makeText(this, "Alarm Scheduled for Tommrrow", Toast.LENGTH_LONG).show();

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

    private Notification getNotification(String tempDescription,
                                         String tempMain,
                                         String tempMin,
                                         String tempMax,
                                         PendingIntent pIntent
                                         )
    {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotification);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.moderate)
                // Set Ticker Message
                .setTicker(tempDescription + " in Ha Noi, Viet Nam")
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Set RemoteViews into Notification
                .setContent(remoteViews);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        remoteViews.setImageViewResource(R.id.imgTemp, R.drawable.moderate);

        // Locate and set the Text into customnotificationtext.xml TextViews
        remoteViews.setTextViewText(R.id.tvDescriptionTemp, tempDescription);
        remoteViews.setTextViewText(R.id.tvMinTemp, "Min: " + tempMin.substring(0, 5)  + " " + (char) 0x00B0 + "C");
        remoteViews.setTextViewText(R.id.tvMaxTemp, "Max: " + tempMax.substring(0, 5) + " " + (char) 0x00B0 + "C");
        remoteViews.setTextViewText(R.id.tvMainTemp, tempMain.substring(0, 5) + (char) 0x00B0 + "C");

        return builder.build();
    }
}
