package org.trecet.nowhere.sensorino.model;

import android.content.Context;
import android.content.SharedPreferences;

import org.trecet.nowhere.sensorino.model.impl.SharedPreferencesDevicePersistor;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Sergio on 14/03/2015.
 */
public class DevicePersistorFactory {

    private static DevicePersistorFactory singleton = null;

    private HashMap<SharedPreferences, DevicePersistor> devicesMap;
    private ReentrantLock lock;

    // Private Constructor
    private DevicePersistorFactory(){

        devicesMap = new HashMap<SharedPreferences, DevicePersistor>();
        lock = new ReentrantLock();
    }

    public synchronized static DevicePersistorFactory getInstance(){

        if(singleton == null) {
            singleton = new DevicePersistorFactory();
        }

        return singleton;
    }

    public DevicePersistor getDevicePersistor(Context context){

        //FIXME Si el día de mañana quieres cambiar a un persitor basado en SQL sólo tendrías que
        // hacer la implementación y crear aquí dentro el nuevo objeto.

        // De esta manera tienes desacoplada ya la implementación concreta de persitencia con el resto
        // del código.

        // Para que fuera perfectamente limpio, el getDevicePersistor no debería tener el parámtro context,
        // pero no soy experto en android y no sé cómo recuperar las SharedPreferences desde aquí dentro

        // De hecho desconozco si este código funcionará, porque está asumiendo que las sharedPreferences son siempre
        // las mismas independientemente del contexto.

        // si no es así y cada vez es un objeto distinto dímelo y vemos cómo arreglarlo, porque la idea claro
        // es no tener que crear un nuevo SharedPreferencesDevicePersistor. Habría que ver si el vale el mismo
        // SharedPreferences con el que se crea o hay que írselo actualizando cada vez (como tú hacías antes).


        //TODO Hardcoded but only in one line
        SharedPreferences preferences = context.getSharedPreferences("Prefs",0);

        if (devicesMap.containsKey(preferences)){
            return devicesMap.get(preferences);
        } else {
            lock.lock();
            try {
                //check again
                if (devicesMap.containsKey(preferences)) {
                    return devicesMap.get(preferences);
                } else {

                    DevicePersistor devicePersistor = new SharedPreferencesDevicePersistor(preferences);
                    devicesMap.put(preferences, devicePersistor);
                    return devicePersistor;
                }
            } finally {
                lock.unlock();
            }
        }
    }

}
