package org.trecet.nowhere.sensorino.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.jjoe64.graphview.series.OnDataPointTapListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.trecet.nowhere.sensorino.R;
import org.trecet.nowhere.sensorino.model.Device;
import org.trecet.nowhere.sensorino.model.Devices;
import org.trecet.nowhere.sensorino.model.RemoteDevice;
import org.trecet.nowhere.sensorino.model.RemoteDeviceBluetooth;
import org.trecet.nowhere.sensorino.model.RemoteDeviceDummy;
import org.trecet.nowhere.sensorino.model.Sensor;
import org.trecet.nowhere.sensorino.model.SensorData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        switch (device.getRemote_type()) {
            case BLUETOOTH:
                remoteDevice = new RemoteDeviceBluetooth(device,this);
                break;
            case DUMMY:
                remoteDevice = new RemoteDeviceDummy(device,this);
                break;
            default:
                finish();
                Toast.makeText(this, getString(R.string.toast_device_unknown_type), Toast.LENGTH_SHORT).show();
        }
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

        // TODO need some error checking here (some errors are provided in the upper classes)
        // This should take care of everything
        remoteDevice.connect(new RemoteDevice.Connection() {
            @Override
            public void onConnected() {
                remoteDevice.getDeviceInfo(new RemoteDevice.Command() {
                    @Override
                    public void onSuccess() {
                        remoteDevice.getSensorInfo(new RemoteDevice.Command(){
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
                    public void onFailure() {
                    }
                });

            }

            @Override
            public void onDisconnected() {
            }
        });
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
        TextView pre = (TextView)findViewById(R.id.txt_viewDevice_pre);
        pre.setText(
                "Local name: " + device.getLocal_name() + "\n" +
                        "Remote name: " + device.getRemote_name() + "\n" +
                        "Remote address: " + device.getRemote_address() + "\n" +
                        "Frequency: " + device.getFrequency() + "\n" +
                        "Processor: " + device.getProcessor() + "\n" +
                        "Uptime: " + remoteDevice.getUptime() + "\n"
        );

        // TODO redrawing the graph all the time may not be very efficient
        LinearLayout linLayout = (LinearLayout) findViewById(R.id.view_device_graphs);
        linLayout.removeAllViews();
        for (String sensorName: device.getSensorNames()) {
            Log.i ("Sensorino", "Drawing graph "+sensorName);
            GraphView graph = new GraphView(this);
            graph.setTitle(sensorName+"  " + device.getSensor(sensorName).getType().toString().toLowerCase());
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
            for (SensorData sensorData : device.getSensor(sensorName).getData()) {
                series.appendData(new DataPoint(sensorData.getDate(), sensorData.getValue()), true, 100);
                graph.addSeries(series);
            }
            /*  The Toast here is not working very well...
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    java.util.Date time = new java.util.Date((long) dataPoint.getX() * 1000);
                    Toast.makeText(ViewDeviceActivity.this, time.toString() + dataPoint.getY(), Toast.LENGTH_SHORT).show();
                }
            });
            */
            // TODO need to draw the X axis correctly
            // Setting up the Graph axis
            graph.getViewport().setScalable(true);
            graph.getViewport().setScrollable(true);
//            graph.getViewport().scrollToEnd();
//            graph.getViewport().setMaxX(new Date().getTime());
//            graph.getViewport().setMinX(new Date().getTime()-10000);
            graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(ViewDeviceActivity.this));
//            graph.getGridLabelRenderer().setNumHorizontalLabels(2); // only 4 because of the space
            //Calendar calendar = Calendar.getInstance();
//            graph.getViewport().setMinX(new Date().getTime());
//            graph.getViewport().setMaxX(new Date().getTime() - 1000*60*60*24);
//            graph.getViewport().setXAxisBoundsManual(true);

            linLayout.addView(graph, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
        }

        TextView post = (TextView)findViewById(R.id.txt_viewDevice_post);
        post.setText("");
        for (String sensorName: device.getSensorNames()) {
            post.append("Sensor " + sensorName + "  (" +
                    device.getSensor(sensorName).getType().toString().toLowerCase() + "): \n");
            for (SensorData sensorData: device.getSensor(sensorName).getData()){
                post.append("   " + sensorData.getTimestamp() + ": " + sensorData.getValue() + "\n");
            }
        }
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
            remoteDevice.getSensorData(new RemoteDevice.Command() {
                @Override
                public void onSuccess() {
                    drawContent();
                }

                @Override
                public void onFailure() {
                }
            });
        }


        return super.onOptionsItemSelected(item);
    }
}

