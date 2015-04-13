package edu.shu.shu_glass;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by simon on 4/10/15.
 */
public class ConnectedThread extends Thread{

    private static BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final String TAG = "BluetoothError";
    private final Handler smsHandler;
    private static final int GET_MESSAGE=1;

    public ConnectedThread(BluetoothSocket socket,Handler smsHandler) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        this.smsHandler = smsHandler;

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
                String cmd = "";
                String resource = "";

                Log.i(TAG,t);
                try{
                    JSONTokener jsonParser = new JSONTokener(t);
                    JSONObject js = (JSONObject)jsonParser.nextValue();
                    cmd = js.getString("cmd");
                    resource = js.getString("resource");

                }catch (JSONException je){
                    Log.i(TAG,je.toString());
                }

                if(cmd.equals("get")){

                    if(resource.equals("sms")){
                        Message msg = new Message();
                        msg.what = GET_MESSAGE;
                        this.smsHandler.sendMessage(msg);
                        //String msg = getSmsFromPhone();
                        //buffer = msg.getBytes();
                        //mmOutStream.write(buffer);

                        bytes = mmInStream.read(buffer);
                        String confirm = new String(buffer);


                        if (confirm.substring(0,2).equals("ok")){
                            mmOutStream.write(buffer);
                        }
                    }
                }

                //clean up this array
                buffer = new byte[1024];
                //write();
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
