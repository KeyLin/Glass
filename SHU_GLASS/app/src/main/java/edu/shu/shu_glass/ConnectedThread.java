package edu.shu.shu_glass;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by simon on 4/10/15.
 */
public class ConnectedThread extends Thread{

    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final String TAG = "BluetoothError";
    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.i(TAG,e.toString());
        }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                // Send the obtained bytes to the UI activity
//                mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
//                        .sendToTarget();
                String t = new String(buffer);
                //clean up this array
                buffer = new byte[1024];

                //write();
                Log.i(TAG,t);
            } catch (IOException e) {
                break;
            }
        }
    }


    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.i(TAG,e.toString());
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.i(TAG,e.toString());
        }
    }
}
