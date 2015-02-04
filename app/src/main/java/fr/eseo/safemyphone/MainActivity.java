package fr.eseo.safemyphone;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Switch;


public class MainActivity extends ActionBarActivity {
    Button listeNotifications;
    private Button deleteNotificationBtn;
    public String notificationTitle;
    public String notificationDesc;
    public final int NOTIFICATION_ID = 42;
    Button buttonPref;
    Switch switch1;
    Intent intent;
    static final String TAG = "DeviceAdminSample";
    static final int ACTIVATION_REQUEST = 47; // identifies our request id
    DevicePolicyManager devicePolicyManager;
    ComponentName demoDeviceAdmin;
    SharedPreferences prefs;
    Intent intentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //recuperation valeur du switch
        prefs = this.getSharedPreferences("fr.eseo.safemyphone", Context.MODE_PRIVATE);
        Boolean bool = prefs.getBoolean("switch",false);
        switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setChecked(bool);
        switch1.setOnClickListener(myhandler2);
        buttonPref = (Button) findViewById(R.id.preference);
        buttonPref.setOnClickListener(myhandler1);
        //bouton affichage des notifications
        listeNotifications=(Button) findViewById(R.id.listeNotifications);
        listeNotifications.setOnClickListener(myhandler3);

        deleteNotificationBtn = (Button) findViewById(R.id.supprimer_notification);
        deleteNotificationBtn.setOnClickListener(actionSuppressionNotification);
    }
    View.OnClickListener myhandler1 = new View.OnClickListener() {
        public void onClick(View v) {
            // it was the 1st button
            intent = new Intent(MainActivity.this, PrefActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener myhandler2 = new View.OnClickListener() {
        public void onClick(View v) {
            activateService();
            if (switch1.isChecked()) {
                //enregistrement valeur du switch
                prefs.edit().putBoolean("switch",true).commit();
                if(!devicePolicyManager.isAdminActive(demoDeviceAdmin)) {
                    // Activate device administration
                    Intent intent = new Intent(
                            DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                            demoDeviceAdmin);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "Your boss told you to do this");
                    startActivityForResult(intent, ACTIVATION_REQUEST);
                }
            }
            else{
                //enregistrement valeur du switch
                prefs.edit().putBoolean("switch",false).commit();
                devicePolicyManager.removeActiveAdmin(demoDeviceAdmin);
            }
            Log.d(TAG, "onCheckedChanged to: " + switch1.isChecked());

        }
    };

    View.OnClickListener myhandler3 = new View.OnClickListener() {
        public void onClick(View v) {
            // it was the 1st button
            intent = new Intent(MainActivity.this, ListeNotifications.class);
            startActivity(intent);
        }
    };

    private void activateService() {
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        demoDeviceAdmin = new ComponentName(this, DeviceAdminSample.class);
    }
    public void createNotification(){
        //Récupération du notification Manager
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //Création de la notification avec spécification de l'icone de la notification et le texte qui apparait à la création de la notfication
        final Notification notification = new Notification(R.drawable.notification, notificationTitle, System.currentTimeMillis());

        //Definition de la redirection au moment du clique sur la notification. Dans notre cas la notification redirige vers notre application
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        //Récupération du titre et description de la notfication
        final String notificationTitle = getResources().getString(R.string.notification_title);
        notificationDesc = PrefActivity.notification_desc;

        //Notification & Vibration
        notification.setLatestEventInfo(this, notificationTitle, notificationDesc, pendingIntent);

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void deleteNotification(){
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        //la suppression de la notification se fait grâce a son ID
        notificationManager.cancel(NOTIFICATION_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVATION_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i("DeviceAdminSample", "Administration enabled!");
                } else {
                    Log.i("DeviceAdminSample", "Administration enable FAILED!");
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        ExpandableListView list = (ExpandableListView) findViewById(R.id.listeNotificationsView);
    }
}
