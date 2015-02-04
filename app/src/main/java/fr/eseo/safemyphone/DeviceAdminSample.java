package fr.eseo.safemyphone;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
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
            createNotification(context,intent,Resources.getSystem());
            try{
                takePictureNoPreview(context);
            }catch(IOException e){
                System.out.println("erreur"+e);
            }

            showToast(context, context.getString(R.string.admin_receiver_password_failed));
            System.out.println("test");
            //afficher la notif
            //prendre une photo
        }

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

    public void takePictureNoPreview(Context context) throws IOException{
        // open back facing camera by default
        Camera myCamera = Camera.open();

        if (myCamera != null) {
            if (myCamera.getNumberOfCameras() >= 2) {

                //if you want to open front facing camera use this line
                myCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                //if you want to use the back facing camera
                myCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
            try {
                //set camera parameters if you want to
                //...

                // here, the unused surface view and holder
                SurfaceView dummy = new SurfaceView(context);
                myCamera.setPreviewDisplay(dummy.getHolder());
                myCamera.startPreview();

                myCamera.takePicture(null, null, getJpegCallback());

            } finally {
            }

        } else {
            //booo, failed!
        }
    }


    private PictureCallback getJpegCallback() {
        PictureCallback jpeg = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream("test.jpeg");
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