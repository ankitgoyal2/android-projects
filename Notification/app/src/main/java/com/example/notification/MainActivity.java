package com.example.notification;

import com.example.notification.serviceMan.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;
    private EditText title;
    private  EditText message;
    public static final String CHANNEL_1_ID="channel1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(getApplicationContext(),testing.class));
        notificationManager=NotificationManagerCompat.from(this);
        title=findViewById(R.id.title);
        message=findViewById(R.id.message);
    }
    public void sendOnChannel1(View view)
    {
        String title1=title.getText().toString();
        String message1=message.getText().toString();
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);

        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message1);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);


     /*   private static final String KEY_TEXT_REPLY = "key_text_reply";
        String replyLabel="String label";
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                    .setLabel(replyLabel)
                    .build();
            PendingIntent replyPendingIntent =
                    PendingIntent.getBroadcast(getApplicationContext(),
                            0,
                            broadcastIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Action action =
                    new NotificationCompat.Action.Builder(R.drawable.ic_reply_icon,
                            "its a action title string", replyPendingIntent)
                            .addRemoteInput(remoteInput)
                            .build();
        }

*/


//        Notification notification=new NotificationCompat.Builder(this,App.CHANNEL_1_ID)
//                .setSmallIcon(R.drawable.ic_audio)
//                .setContentTitle(title1)
//                .setContentText(message1)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                                  .bigText("it would be a very very long long message which can not be displayed in the a notification"))
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
////                .setStyle(new NotificationCompat.MessagingStyle("Me")
////                        .setConversationTitle("Team lunch")
////                        .addMessage("Hi", 122, (Person) null) // Pass in null for user.
////                        .addMessage("What's up?",123, "Coworker")
////                        .addMessage("Not much", 124 , (Person)null)
////                        .addMessage("How about lunch?", 125, "Coworker")
////
////                )
//
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)     //show on lock screen
//                .setColor(Color.CYAN)
//                .setContentIntent(contentIntent)
//                .setAutoCancel(true)
//                .setOnlyAlertOnce(true)
//               // .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
//                .build();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_audio)
                .setContentTitle(title1)
                .setContentText(message1)
              //  .setLargeIcon(R.drawable.boss)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("hjwhjhbjrujefvnkjfbvjfkjvndfjkbdhjfvdshfvhjsdbjhdsbhjfbhjfbvsdfhjvbdfjhvbjhfbj")
//                        .setBigContentTitle("Big Content Title")
//                        .setSummaryText("Summary Text"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
                .build();
        notificationManager.notify(1,notification);
    }
}
