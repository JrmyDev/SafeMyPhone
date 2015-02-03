package fr.eseo.safemyphone;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import static fr.eseo.safemyphone.R.xml;

/**
 * Created by etudiant on 03/02/2015.
 */
public class NotificationHomeActivity extends Activity{
    private Button addNotificationBtn;
    private Button deleteNotificationBtn;
    private String notificationTitle;
    public static final int NOTIFICATION_ID = 42;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        addNotificationBtn = (Button) findViewById(R.id.add_notification);

        addNotificationBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                createNotification();
                Toast.makeText(getBaseContext(), "Ajout d'une notification", Toast.LENGTH_SHORT).show();
            }
        });

        deleteNotificationBtn = (Button) findViewById(R.id.delete_notification);
        deleteNotificationBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
               deleteNotification();
               Toast.makeText(getBaseContext(), "Suppression d'une notification", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final void createNotification(){
        //Récupération du notification Manager
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //Création de la notification avec spécification de l'icone de la notification et le texte qui apparait à la création de la notfication
        final Notification notification = new Notification(R.drawable.notification, notificationTitle, System.currentTimeMillis());

        //Definition de la redirection au moment du clique sur la notification. Dans notre cas la notification redirige vers notre application
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, NotificationHomeActivity.class), 0);

        //Récupération du titre et description de la notfication
        final String notificationTitle = getResources().getString(R.string.notification_title);
        final String notificationDesc = getResources().getString(R.string.notification_desc);

        //Notification & Vibration
        notification.setLatestEventInfo(this, notificationTitle, notificationDesc, pendingIntent);

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void deleteNotification(){
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        //la suppression de la notification se fait grâce a son ID
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
