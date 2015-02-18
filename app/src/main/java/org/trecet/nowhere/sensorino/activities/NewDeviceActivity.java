package org.trecet.nowhere.sensorino.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.trecet.nowhere.sensorino.R;
import org.trecet.nowhere.sensorino.model.Device;
import org.trecet.nowhere.sensorino.model.Devices;

public class NewDeviceActivity extends Activity {

    private EditText txt_local_name;
    private EditText txt_remote_name;
    private EditText txt_remote_address;
    private EditText txt_frequency;
    private Button but_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensorino_device_new);

        // We grab all the stuff
        txt_local_name = (EditText)findViewById(R.id.txt_new_device_local_name);
        txt_remote_name = (EditText)findViewById(R.id.txt_new_device_remote_name);
        txt_frequency = (EditText)findViewById(R.id.txt_new_device_frequency);
        txt_remote_address = (EditText)findViewById(R.id.txt_new_device_remote_address);

        but_create = (Button)findViewById(R.id.but_new_device_create);

        // Button
        but_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String local_name = txt_local_name.getText().toString();
                String remote_name = txt_remote_name.getText().toString();
                String remote_address = txt_remote_address.getText().toString();
                int frequency = Integer.parseInt(txt_frequency.getText().toString());

                // TODO we would need to check if one of the names already exists.
                Device device = new Device ();
                device.setRemote_name(remote_name);
                device.setLocal_name(local_name);
                device.setRemote_address(remote_address);
                device.setFrequency(frequency);

                Devices devices = Devices.getInstance(NewDeviceActivity.this);
                if (devices.add(device) == 0) {
                    finish();
                    Toast.makeText(NewDeviceActivity.this, getString(R.string.toast_new_device_created), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewDeviceActivity.this, getString(R.string.toast_new_device_error), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


 // Usar GSON

}
