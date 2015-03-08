package org.trecet.nowhere.sensorino.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.trecet.nowhere.sensorino.R;
import org.trecet.nowhere.sensorino.model.Device;
import org.trecet.nowhere.sensorino.model.Devices;
import org.trecet.nowhere.sensorino.model.RemoteDeviceType;

import java.util.ArrayList;
import java.util.List;

public class NewDeviceActivity extends Activity {

    private EditText txt_local_name;
    private EditText txt_remote_name;
    private EditText txt_remote_address;
    private EditText txt_frequency;
    private Button but_create;
    private Spinner spinner_remote_type;

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

        // Spinner
        // Add items to the Spinner
        spinner_remote_type = (Spinner) findViewById(R.id.spinner_new_device_remote_type);
        List<String> list = new ArrayList<String>();
        for (RemoteDeviceType item: RemoteDeviceType.values()) {
            list.add(item.toString());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_remote_type.setAdapter(dataAdapter);

        // Set Listener to Spinner
        class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {


                Toast.makeText(parent.getContext(),
                        "On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        }
        spinner_remote_type.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        // Button
        but_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String local_name = txt_local_name.getText().toString();
                String remote_name = txt_remote_name.getText().toString();
                String remote_address = txt_remote_address.getText().toString();
                int frequency = Integer.parseInt(txt_frequency.getText().toString());
                RemoteDeviceType remote_type = RemoteDeviceType.valueOf(
                        spinner_remote_type.getSelectedItem().toString());

                // TODO we would need to check if one of the names already exists.
                Device device = new Device ();
                device.setRemote_name(remote_name);
                device.setLocal_name(local_name);
                // TODO we need to check the address is a valid BT address
                device.setRemote_address(remote_address);
                device.setFrequency(frequency);
                device.setRemote_type(remote_type);

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
