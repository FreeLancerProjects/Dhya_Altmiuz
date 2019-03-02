package com.appzone.dhai.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.appzone.dhai.R;
import com.appzone.dhai.models.UserModel;
import com.appzone.dhai.preferences.Preferences;
import com.appzone.dhai.tags.Tags;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FireBaseNotificationMessaging extends FirebaseMessagingService {
    private Preferences preferences = Preferences.getInstance();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String,String> map = remoteMessage.getData();
        for (String key : remoteMessage.getData().keySet())
        {
            Log.e("Key",key+" "+"value"+" "+remoteMessage.getData().get(key));
        }

        manageNotification(map);


    }

    private void manageNotification(final Map<String, String> map) {
        String session = getSession();
        UserModel userModel = getUserData();
        int receiver_id = Integer.parseInt(map.get("receiver_id"));
        if (session.equals(Tags.session_login))
        {
            if (userModel!=null&&userModel.getId()==receiver_id)
            {
                new Handler(Looper.getMainLooper())
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CreateNotification(map);
                            }
                        },1);

            }
        }
    }

    private void CreateNotification(Map<String, String> map) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CreateProfessionalNotification(map);
        }else
            {
                CreateNativeNotification(map);

            }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateProfessionalNotification(Map<String, String> map) {
        String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.not;
        String CHANNEL_ID = "my_channel_01";
        CharSequence CHANNEL_NAME = "channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
        channel.setShowBadge(true);
        channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .build()
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setChannelId(CHANNEL_ID);
        builder.setSound(Uri.parse(sound_path));
        builder.setContentTitle(getString(R.string.admin));
        builder.setContentText(getString(R.string.welcome_thank));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(bitmap);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channel);
            manager.notify(1, builder.build());
        }


        }

    private void CreateNativeNotification(Map<String, String> map) {

    }


    private UserModel getUserData()
    {
        return preferences.getUserData(this);
    }

    private String getSession()
    {
        return preferences.getSession(this);
    }
}
