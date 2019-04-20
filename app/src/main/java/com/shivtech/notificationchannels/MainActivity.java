package com.shivtech.notificationchannels;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.shivtech.notificationchannels.Utils.NotificationUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE = 1025;
    int[] ids;
    private String[] sounds;
    int currNotificationSound = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ids = new int[]{
                R.raw.pal_1,
                R.raw.pal_2,
                R.raw.pal_3,
                R.raw.teri_meri,
        };

        sounds = getResources().getStringArray(R.array.sounds);



        if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){

        NotificationUtils.updateNotificationSound(getApplicationContext(),NotificationUtils.APP_NOTIFICATION_CHANNEL,
                R.raw.pal_1
        );
        }else {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },REQ_CODE);
        }



        Spinner spinner  = findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item
                ,sounds));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currNotificationSound = i;
                NotificationUtils.updateNotificationSound(getApplicationContext(),NotificationUtils.APP_NOTIFICATION_CHANNEL
                        ,
                        ids[i]
//                        ContentResolver.SCHEME_ANDROID_RESOURCE + File.pathSeparator +"/"+ File.separator + getPackageName() + "/raw/"+ids[i]+".mp3"
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationUtils.fireNotification(getApplicationContext(),
                        "My Title",
                        "Current Sound:"+sounds[currNotificationSound],
                        NotificationUtils.APP_NOTIFICATION_CHANNEL,
                        "Shiv Notifications"
                        ,false
                        );
            }
        });

        Button btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE && grantResults.length > 0){
            NotificationUtils.updateNotificationSound(getApplicationContext(),NotificationUtils.APP_NOTIFICATION_CHANNEL,
                    R.raw.pal_1
            );
        }
    }
}
