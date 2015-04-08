package edu.shu.shu_glass;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


public class BlueToothService extends Service {

    private static final String TAG = "BlueToothService";

    public static final int BLUETOOTHSERVICE_MSG_TAG = 13721049;
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";

    private ServerThread mServerThread = null;
    private BluetoothServerSocket mServerSocket = null;
    private DataReadThread dataReadThread = null;
    // 获取蓝牙网卡
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket socket = null;



    private Messenger cMessager = null;


    public class BlueToothBinder extends Binder {

        BlueToothService getService(){
            return BlueToothService.this;
        }
    }

    public BlueToothService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        Log.i(TAG,"MessengerService.onBind()....");
        return mMessager.getBinder();

    }



    @Override
    public void onCreate(){
        super.onCreate();

        Log.i(TAG, "MessengerService.onCreate()...pid: " + Process.myPid());

        mServerThread = new ServerThread();
        mServerThread.start();
        MainActivity.isBlueTeethOpened = true;
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BLUETOOTHSERVICE_MSG_TAG:
                    Log.e(TAG, "Get Message from MainActivity.");
                    cMessager = msg.replyTo;
                    break;
                default:
                    break;

            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "MessageService.onDestory()...");
        shutdownServer();
    }


    private Messenger mMessager = new Messenger(mHandler);
    // 开启服务器
    private class ServerThread extends Thread {
        public void run() {
            try {
                 /*
                 * 创建一个蓝牙服务器 参数分别：服务器名称、UUID
                 */
                // UUID uuid = UUID.randomUUID();

                mServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(
                        PROTOCOL_SCHEME_RFCOMM,
                        UUID.fromString("e8587008-297a-4676-9fc6-cc8ee6fa097c"));

                Log.d("server", "wait cilent connect...");


                Message msg = new Message();

                msg.obj = "请稍候，正在等待客户端的连接...";
                msg.what = 0;
                Log.i(TAG, "请稍候，正在等待客户端的连接...");

                 /* 接受客户端的连接请求 */
                socket = mServerSocket.accept();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private class DataReadThread extends Thread {

        public void run() {

            byte[] buffer = new byte[1024];

            int bytes;

            InputStream mmInStream = null;

            try {
                mmInStream = socket.getInputStream();

            } catch (IOException e1) {
                e1.printStackTrace();
            }

            while (true) {
                try {
                    if ((bytes = mmInStream.read(buffer)) > 0) {
                        byte[] buf_data = new byte[bytes];
                        for (int i = 0; i < bytes; i++) {
                            buf_data[i] = buffer[i];
                        }
                        String s = new String(buf_data);
                        Log.i(TAG, "接受到了:" + s);

                        Message msg = new Message();
                        msg.obj = s;
                        msg.what = 1;
                    }
                } catch (IOException e) {
                    try {
                        mmInStream.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                }
            }


        }
    }


        /* 停止服务器 */
        public void shutdownServer() {
            new Thread() {
                public void run() {
                    if (mServerThread != null) {
                        mServerThread.interrupt();
                        mServerThread = null;
                    }
                    if (dataReadThread != null) {
                        dataReadThread.interrupt();
                        dataReadThread = null;
                    }
                    try {
                        if (socket != null) {
                            socket.close();
                            socket = null;
                        }
                        if (mServerSocket != null) {
                            mServerSocket.close();/* 关闭服务器 */
                            mServerSocket = null;
                        }
                    } catch (IOException e) {
                        Log.e("server", "mserverSocket.close()", e);
                    }
                }
            }.start();
        }
}
