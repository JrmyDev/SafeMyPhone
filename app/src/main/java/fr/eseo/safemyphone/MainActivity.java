package fr.eseo.safemyphone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Switch;


public class MainActivity extends ActionBarActivity {

    private Button buttonPref;
    private Button addNotificationBtn;
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
        buttonPref = (Button) findViewById(R.id.preference);
        buttonPref.setOnClickListener(actionPreference);
        addNotificationBtn = (Button) findViewById(R.id.nouvelle_notification);
        addNotificationBtn.setOnClickListener(actionAjoutNotification);
        deleteNotificationBtn = (Button) findViewById(R.id.supprimer_notification);
        deleteNotificationBtn.setOnClickListener(actionSuppressionNotification);
        buttonPref.setOnClickListener(myhandler1);





    }
    View.OnClickListener actionPreference = new View.OnClickListener() {
        public void onClick(View v) {
            // it was the 1st button
            intent = new Intent(MainActivity.this, PrefActivity.class);
            startActivity(intent);
        }
    };
    public View.OnClickListener actionAjoutNotification = new View.OnClickListener() {
        public void onClick(View v) {
            createNotification();
            Toast.makeText(getBaseContext(), "Ajout d'une notification", Toast.LENGTH_SHORT).show();
        }
    };
    View.OnClickListener actionSuppressionNotification = new View.OnClickListener() {
        public void onClick(View v) {
            deleteNotification();
            Toast.makeText(getBaseContext(), "Suppression d'une notification", Toast.LENGTH_SHORT).show();
        }
    };
    public void createNotification(){
        //Récupération du notification Manager
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
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

        //Création de la notification avec spécification de l'icone de la notification et le texte qui apparait à la création de la notfication
        final Notification notification = new Notification(R.drawable.notification, notificationTitle, System.currentTimeMillis());
        }
    };

        //Definition de la redirection au moment du clique sur la notification. Dans notre cas la notification redirige vers notre application
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, NotificationHomeActivity.class), 0);
    private void activateService() {
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        demoDeviceAdmin = new ComponentName(this, DeviceAdminSample.class);
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

    public void onClick(){
        int id = 2;
    }


}
