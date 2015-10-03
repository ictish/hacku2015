package com.hacku.kuse.hacku2015;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sensormanager.SensorManager;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements Runnable {
    private BluetoothAdapter bluetoothAdapter;
    private SensorManager nekozeManager;
    private SensorManager binboManager;
    private final int REQUEST_ENABLE_BT = 1;

    private Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        ImageButton btnNekoze = (ImageButton) findViewById(R.id.btnNekoze);
        btnNekoze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainPopUp.class));
            }
        });

        ImageButton btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        ImageButton btnGraph = (ImageButton) findViewById(R.id.btnGraph);
        btnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Graph.class));
            }
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null){
            Toast.makeText(this, "Bluetooth is not supported!", Toast.LENGTH_SHORT);
            finish();
            return;
        }

        if(!bluetoothAdapter.isEnabled()){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableIntent);
        }else{
            connectSensor();
        }

        thread = new Thread(this);
        thread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case REQUEST_ENABLE_BT :
                if(resultCode == Activity.RESULT_OK){
                    connectSensor();
                }else{
                    Toast.makeText(this, "can't enable Bluetooth", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void connectSensor(){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 1) {
            for (BluetoothDevice device : pairedDevices) {
                if(device.getName().equals("KUSE-0")){
                    binboManager = new SensorManager(device);
                    binboManager.start();
                }else if(device.getName().equals("KUSE-1")){
                    nekozeManager = new SensorManager(device);
                    nekozeManager.start();
                }
            }
        }
    }

    @Override
    public void run(){
        int binboPoint = 0;
        int nekozePoint = 0;
        while(true){
            if(binboManager.isNew()) {
                binboPoint = (binboManager.getCurrentVal()) * 9 / 50; // 0 ~ 255
                if(binboPoint > 9) binboPoint = 9;
                Log.d("LOG", "binboPoint : " + String.valueOf(binboPoint));
                binboHandler.sendMessage(Message.obtain(binboHandler, binboPoint));
                if(binboPoint > 3)
                    characterHandler.sendMessage(Message.obtain(characterHandler, 1));
                else
                    characterHandler.sendMessage(Message.obtain(characterHandler, 0));
            }

            if(nekozeManager.isNew()) {
                nekozePoint = (nekozeManager.getCurrentVal()) * 9 / 80;
                if(nekozePoint > 9) nekozePoint = 9;
                Log.d("LOG", "nekozePoint : " + String.valueOf(nekozePoint));
                nekozeHandler.sendMessage(Message.obtain(nekozeHandler, nekozePoint));
            }
        }
    }

    private Handler binboHandler = new Handler(){
        public void handleMessage(Message msg){
            ImageButton btnBinbo = (ImageButton) findViewById(R.id.btnBinbo);
            switch(msg.what){
                case 0:
                    btnBinbo.setImageResource(R.drawable.icon_binbo0);
                    break;
                case 1:
                    btnBinbo.setImageResource(R.drawable.icon_binbo1);
                    break;
                case 2:
                    btnBinbo.setImageResource(R.drawable.icon_binbo2);
                    break;
                case 3:
                    btnBinbo.setImageResource(R.drawable.icon_binbo3);
                    break;
                case 4:
                    btnBinbo.setImageResource(R.drawable.icon_binbo4);
                    break;
                case 5:
                    btnBinbo.setImageResource(R.drawable.icon_binbo5);
                    break;
                case 6:
                    btnBinbo.setImageResource(R.drawable.icon_binbo6);
                    break;
                case 7:
                    btnBinbo.setImageResource(R.drawable.icon_binbo7);
                    break;
                case 8:
                    btnBinbo.setImageResource(R.drawable.icon_binbo8);
                    break;
                case 9:
                    btnBinbo.setImageResource(R.drawable.icon_binbo9);
                    break;
            }
        }
    };

    private Handler nekozeHandler = new Handler(){
        public void handleMessage(Message msg){
            ImageButton btnNekoze = (ImageButton) findViewById(R.id.btnNekoze);
            switch(msg.what){
                case 0:
                    btnNekoze.setImageResource(R.drawable.icon_nekoze0);
                    break;
                case 1:
                    btnNekoze.setImageResource(R.drawable.icon_nekoze1);
                    break;
                case 2:
                    btnNekoze.setImageResource(R.drawable.icon_nekoze2);
                    break;
                case 3:
                    btnNekoze.setImageResource(R.drawable.icon_nekoze3);
                    break;
                case 4:
                    btnNekoze.setImageResource(R.drawable.icon_nekoze4);
                    break;
                case 5:
                    btnNekoze.setImageResource(R.drawable.icon_nekoze5);
                    break;
                case 6:
                    btnNekoze.setImageResource(R.drawable.icon_nekoze6);
                    break;
                case 7:
                    btnNekoze.setImageResource(R.drawable.icon_nekoze7);
                    break;
                case 8:
                    btnNekoze.setImageResource(R.drawable.icon_nekoze8);
                    break;
                case 9:
                    btnNekoze.setImageResource(R.drawable.icon_nekoze9);
                    break;
            }
        }
    };

    private Handler characterHandler = new Handler(){
        public void handleMessage(Message msg){
            ImageView characterImage = (ImageView) findViewById(R.id.logo);
            switch (msg.what){
                case 0:
                    characterImage.setBackgroundResource(R.drawable.human);
                    break;
                case 1:
                    characterImage.setBackgroundResource(R.drawable.binbo);
                    break;
                case 2:
                    characterImage.setBackgroundResource(R.drawable.nekoze2);
                    break;
                case 3:
                    characterImage.setBackgroundResource(R.drawable.nekoze3);
                    break;
                case 4:
                    characterImage.setBackgroundResource(R.drawable.nail);
                    break;
            }
        }
    };
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
