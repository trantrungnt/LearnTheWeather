package techkids.mad3.theweather;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WeatherService extends IntentService {

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

                Log.d("min temp: ", strDisplayTempMin);


                // Using RemoteViews to bind custom layouts into Notification
                RemoteViews remoteViews = new RemoteViews(getPackageName(),
                        R.layout.customnotification);

                // Set Notification Title
                String strweatherDescription = weatherDescription;

                Intent i = new Intent(this, MainActivity.class);

                // Open NotificationView.java Activity
                PendingIntent pIntent = PendingIntent.getActivity(this, 0, i,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        // Set Icon
                        .setSmallIcon(R.drawable.cloudy)
                        // Set Ticker Message
                        .setTicker(strweatherDescription)
                        // Dismiss Notification
                        .setAutoCancel(true)
                        // Set PendingIntent into Notification
                        .setContentIntent(pIntent)
                        // Set RemoteViews into Notification
                        .setContent(remoteViews);

                // Locate and set the Image into customnotificationtext.xml ImageViews
                remoteViews.setImageViewResource(R.id.imgTemp, R.drawable.cloudy);

                // Locate and set the Text into customnotificationtext.xml TextViews
                //remoteViews.setTextViewText(R.id.title,getString(R.string.customnotificationtitle));
                remoteViews.setTextViewText(R.id.tvMainTemp, strweatherDescription);
                remoteViews.setTextViewText(R.id.tvMinTemp, "Min: " + strDisplayTempMin.substring(0, 5)  + " " + (char) 0x00B0 + "C");
                remoteViews.setTextViewText(R.id.tvMaxTemp, "Max: " + strDisplayTempMax.substring(0, 5) + " " + (char) 0x00B0 + "C");

                // Create Notification Manager
                NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Build Notification with Notification Manager
                notificationmanager.notify(0, builder.build());


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
