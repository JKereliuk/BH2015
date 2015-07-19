package com.charity.battle.fightforcharity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class MainActivity extends ActionBarActivity {

    private Button searchButton;
    private TextView mainText;
    private ImageView brainTreeLogo;

    UserManager usermanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set layout
        setContentView(R.layout.activity_main);

        // set view variables
        searchButton = (Button) findViewById(R.id.searchButton);
        mainText = (TextView) findViewById(R.id.mainText);
        brainTreeLogo = (ImageView) findViewById(R.id.brainTree);

        usermanager = new UserManager(getApplicationContext());

        usermanager.checkLogin();

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Context context = getApplicationContext();
                CharSequence text = "Searching for a nearby battle!";
                int duration = LENGTH_SHORT;

                Toast toast = makeText(context, text, duration);
                toast.show();



            }
        });

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
            FragmentManager mFragmentManager = getFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager
                    .beginTransaction();
            PrefsFragment mPrefsFragment = new PrefsFragment();
            mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
            mFragmentTransaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }
}
