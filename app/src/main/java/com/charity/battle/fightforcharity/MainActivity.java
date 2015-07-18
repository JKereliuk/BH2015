package com.charity.battle.fightforcharity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import static android.widget.Toast.*;


public class MainActivity extends ActionBarActivity {

    private final int REQUEST_ENABLE_BT = 1;

    private Button searchButton;

    private TextView mainText;

    private ImageView brainTreeLogo;

    private ImageView spin;

    private Context context;

    private BluetoothAdapter bluetoothAdapter;

    ArrayAdapter<String> devicesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // set layout
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        setBluetooth();

        // set view variables
        searchButton  = (Button) findViewById(R.id.searchButton);
        mainText      = (TextView) findViewById(R.id.mainText);
        brainTreeLogo = (ImageView) findViewById(R.id.brainTree);
        spin          = (ImageView) findViewById(R.id.spin);

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(bluetoothAdapter != null)
                {
                    // Make a toast alert
                    CharSequence searchingMessage = "Searching for a nearby battle!";
                    Toast alertSearching = makeText(context, searchingMessage, LENGTH_SHORT);
                    alertSearching.show();
                    searchButton.setVisibility(View.INVISIBLE);

                    Animation rotation = AnimationUtils.loadAnimation(context, R.anim.rotate);
                    rotation.setRepeatCount(Animation.INFINITE);
                    spin.startAnimation(rotation);
                    spin.setVisibility(View.VISIBLE);
                }
                else
                {
                    CharSequence cannotSearch = "No bluetooth means you cannot search :(";
                    Toast alertSearching = makeText(context, cannotSearch, LENGTH_SHORT);
                    alertSearching.show();
                }
            }
        });
    }

    public void setBluetooth()
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            CharSequence noBluetoothMessage = "The device doesn't not support bluetooth. Sorry!";
            Toast alertNoBluetooth = makeText(context, noBluetoothMessage, LENGTH_SHORT);
            alertNoBluetooth.show();
        }
        else {
            devicesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            // enable bluetooth if it is not
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            // If there are paired devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {
                    // Add the name and address to an array adapter to show in a ListView
                    devicesAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        }
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
}
