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


                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.notification_icon)
                                .setContentTitle("My notification")
                                .setContentText("Hello World!");

                Intent resultIntent = new Intent(WeatherService.this, MainActivity.class);


                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(mId, mBuilder.build());


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
