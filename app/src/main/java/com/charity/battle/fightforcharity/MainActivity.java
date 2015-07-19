package com.charity.battle.fightforcharity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class MainActivity extends ActionBarActivity {

    private final int REQUEST_ENABLE_BT = 1;

    private final String NAME = "DonationDuel";

    private UUID MY_UUID = new UUID(1, 1);

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                if(device.getName().endsWith(NAME_SUFFIX))
                {
                    devicesAdapter.add(device.getName() + "\n" + device.getAddress());
                    deviceSpinner.setAdapter(devicesAdapter);
                }
            }
        }
    };

    private Context context;

    private final String NAME_SUFFIX = ":DonationDuel";

    /* Views */

    private Button searchButton;

    private Button connectButton;

    private Button hostButton;

    private TextView mainText;

    private ImageView brainTreeLogo;

    private ImageView spin;

    private ImageView xButton;

    private Spinner deviceSpinner;

    /* Bluetooth */

    private BluetoothAdapter bluetoothAdapter;

    private ArrayAdapter<String> devicesAdapter;

    private boolean connected = false;

    private BluetoothSocket sock;

    private InputStream in;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // set layout
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        setBluetooth();

        // set view variables
        searchButton  = (Button)    findViewById(R.id.searchButton);
        connectButton = (Button)    findViewById(R.id.connectButton);
        hostButton    = (Button)    findViewById(R.id.hostButton);
        mainText      = (TextView)  findViewById(R.id.mainText);
        brainTreeLogo = (ImageView) findViewById(R.id.brainTree);
        spin          = (ImageView) findViewById(R.id.spin);
        xButton       = (ImageView) findViewById(R.id.xButton);
        deviceSpinner = (Spinner)   findViewById(R.id.deviceSpinner);

        /* onClick listeners */

        hostButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AcceptThread acceptThread = new AcceptThread();
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(bluetoothAdapter != null)
                {
                    // make a toast alert
                    CharSequence searchingMessage = "Searching for a nearby battle!";
                    Toast alertSearching = makeText(context, searchingMessage, LENGTH_SHORT);
                    alertSearching.show();

                    //hide the search button
                    searchButton.setVisibility(View.INVISIBLE);

                    // set animation of spinner
                    Animation rotation = AnimationUtils.loadAnimation(context, R.anim.rotate);
                    rotation.setRepeatCount(Animation.INFINITE);
                    spin.startAnimation(rotation);
                    spin.setVisibility(View.VISIBLE);

                    // show the cancel button
                    xButton.setVisibility(View.VISIBLE);

                    // look for devices
                    bluetoothAdapter.startDiscovery();
//                    updateBluetoothDevices(devicesAdapter);
                }
                else
                {
                    CharSequence cannotSearch = "No bluetooth means you cannot search :(";
                    Toast alertSearching = makeText(context, cannotSearch, LENGTH_SHORT);
                    alertSearching.show();
                }
            }
        });

        xButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                spin.clearAnimation();
                spin.setVisibility(View.INVISIBLE);
                xButton.setVisibility(View.INVISIBLE);
                searchButton.setVisibility(View.VISIBLE);
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String macAddress = deviceSpinner.getSelectedItem().toString().split("\n")[1];
                BluetoothDevice host = bluetoothAdapter.getRemoteDevice(macAddress);
                ConnectThread connectThread = new ConnectThread(host);
            }
        });
    }

    /**
     * Sends a request to pair with the selected device
     *
     * @param macAddress the mac address of the device to connect to
     * @throws Exception
     */
    public void connectToDevice(String macAddress) throws Exception {
        if (connected) {
            return;
        }
        BluetoothDevice device = bluetoothAdapter.
                getRemoteDevice(macAddress);
        Method m = device.getClass().getMethod("createRfcommSocket",
                new Class[] { int.class });
        sock = (BluetoothSocket) m.invoke(device, Integer.valueOf(1));
        sock.connect();
        in = sock.getInputStream();
        byte[] buffer = new byte[50];
        int read = 0;
//        try {
//            while (true) {
//                read = in.read(buffer);
//                connected = true;
//                StringBuilder buf = new StringBuilder();
//                for (int i = 0; i < read; i++) {
//                    int b = buffer[i] & 0xff;
//                    if (b < 0x10) {
//                        buf.append("0");
//                    }
//                    buf.append(Integer.toHexString(b)).append(" ");
//                }
//                Log.d("ZeeTest", "++++ Read "+ read +" bytes: "+ buf.toString());
//            }
//        } catch (IOException e) {}
    }

    /**
     * Updates the list of available bluetooth devices
     */
    public void updateBluetoothDevices(ArrayAdapter arrayAdapter)
    {
        if(arrayAdapter != null) {
            arrayAdapter.clear();
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            // If there are paired devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {
                    System.out.println(device.getName());
                    // Add the name and address to an array adapter to show in a ListView
                    if(device.getName().endsWith(NAME_SUFFIX)) {
                        devicesAdapter.add(device.getName() + "\n" + device.getAddress());
                    }
                }
            }
            deviceSpinner.setAdapter(devicesAdapter);
        }
    }

    /**
     * Set up the bluetooth for the device.
     *  - Check if bluetooth is supported
     *  - Enable bluetooth if it is not
     *  - Read any already paired devices
     *  - Register the BroadcastReceiver
     */
    public void setBluetooth()
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // set the name if it is not already set to identify app use
        if(!(bluetoothAdapter.getName().endsWith(NAME_SUFFIX)))
        {
            bluetoothAdapter.setName(bluetoothAdapter.getName().concat(NAME_SUFFIX));
        }

        if (bluetoothAdapter == null)
        {
            CharSequence noBluetoothMessage = "The device doesn't not support bluetooth. Sorry!";
            Toast alertNoBluetooth = makeText(context, noBluetoothMessage, LENGTH_SHORT);
            alertNoBluetooth.show();
        }
        else {
            devicesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            // enable bluetooth if it is not
            if (!bluetoothAdapter.isEnabled())
            {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            devicesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            // If there are paired devices
            if (pairedDevices.size() > 0)
            {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices)
                {
                    // Add the name and address to an array adapter to show in a ListView
                    if(device.getName().endsWith(NAME_SUFFIX)) {
                        devicesAdapter.add(device.getName() + "\n" + device.getAddress());
                    }
                }
            }
        }

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
//                    manageConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }
        }

        /**
         * Will cancel the listening socket, and cause the thread to finish
         */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
            }
        }
    }
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
//            manageConnectedSocket(mmSocket);
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}
