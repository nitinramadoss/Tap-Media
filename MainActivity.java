package com.tm.nitinr.tapmedia;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {
    NfcAdapter nfcAdapter;
    EditText et,et2,et3,et4;
    FloatingActionButton fab;
    RadioButton cbIg, cbSc, cbFb, cbT;
    String message, ig, sc, fb, t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        et = findViewById(R.id.etig);
        et2 = findViewById(R.id.etsc);
        et3 = findViewById(R.id.etfb);
        et4 = findViewById(R.id.ett);
        cbIg = findViewById(R.id.cbig);
        cbSc = findViewById(R.id.cbsc);
        cbFb = findViewById(R.id.cbfb);
        cbT = findViewById(R.id.cbt);
        fab=  findViewById(R.id.fab);

        message = "";
        ig = "";
        sc = "";
        fb = "";
        t = "";

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String sig = sp.getString("ig", "");
        String ssc = sp.getString("sc", "");
        String sfb = sp.getString("fb", "");
        String st = sp.getString("t", "");

        et.setText(sig);
        et2.setText(ssc);
        et3.setText(sfb);
        et4.setText(st);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(MainActivity.this, "Sorry this device does not have NFC.", Toast.LENGTH_LONG).show();
            return;
        } else if (nfcAdapter != null){

            // Register callback to set NDEF message
            nfcAdapter.setNdefPushMessageCallback(this, this);

            // Register callback to listen for message-sent success
            nfcAdapter.setOnNdefPushCompleteCallback((NfcAdapter.OnNdefPushCompleteCallback) this, this);
        }

        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(MainActivity.this, "Please enable NFC in Settings.", Toast.LENGTH_LONG).show();
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                if(!cbIg.isChecked() && !cbFb.isChecked() && !cbSc.isChecked() && !cbT.isChecked())
                    builder.setMessage("Please select the information to send");
                else {
                    builder.setMessage("Place phones back to back to transfer media");
                }
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });



    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {

        return new NdefMessage(new NdefRecord[] {
                NdefRecord.createUri(message)
        });
    }

    @Override
    public void onNdefPushComplete(NfcEvent arg0) {

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
        if (id == R.id.info) {
            Intent openInfo = new Intent(MainActivity.this, InformationActivity.class);
            startActivity(openInfo);
        } else if (id == R.id.reset){
            Intent i = getIntent();
            finish();
            startActivity(i);
        } else if(id == R.id.save_menu){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor editor = preferences.edit();

            //saving information to local storage on cellphone
            ig = et.getText().toString();
            sc = et2.getText().toString();
            fb = et3.getText().toString();
            t = et4.getText().toString();
            editor.putString("ig", ig);
            editor.putString("sc", sc);
            editor.putString("fb", fb);
            editor.putString("t", t);
            editor.apply();

            Toast.makeText(MainActivity.this, "Saving media...", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkBox(View v) {

        switch (v.getId()) {
            case R.id.cbig:
                    message += "\n" + "Instagram: " + et.getText().toString() + "\n";
                break;
            case R.id.cbsc:
                    message += "\n" + "Snap Chat: " + et2.getText().toString() + "\n";
                break;
            case R.id.cbfb:
                    message += "\n" + "FaceBook: " + et3.getText().toString() + "\n";
                break;
            case R.id.cbt:
                    message += "\n" + "Twitter: " + et4.getText().toString() + "\n";
                break;

        }

        }
    }


