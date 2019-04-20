package com.shivtech.notificationchannels.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.shivtech.notificationchannels.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import static android.app.NotificationChannel.DEFAULT_CHANNEL_ID;

public class NotificationUtils {

    public static final String APP_NOTIFICATION_CHANNEL = "SHIV_NOTIFICATIONS";
    private static final String NOTIFICATION_PATH = "/Android/data/com.shivtech.notificationchannels/sounds/";

    public static Notification fireNotification(Context context, String title, String message){
        return fireNotification(context,title,message,DEFAULT_CHANNEL_ID,"Default",true);
    }
    public static Notification fireNotification(Context context,String title,String message,String channelId,String channelName,boolean isDefaultSound){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notificationBuilder ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
            if (notificationChannel == null){
                notificationChannel = new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH);
                AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_MEDIA).build();
                Uri uri  = Uri.parse(Environment.getExternalStorageDirectory()+NOTIFICATION_PATH + channelId + ".mp3");
                notificationChannel.setSound(uri,audioAttributes);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            notificationBuilder = new Notification.Builder(context,channelId);

        }else {
            notificationBuilder = new Notification.Builder(context);
            if (!isDefaultSound) {
                notificationBuilder.setSound(Uri.parse(NOTIFICATION_PATH + channelId + ".mp3"));
            }
            else {
                notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            }
        }
        notificationBuilder.setSmallIcon(R.drawable.icon_notification);
        notificationBuilder.setColor(context.getResources().getColor(R.color.colorPrimary));
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setAutoCancel(true);

        Notification notification = notificationBuilder.build();
        notificationManager.notify(new Random().nextInt(9999),notification);
        return notification;
    }

    public static void updateNotificationSound(final Context context , final String channelId, final int soundId){
        BackgroundTaskHelper backgroundTaskHelper = new BackgroundTaskHelper(new BackgroundTaskHelper.BackgroundTaskListener() {
            @Override
            public void doTask()  {
                {
                    //copy that mp3 file for channel id
                    File myNotificationSoundFile = new File(Environment.getExternalStorageDirectory()+NOTIFICATION_PATH+channelId+".mp3");
                    if (myNotificationSoundFile.exists()){
                        myNotificationSoundFile.delete();
                    }

                    new File(myNotificationSoundFile.getParent()).mkdirs();

                    try {
//                        FileInputStream fileInputStream = new FileInputStream();
                        FileOutputStream fileOutputStream = new FileOutputStream(myNotificationSoundFile);

                        InputStream fileInputStream = context.getResources().openRawResource(soundId);

                        byte[] allBytesForRead = new byte[1024];
                        int reader = 0;

                        while ((reader = fileInputStream.read(allBytesForRead))!= -1){
                            fileOutputStream.write(allBytesForRead,0,reader);
                        }
                        fileOutputStream.close();
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void doAfterTask() {
                // do nothing
            }
        });
        backgroundTaskHelper.execute();
    }

    public static boolean isCreated(String channelId,Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
            return notificationChannel!=null;
        }
        return false;
    }

}
