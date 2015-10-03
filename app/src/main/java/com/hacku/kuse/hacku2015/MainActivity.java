package com.hacku.kuse.hacku2015;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private SensorManager nekozeManager;
    private SensorManager binboManager;
    private final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        ImageButton btnBinbo = (ImageButton) findViewById(R.id.btnBinbo);
        btnBinbo.setOnClickListener(new View.OnClickListener() {
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

        int binboPoint = 0;
        Drawable binboImage = (android.os.Build.VERSION.SDK_INT > 20) ?
                getResources().getDrawable(R.drawable.icon_binbo, null) : getResources().getDrawable(R.drawable.icon_binbo);
       while(true){
         /* // if(binboManager.isNew()) {
            //    binboPoint = (binboManager.getCurrentVal()) / 170 * 9; // 0 ~ 255
            //}

            switch(binboPoint) {
                case 0:
                    binboImage = (android.os.Build.VERSION.SDK_INT > 20) ?
                            getResources().getDrawable(R.drawable.icon_binbo, null) : getResources().getDrawable(R.drawable.icon_binbo);
                    break;
                case 1:
                    binboImage = (android.os.Build.VERSION.SDK_INT > 20) ?
                            getResources().getDrawable(R.drawable.icon_binbo1, null) : getResources().getDrawable(R.drawable.icon_binbo1);
                    break;
                case 2:
                    binboImage = (android.os.Build.VERSION.SDK_INT > 20) ?
                            getResources().getDrawable(R.drawable.icon_binbo2, null) : getResources().getDrawable(R.drawable.icon_binbo2);
                    break;
                case 3:
                    binboImage = (android.os.Build.VERSION.SDK_INT > 20) ?
                            getResources().getDrawable(R.drawable.icon_binbo3, null) : getResources().getDrawable(R.drawable.icon_binbo3);
                    break;
                case 4:
                    binboImage = (android.os.Build.VERSION.SDK_INT > 20) ?
                            getResources().getDrawable(R.drawable.icon_binbo4, null) : getResources().getDrawable(R.drawable.icon_binbo4);
                    break;
                case 5:
                    binboImage = (android.os.Build.VERSION.SDK_INT > 20) ?
                            getResources().getDrawable(R.drawable.icon_binbo5, null) : getResources().getDrawable(R.drawable.icon_binbo5);
                    break;
                case 6:
                    binboImage = (android.os.Build.VERSION.SDK_INT > 20) ?
                            getResources().getDrawable(R.drawable.icon_binbo6, null) : getResources().getDrawable(R.drawable.icon_binbo6);
                    break;
                case 7:
                    binboImage = (android.os.Build.VERSION.SDK_INT > 20) ?
                            getResources().getDrawable(R.drawable.icon_binbo7, null) : getResources().getDrawable(R.drawable.icon_binbo7);
                    break;
                case 8:
                    binboImage = (android.os.Build.VERSION.SDK_INT > 20) ?
                            getResources().getDrawable(R.drawable.icon_binbo8, null) : getResources().getDrawable(R.drawable.icon_binbo8);
                    break;
                case 9:
                    binboImage = (android.os.Build.VERSION.SDK_INT > 20) ?
                            getResources().getDrawable(R.drawable.icon_binbo9, null) : getResources().getDrawable(R.drawable.icon_binbo9);
                    break;
            }*/
            btnBinbo.setBackground(binboImage);
        }
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
