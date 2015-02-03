package fr.eseo.safemyphone;

/**
 * Created by etudiant on 02/02/2015.
 */
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
public class AdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context ctxt, Intent intent) {
        ComponentName cn=new ComponentName(ctxt, AdminReceiver.class);
        DevicePolicyManager mgr=
                (DevicePolicyManager)ctxt.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mgr.setPasswordQuality(cn,
                DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC);
        onPasswordChanged(ctxt, intent);
    }

    @Override
    public void onPasswordFailed(Context ctxt, Intent intent) {
        Toast.makeText(ctxt, "Echec", Toast.LENGTH_LONG)
                .show();
    }

}