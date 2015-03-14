package org.trecet.nowhere.sensorino.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.trecet.nowhere.sensorino.R;
import org.trecet.nowhere.sensorino.model.DevicePersistorFactory;
import org.trecet.nowhere.sensorino.model.DevicePersistor;

import java.util.ArrayList;


public class ListDevicesActivity extends Activity {
    ListView listView ;
    DevicePersistor devices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Only to initiate local variables. The rest is done in onResume()
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensorino_device_list);
    }

    @Override
    public void onResume(){
        super.onResume();

        // Get the device list
        devices = DevicePersistorFactory.getInstance().getDevicePersistor(this);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listDevices);
        // Set values
        //String[] values = new String[] {"a","b","c"};

        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < devices.size(); ++i) {
            names.add(devices.getDeviceByPosition(i).getLocal_name());
        }
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, names);


        // Assign adapter to ListView
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent action_view_device = new Intent(ListDevicesActivity.this,
                        ViewDeviceActivity.class);
                action_view_device.putExtra("DeviceID", position);
                //action_view_device.putExtra("Device", devices.getDeviceByPosition(position));
                startActivity(action_view_device);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensorino_device_list, menu);
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
// TODO rebuild chat tool
//        if (id == R.id.action_chat) {
//            Intent action_chat= new Intent(this, ChatMainActivity.class);
//            startActivity(action_chat);
//            return true;
//        }
        if (id == R.id.action_new_device) {
            Intent action_new_device= new Intent(this, NewDeviceActivity.class);
            startActivity(action_new_device);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

