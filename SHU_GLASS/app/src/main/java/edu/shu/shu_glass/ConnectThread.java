package edu.shu.shu_glass;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;
/**
 * Created by simon on 4/10/15.
 */
public class ConnectThread extends Thread{

    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mmAdapter;
    private static final String TAG = "BluetoothConnectError";
    private static final int CONNECT_COMPLETE = 1;

    private Handler handler;

    public ConnectThread(BluetoothDevice device,BluetoothAdapter adapter,Handler handler) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;
        mmAdapter = adapter;
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code

            //test uuid
            UUID MY_UUID = UUID.fromString("e8587008-297a-4676-9fc6-cc8ee6fa097c");
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.i(TAG,e.toString());
        }
        mmSocket = tmp;

        this.handler = handler;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        //mmAdapter.cancelDiscovery();
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            Log.i(TAG,connectException.toString());
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.i(TAG,closeException.toString());
            }
            return;
        }

        Message msg = new Message();
        msg.what = CONNECT_COMPLETE;
        this.handler.sendMessage(msg);

    }


    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

    public BluetoothSocket getSocket(){
        return this.mmSocket;
    }

}
