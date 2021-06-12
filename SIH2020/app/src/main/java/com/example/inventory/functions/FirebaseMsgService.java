package com.example.inventory.functions;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.inventory.MainActivity;
import com.example.inventory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class FirebaseMsgService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("getInstanceId failed", task.getException());
                            return;
                        }

                        String token = task.getResult().getToken();
                        SharedPreferences sharedPref = getApplication().getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("token", token);
                        editor.apply();
                    }
                });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        int type = getSharedPreferences("login_info", MODE_PRIVATE).getInt("usertype", -1);

        Map<String, String> data = remoteMessage.getData();
        String type1 = data.get("type");

        String sid,cNo;
        if(type1=="complaint") {
             sid = data.get("sid");
             cNo = data.get("requestNo");
        }
        else
        {
             sid = data.get("sid");
             cNo = data.get("requestNo");
        }



        Log.i("Vikas Notification",sid);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 101, intent, 0);

        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            channel = new NotificationChannel("222", "my_channel", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channel);
        }

//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(
//                        getApplicationContext(), "222")
//                        .setContentTitle(sid)
//                        .setAutoCancel(true)
////                        .setLargeIcon(((BitmapDrawable)getDrawable(R.drawable.lmis_logo)).getBitmap())
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.electro))
//                        .setContentText(cNo)
//                        .setContentIntent(pi);


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), "222")
                        .setContentTitle(sid)
                        .setAutoCancel(true)
//                        .setLargeIcon(((BitmapDrawable)getDrawable(R.drawable.lmis_logo)).getBitmap())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("it will be a very very long string that will not be in the preview")
                                .setBigContentTitle("Big Content Title")
                                .setSummaryText("Summary Text"))
                        //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.electro))
                        .setContentText(cNo)
                        .setColor(Color.BLUE)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setContentIntent(pi);

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        nm.notify(101, builder.build());
    }
}