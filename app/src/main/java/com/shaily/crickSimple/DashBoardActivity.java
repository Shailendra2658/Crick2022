package com.shaily.crickSimple;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.shaily.crickSimple.databinding.ActivityMainBinding;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class DashBoardActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE = 1;
    public static final int REQUEST_ENABLE_BT = 2;

    private ActivityMainBinding binding;
    private static final int CODE_CAM_PER = 101;
    private static final String CODE_STORAGE_PER = "STORAGE";
    // Local Bluetooth adapter
    //  private BluetoothAdapter mBluetoothAdapter = null;
    private PowerManager.WakeLock mWakeLock;
    public ProgressDialog progress;
    BluetoothSocket btSocket = null;
    int counter;
    /* access modifiers changed from: private */
    public boolean isBtConnected = false;
    InputStream mmInputStream;
    BluetoothAdapter myBluetooth = null;
    String address = null;
    StringBuffer strBuf = new StringBuffer();
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg_type) {
            super.handleMessage(msg_type);
            int i = msg_type.what;
            if (i == 0) {
                Toast.makeText(DashBoardActivity.this.getApplicationContext(), (((String) msg_type.obj).trim()), Toast.LENGTH_SHORT).show();
            } else if (i == 1) {
                Object obj = msg_type.obj;
            } else if (i == 2) {
                Toast.makeText(DashBoardActivity.this.getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();
            } else if (i == 3) {
                Toast.makeText(DashBoardActivity.this.getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
            } else if (i == 4) {
                Toast.makeText(DashBoardActivity.this.getApplicationContext(), "No socket found", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private String[] permissionList = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Bluetooth connections
        //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());
        mWakeLock.acquire();

        // If the adapter is null, then Bluetooth is not supported
        /*if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }*/

        binding.start.setOnClickListener(view -> {
            //checkForPermission(permissionList)
            if (checkForPermission(permissionList))
                sendData(2);
            // Permission is granted. Continue the action or workflow in your
            // app.
            //startActivity(new Intent(DashBoardActivity.this, SelectBowler.class));
            ;
        });

        binding.setting.setOnClickListener(view -> {
            startActivity(new Intent(DashBoardActivity.this, DeviceListActivity.class));

        });
    }


    @Override
    public void onStart() {
        super.onStart();
        /*if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            } else {
                //if (mChatService == null) setupChat();
            }
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.address = getIntent().getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        address = (!TextUtils.isEmpty(address) ? address : DeviceListActivity.ADDRESS);
        Log.d(TAG, "onResume " + address);

        if (!TextUtils.isEmpty(address)) {
            new ConnectBT().execute(new Void[0]);

        }
    }

    private void ensureDiscoverable() {
        if (myBluetooth.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                CODE_CAM_PER);
    }

    private boolean checkForPermission(String... permissionList) {
        for (String permission : permissionList) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, CODE_CAM_PER);
                // requestPermissionLauncher.launch(permission);
                return false;
            }
        }
        return true;
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            //performAction(...);
            Intent localIntent = new Intent(this, SelectBowler.class);
            startActivity(localIntent);
        } *//*else if (shouldShowRequestPermissionRationale(...)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            //showInContextUI(...);
        }*//* else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODE_CAM_PER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DashBoardActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Intent localIntent = new Intent(this, SelectBowler.class);
                    startActivity(localIntent);
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Toast.makeText(this, "Cant use this app without giving permission of storage. Please grant permission to proceed.", Toast.LENGTH_LONG).show();
                }
            });

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess;

        private ConnectBT() {
            this.ConnectSuccess = true;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            DashBoardActivity dashBoardActivity = DashBoardActivity.this;
            ProgressDialog unused = dashBoardActivity.progress = ProgressDialog.show(dashBoardActivity, "Connecting...", "Please wait!!!");
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... devices) {
            try {
                Log.d(TAG, "doInBackground");

                if (DashBoardActivity.this.btSocket != null && DashBoardActivity.this.isBtConnected) {
                    return null;
                }
                myBluetooth = BluetoothAdapter.getDefaultAdapter();
                if (myBluetooth != null) {
                    Log.d(TAG, "myBluetooth");

                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    DashBoardActivity.this.btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(DashBoardActivity.myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    DashBoardActivity.this.btSocket.connect();
                } else
                    this.ConnectSuccess = false;
                return null;
            } catch (IOException e) {
                this.ConnectSuccess = false;
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!this.ConnectSuccess) {
                DashBoardActivity.this.msg("Connection Failed. Is it a SPP Bluetooth? Try again.", 1);
                //DashBoardActivity.this.finish();
            } else {
                DashBoardActivity.this.msg("Connected.", 1);
                boolean unused = DashBoardActivity.this.isBtConnected = true;
                //DashBoardActivity.this.statusMsg.setText("Bluetooth Opened");
                if (btSocket != null)
                    new ConnectedThread(btSocket).start();
                else
                    msg("Failed to Connect", 1);
            }
            DashBoardActivity.this.progress.dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void msg(String s, int toastLength) {
        Toast toast2 = new Toast(this);
        if (toast2 != null) {
            toast2.cancel();
        }
        if (s.length() > 1) {
            Toast makeText = Toast.makeText(getApplicationContext(), s, toastLength);
            makeText.show();
        }
    }

    public void sendData(int i) {
        if (this.btSocket != null) {
            try {
                //enableAll(false);
                this.btSocket.getOutputStream().write(i);
            } catch (IOException e) {
                msg("Please wait", 0);
            }
        } else
            msg("Not connected", 0);
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final BluetoothSocket mmSocket;

        public ConnectedThread(BluetoothSocket socket) {
            this.mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.mmInStream = tmpIn;
            this.mmOutStream = tmpOut;
        }

        public void run() {
            while (!isInterrupted()) {
                try {
                    try {
                        Log.i(TAG, "Read from the InputStream...");
                        InputStream inputStream = this.mmInStream;
                        if (inputStream == null) {
                            Log.e(TAG, "Lost bluetooth connection!");
                            return;
                        } else if (inputStream.available() > 0) {
                            byte[] buffer = new byte[1024];
                            int bytes = this.mmInStream.read(buffer);
                            Log.i(DashBoardActivity.TAG, "Read from the InputStream, length is " + bytes);
                            String string_recieved = new String(buffer, "ASCII").substring(0, bytes);
                            if (string_recieved.contains("*")) {
                                DashBoardActivity.this.strBuf.append(string_recieved.substring(0, string_recieved.indexOf("*")));
                                DashBoardActivity.this.mHandler.obtainMessage(0, bytes, -1, DashBoardActivity.this.strBuf.toString().replace("*", "").trim()).sendToTarget();
                                String pending = string_recieved.substring(string_recieved.indexOf("*"));
                                DashBoardActivity.this.strBuf = new StringBuffer();
                                DashBoardActivity.this.strBuf.append(pending);
                                msg( "Final str is: "+pending, 0);
                                msg( "Buffer str is: "+strBuf.toString(), 0);
                            } else {
                                DashBoardActivity.this.strBuf.append(string_recieved);
                                msg( string_recieved, 0);

                            }
                        }
                    } catch (IOException e) {
                        Log.e(DashBoardActivity.TAG, "disconnected", e);
                        return;
                    }
                } catch (Exception e2) {
                    Log.e(DashBoardActivity.TAG, "disconnected", e2);
                    return;
                }
            }
        }

        public void write(byte[] buffer) {
        }
    }


}