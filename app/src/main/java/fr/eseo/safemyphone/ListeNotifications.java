package fr.eseo.safemyphone;

import android.app.Notification;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ListeNotifications extends ActionBarActivity {

    private static List<Notification> listeNotif = new ArrayList<Notification>();
    static ListView liste;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_notifications);
        // it was the 1st button
        liste = (ListView) findViewById(R.id.listeNotificationsView);
        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        //Notification notification = DeviceAdminSample.getMyDeviceSample().getNotification();
      //  listeNotif.add(notification);
        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<Notification> arrayAdapter = new ArrayAdapter<Notification>(  this,  android.R.layout.simple_list_item_1,  listeNotif );
        liste.setAdapter(arrayAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_liste_notifications, menu);
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
}
