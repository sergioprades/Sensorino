package org.trecet.nowhere.sensorino.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.trecet.nowhere.sensorino.R;
import org.trecet.nowhere.sensorino.model.Device;
import org.trecet.nowhere.sensorino.model.Devices;
import org.trecet.nowhere.sensorino.model.RemoteDevice;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;


public class ViewDeviceActivity extends Activity {
    Device device;
    RemoteDevice remoteDevice;
    Devices devices;
    int deviceID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensorino_view_device);

        // Get the Device as a parameter
        devices = Devices.getInstance(this);
        deviceID = getIntent().getIntExtra("DeviceID",-1);
        device = devices.getDeviceByPosition(deviceID);
        remoteDevice = new RemoteDevice(device,this);
        //device = (Device)getIntent().getSerializableExtra("Device");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensorino_view_device, menu);
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();

        // This should take care of everything
        remoteDevice.connect(new RemoteDevice.Connection() {
            @Override
            public void onConnected() {
                remoteDevice.getDeviceInfo(new RemoteDevice.Command() {
                    @Override
                    public void onSuccess() {
                        drawContent();
                    }

                    @Override
                    public void onFailure() {
                    }
                });

            }

            @Override
            public void onDisconnected() {
            }
        });

        /*
            bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
                public void onDataReceived(byte[] data, String message) {
                    // Do something when data incoming
                    Log.i("Sensorino", "Received bytes: " + data.length);
                    TextView t = (TextView) findViewById(R.id.txt_viewDevice);
//                    t.append(message + "\n");
//                    Response response = new Response("message");
//                    if (response.getType() == "response_info") {
//                        Toast.makeText(ViewDeviceActivity.this, "Device recogniced",
//                                Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject json_response = new JSONObject(message);
                        device.setProcessor(json_response.getJSONObject("device_info").
                                getString("processor"));
                    } catch (JSONException e) {
                        Log.e("Sensorino", "Invalid JSON string: " + message, e);
                    }
//                      } else {
//                        Toast.makeText(ViewDeviceActivity.this, "Device not recognized",
//                                Toast.LENGTH_SHORT).show();
//                    }
                }
            });
        */
    }

    @Override
    public void onStop(){
        super.onStop();

        // Disconnect from device
        remoteDevice.disconnect();
    }
    @Override
    public void onResume() {
        super.onResume();

        setTitle("Device: " + device.getLocal_name());
        drawContent();

    }

    private void drawContent(){
        TextView t = (TextView)findViewById(R.id.txt_viewDevice);
        t.setText(
                "Local name: " + device.getLocal_name() + "\n" +
                        "Remote name: " + device.getRemote_name() + "\n" +
                        "Remote address: " + device.getRemote_address() + "\n" +
                        "Frequency: " + device.getFrequency() + "\n" +
                        "Processor: " + device.getProcessor() + "\n" +
                        "Uptime: " + remoteDevice.getUptime() + "\n"


        );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_device) {
            Intent action_new_device= new Intent(this, NewDeviceActivity.class);
            // Pass the device as parameter
            action_new_device.putExtra("DeviceID", deviceID);
            startActivity(action_new_device);
            return true;
        }
//TODO Rebuild chat activity
//        if (id == R.id.action_chat) {
//            Intent action_chat= new Intent(this, ChatMainActivity.class);
//            startActivity(action_chat);
//            return true;
//        }
        if (id == R.id.action_remove_device) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.remove_device))
                    .setMessage(getString(R.string.remove_device_confirmation))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            devices.remove(device);
                            Toast.makeText(ViewDeviceActivity.this,
                                    "Device " + device.getLocal_name() + " removed",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        }

        if (id == R.id.action_get_sensor_data) {
            remoteDevice.getSensorData();

        }

        return super.onOptionsItemSelected(item);
    }
}

/*
// Constants that indicate the current connection state
public static final int STATE_NONE = 0;             // we're doing nothing
public static final int STATE_LISTEN = 1;           // now listening for incoming connections
public static final int STATE_CONNECTING = 2;       // now initiating an outgoing connection
public static final int STATE_CONNECTED = 3;        // now connected to a remote device
public static final int STATE_NULL = -1;            // now service is null

// Message types sent from the BluetoothChatService Handler
public static final int MESSAGE_STATE_CHANGE = 1;
public static final int MESSAGE_READ = 2;
public static final int MESSAGE_WRITE = 3;
public static final int MESSAGE_DEVICE_NAME = 4;
public static final int MESSAGE_TOAST = 5;
*/