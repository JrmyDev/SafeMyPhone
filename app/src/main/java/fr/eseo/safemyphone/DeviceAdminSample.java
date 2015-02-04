package fr.eseo.safemyphone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextThemeWrapper;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by etudiant on 03/02/2015.
 */
public class DeviceAdminSample extends DeviceAdminReceiver {
    public String notificationTitle;
    public String notificationDesc;
    public final int NOTIFICATION_ID = 42;

    void showToast(Context context, String msg1) {
        String msg = context.getString(R.string.admin_receiver_status)+msg1;
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, context.getString(R.string.admin_receiver_status_enabled));
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return context.getString(R.string.admin_receiver_status_disable_warning);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, context.getString(R.string.admin_receiver_status_disabled));
    }


    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        DevicePolicyManager mgr = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        int no = mgr.getCurrentFailedPasswordAttempts();

        if(no>0)
        {
            if(MainActivity.getSwitch1().isChecked()) {
                if(MainActivity.getSwitch2().isChecked()){
                    sendEmail(context);
                    takePictureNoPreview(context);
                }
                if(MainActivity.getSwitch3().isChecked()){
                    createNotification(context, intent, Resources.getSystem());
                    takePictureNoPreview(context);
                }

            }

            showToast(context, context.getString(R.string.admin_receiver_password_failed));
            System.out.println("test");
            //afficher la notif
            //prendre une photo
        }

    }

    private void sendEmail(Context context) {
        String email = PrefActivity.getEmail();
        String password = PrefActivity.getPassword();
    }

    public void createNotification(Context context,Intent intent, Resources res){
        //Récupération du notification Manager
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Création de la notification avec spécification de l'icone de la notification et le texte qui apparait à la création de la notfication
        final Notification notification = new Notification(R.drawable.notification, notificationTitle, System.currentTimeMillis());

        //Récupération du titre et description de la notfication
        //final String notificationTitle = res.getString(R.string.notification_title);
        final String notificationTitle = context.getResources().getString(R.string.notification_title);
        notificationDesc = PrefActivity.notification_desc;

        //Notification & Vibration
        // Definition de la redirection au moment du clique sur la notification. Dans notre cas la notification redirige vers notre application
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        //Notification & Vibration
        notification.setLatestEventInfo(context, notificationTitle, notificationDesc, pendingIntent);

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void takePictureNoPreview(Context context) throws RuntimeException{
        Camera myCamera = null;


            if (myCamera.getNumberOfCameras() >= 2) {

                myCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            }
        else{
                myCamera = Camera.open();
            }
        if (myCamera != null) {
            try {
                //set camera parameters if you want to
                //...

                // here, the unused surface view and holder
                SurfaceTexture texture = new SurfaceTexture(0);
                try {
                    myCamera.setPreviewTexture(texture);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myCamera.startPreview();

                myCamera.takePicture(null, null, getJpegCallback());

            } finally {
            }

        } else {
            //booo, failed!
        }
    }


    private PictureCallback getJpegCallback() {
        final Boolean mSDcheck = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        PictureCallback jpeg = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream fos;
                try {
                    if(mSDcheck) {
                        fos = new FileOutputStream("/sdcard/test.jpeg");
                    }else{
                        fos = new FileOutputStream("/storage/emulated/0/test.jpeg");
                    }
                    fos.write(data);
                    fos.close();
                } catch (IOException e) {
                    //do something about it
                }
            }
        };
        return jpeg;
    }



}