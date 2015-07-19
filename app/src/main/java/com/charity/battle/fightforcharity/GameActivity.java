package com.charity.battle.fightforcharity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Scanner;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class GameActivity extends ActionBarActivity {

    private ImageView rockImg;
    private ImageView paperImg;
    private ImageView axeImg;
    private TextView playAgainView;
    private TextView winView;
    private TextView tieView;
    private TextView loseView;
    private Button acceptButton;
    private Button declineButton;
    private Context context;
    private int move;
    private int result;
    private int otherMove; //this will be the response from the other user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        context = getApplicationContext();

        // set view variables
        rockImg       = (ImageView) findViewById(R.id.rock);
        paperImg      = (ImageView) findViewById(R.id.paper);
        axeImg        = (ImageView) findViewById(R.id.axe);
        playAgainView = (TextView)  findViewById(R.id.play_again);
        winView       = (TextView)  findViewById(R.id.win);
        tieView       = (TextView)  findViewById(R.id.tie);
        loseView      = (TextView)  findViewById(R.id.lose);
        acceptButton  = (Button)    findViewById(R.id.accept);
        declineButton = (Button)    findViewById(R.id.decline);
        move = 0;
        otherMove = 1; // for testing

        rockImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                move = 1;
                //send move to other device
                result = rockPaperAxe(move, otherMove);
                //call paypal transactions here
                replay(result);
            }
        });
        paperImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                move = 2;
                //send move to other device
                result = rockPaperAxe(move, otherMove);
                //call paypal transaction here
                replay(result);
            }
        });
        axeImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                move = 3;
                //send move to other device
                result = rockPaperAxe(move, otherMove);
                //call paypal transaction here
                replay(result);
            }
        });
    }


    public int rockPaperAxe(int move, int otherMove) {
        if (move == otherMove) { return 0;}
        switch (move) {
            case 1:
                return (otherMove == 3 ? 1 : -1);
            case 2:
                return (otherMove == 1 ? 1 : -1);
            case 3:
                return (otherMove == 2 ? 1 : -1);
        }

        //should never get here
        return 0;
    }

    public void replay(int result) {

        rockImg.setVisibility(View.INVISIBLE);
        paperImg.setVisibility(View.INVISIBLE);
        axeImg.setVisibility(View.INVISIBLE);
        playAgainView.setVisibility(View.VISIBLE);
        acceptButton.setVisibility(View.VISIBLE);
        declineButton.setVisibility(View.VISIBLE);

        if (result == 1) { winView.setVisibility(View.VISIBLE); }
        else if (result == 0) { tieView.setVisibility(View.VISIBLE); }
        else{ loseView.setVisibility(View.VISIBLE); }

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
