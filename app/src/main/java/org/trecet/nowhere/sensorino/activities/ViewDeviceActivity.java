package org.trecet.nowhere.sensorino.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.trecet.nowhere.sensorino.R;
import org.trecet.nowhere.sensorino.model.Device;
import org.trecet.nowhere.sensorino.model.Devices;


public class ViewDeviceActivity extends Activity {
    Device device;
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
        //device = (Device)getIntent().getSerializableExtra("Device");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensorino_view_device, menu);
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();

        setTitle("Device: " + device.getLocal_name());

        TextView t = (TextView)findViewById(R.id.txt_viewDevice);
        t.setText(
                "Local name: " + device.getLocal_name() + "\n" +
                "Remote name: " + device.getRemote_name() + "\n" +
                "Frequency: " + device.getFrequency() + "\n"

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

        return super.onOptionsItemSelected(item);
    }
}
