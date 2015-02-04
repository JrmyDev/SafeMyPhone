package fr.eseo.safemyphone;

import android.app.Activity;
import android.app.NotificationManager;
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
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private Button deleteNotificationBtn,listeNotifications, buttonPref ;
    public String notificationTitle;
    public String notificationDesc;
    public final int NOTIFICATION_ID = 42;
    private static Switch switch1;
    private static Switch switch2;
    private static Switch switch3;
    static final String TAG = "DeviceAdminSample";
    static final int ACTIVATION_REQUEST = 47; // identifies our request id
    DevicePolicyManager devicePolicyManager;
    DeviceAdminSample deviceAdminSample;
    ComponentName demoDeviceAdmin;
    SharedPreferences prefs;
    Intent intentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //recuperation valeur du switch
        prefs = this.getSharedPreferences("fr.eseo.safemyphone", Context.MODE_PRIVATE);
        Boolean bool1 = prefs.getBoolean("switch1",false);
        Boolean bool2 = prefs.getBoolean("switch2",false);
        Boolean bool3 = prefs.getBoolean("switch3",false);

        switch1=(Switch) findViewById(R.id.switch1);
        getSwitch1().setChecked(bool1);
        getSwitch1().setOnClickListener(myhandler2);
        switch2=(Switch) findViewById(R.id.switch2);
        getSwitch2().setChecked(bool2);
        getSwitch2().setOnClickListener(myhandler3);
        switch3=(Switch) findViewById(R.id.switch3);
        getSwitch3().setChecked(bool3);
        getSwitch3().setOnClickListener(myhandler4);

        buttonPref = (Button) findViewById(R.id.preference);
        buttonPref.setOnClickListener(actionPreference);
        deleteNotificationBtn = (Button) findViewById(R.id.supprimer_notification);
        deleteNotificationBtn.setOnClickListener(actionSuppressionNotification);
        listeNotifications = (Button) findViewById(R.id.listeNotifications);
        listeNotifications.setOnClickListener(actionListeNotifications);

    }
    View.OnClickListener actionPreference = new View.OnClickListener() {
        public void onClick(View v) {
            // it was the 1st button
            Intent intent0 = new Intent(MainActivity.this, PrefActivity.class);
            startActivity(intent0);
        }
    };

    View.OnClickListener actionSuppressionNotification = new View.OnClickListener() {
        public void onClick(View v) {
            deleteNotification();
            Toast.makeText(getBaseContext(), "Suppression d'une notification", Toast.LENGTH_SHORT).show();
        }
    };
    View.OnClickListener myhandler2 = new View.OnClickListener() {
        public void onClick(View v) {
            activateService();
            if (switch1.isChecked()) {
                //enregistrement valeur du switch
                prefs.edit().putBoolean("switch1",true).commit();
                if(!devicePolicyManager.isAdminActive(demoDeviceAdmin)) {
                    // Activate device administration
                    Intent intent1 = new Intent(
                            DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                            demoDeviceAdmin);
                    intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "Your boss told you to do this");
                    startActivityForResult(intent1, ACTIVATION_REQUEST);
                }
            }
            else{
                //enregistrement valeur du switch
                prefs.edit().putBoolean("switch1",false).commit();
                devicePolicyManager.removeActiveAdmin(demoDeviceAdmin);
            }
            Log.d(TAG, "onCheckedChanged to: " + getSwitch1().isChecked());
        }
    };
    //handler du switch 2 (activation des emails)
    View.OnClickListener myhandler3 = new View.OnClickListener() {
        public void onClick(View v) {
            if (switch2.isChecked()) {
                //enregistrement valeur du switch
                prefs.edit().putBoolean("switch2",true).commit();
            }
            else{
                //enregistrement valeur du switch
                prefs.edit().putBoolean("switch2",false).commit();
            }
            Log.d(TAG, "onCheckedChanged to: " + getSwitch2().isChecked());
        }
    };
    //handler du switch 3 (activation des notifications)
    View.OnClickListener myhandler4 = new View.OnClickListener() {
        public void onClick(View v) {
            if (switch3.isChecked()) {
                //enregistrement valeur du switch
                prefs.edit().putBoolean("switch3",true).commit();

            }
            else{
                //enregistrement valeur du switch
                prefs.edit().putBoolean("switch3",false).commit();
            }
            Log.d(TAG, "onCheckedChanged to: " + getSwitch3().isChecked());
        }
    };

    View.OnClickListener actionListeNotifications = new View.OnClickListener() {
        public void onClick(View v) {

         //   Intent intent2 = new Intent(MainActivity.this, ListeNotifications.class);
          //  startActivity(intent2);
        }
    };


    private void activateService() {
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        demoDeviceAdmin = new ComponentName(this, DeviceAdminSample.class);
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
        prefs = this.getSharedPreferences("fr.eseo.safemyphone", Context.MODE_PRIVATE);
        Boolean bool1 = prefs.getBoolean("switch1",false);
        Boolean bool2 = prefs.getBoolean("switch2",false);
        Boolean bool3 = prefs.getBoolean("switch3",false);

        switch1=(Switch) findViewById(R.id.switch1);
        getSwitch1().setChecked(bool1);
        getSwitch1().setOnClickListener(myhandler2);
        switch2=(Switch) findViewById(R.id.switch2);
        getSwitch2().setChecked(bool2);
        getSwitch2().setOnClickListener(myhandler3);
        switch3=(Switch) findViewById(R.id.switch3);
        getSwitch3().setChecked(bool3);
        getSwitch3().setOnClickListener(myhandler4);
    }


    public static Switch getSwitch1() {
        return switch1;
    }

    public void setSwitch1(Switch switch1) {
        this.switch1 = switch1;
    }

    public static Switch getSwitch2() {
        return switch2;
    }

    public void setSwitch2(Switch switch2) {
        this.switch2 = switch2;
    }

    public static Switch getSwitch3() {
        return switch3;
    }

    public void setSwitch3(Switch switch3) {
        this.switch3 = switch3;
    }
}
