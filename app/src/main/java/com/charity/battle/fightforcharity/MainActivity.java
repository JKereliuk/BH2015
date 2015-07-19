package com.charity.battle.fightforcharity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalService;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class MainActivity extends ActionBarActivity {

    private Button searchButton;
    private Button paypalButton;
    private TextView mainText;
    private ImageView brainTreeLogo;
    PrefsFragment mPrefsFragment = new PrefsFragment();
    public boolean settings_toggle = true;

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)

            .clientId("ARYcSz8eGomVVQ-TPSuqQmM61u_PsHiD8WmQ05jNLfUccTu6fmPHwwhtNF88m0Tt5IQYn47rJrCrCAN2")

            .merchantName("Donation Duel")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

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
        paypalButton = (Button) findViewById(R.id.config_paypal);

        usermanager = new UserManager(getApplicationContext());

        usermanager.checkLogin();

        paypalButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View pressed) {
                Intent intent = new Intent(MainActivity.this, PayPalFuturePaymentActivity.class);

                // send the same configuration for restart resiliency
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Context context = getApplicationContext();
                CharSequence text = "Searching for a nearby battle!";
                int duration = LENGTH_SHORT;

                Toast toast = makeText(context, text, duration);
                toast.show();

                getPref("donation_amount", getApplicationContext());

            }
        });

        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);
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

            if (settings_toggle) {
                FragmentTransaction mFragmentTransaction = mFragmentManager
                        .beginTransaction();

                mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
                mFragmentTransaction.commit();

            } else {
                mFragmentManager.beginTransaction().remove(mPrefsFragment).commit();
            }

            settings_toggle = !settings_toggle;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void putPref(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PayPalAuthorization auth = data
                    .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
            Log.d("TAG PLEASE TAG PLLZZZ", "POOOOOOOOP");
//            if (auth != null) {
//                try {
//                    String authorization_code = auth.getAuthorizationCode();
//
//                    sendAuthorizationToServer(auth);
//
//                } catch (JSONException e) {
//                    Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
//                }
//            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("FuturePaymentExample", "The user canceled.");
        } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("FuturePaymentExample",
                    "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
        }
    }
}
