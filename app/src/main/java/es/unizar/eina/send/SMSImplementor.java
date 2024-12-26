package es.unizar.eina.send;

import android.app.Activity;
import android.net.Uri;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

/** Concrete implementor utilizando la aplicación de WhatsApp. No funciona en el emulador si no se ha configurado previamente */
public class SMSImplementor implements SendImplementor{

    /** actividad desde la cual se abrirá la aplicación de WhatsApp */
    private Activity sourceActivity;

    /** Constructor
     * @param source actividad desde la cual se abrira la aplicación de Whatsapp
     */
    public SMSImplementor(Activity source){
        setSourceActivity(source);
    }

    /**  Actualiza la actividad desde la cual se abrira la actividad de gestion de correo */
    public void setSourceActivity(Activity source) {
        sourceActivity = source;
    }

    /**  Recupera la actividad desde la cual se abrira la aplicación de Whatsapp */
    public Activity getSourceActivity(){
        return sourceActivity;
    }

    /**
     * Implementacion del metodo send utilizando la aplicacion de WhatsApp
     * @param phone teléfono
     * @param message cuerpo del mensaje
     */
    public void send (String phone, String message) {
        // Por implementar
        Uri smsUri= Uri.parse("sms:" + phone);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsUri);
        sendIntent.putExtra("sms_body", message);
        getSourceActivity().startActivity(sendIntent);
    }

}

