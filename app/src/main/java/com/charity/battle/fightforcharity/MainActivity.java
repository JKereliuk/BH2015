package com.charity.battle.fightforcharity;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.*;


public class MainActivity extends ActionBarActivity {

    private Button searchButton;

    private TextView mainText;

    private ImageView brainTreeLogo;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // set layout
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        // set view variables
        searchButton  = (Button) findViewById(R.id.searchButton);
        mainText      = (TextView) findViewById(R.id.mainText);
        brainTreeLogo = (ImageView) findViewById(R.id.brainTree);

        searchButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // Make a toast alert
                CharSequence text = "Searching for a nearby battle!";
                Toast toast = makeText(context, text, LENGTH_SHORT);
                toast.show();
                searchButton.setVisibility(View.INVISIBLE);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
