package edu.shu.shu_glass;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class BlueToothCommuniteActivity extends ActionBarActivity {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothReceiver receiver;
    private List<String> devices;
    private List<BluetoothDevice> deviceList;
    private BluetoothSocket clientSocket;
    private final String lockName = "BOLUTEK";

    private String message = "000001";

    private ListView lv;

    private ConnectThread connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth_communite);


//        listView = (ListView) this.findViewById(R.id.list);
        deviceList = new ArrayList<BluetoothDevice>();
        devices = new ArrayList<String>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startDiscovery();

        // dynamic create Intent filter

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        receiver = new BluetoothReceiver();
        registerReceiver(receiver,filter);


        lv = (ListView)findViewById(R.id.connect_layout);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?>parent,View view,int postion,long id){

               // setContentView(lv);
                BluetoothDevice device = deviceList.get(postion);

                // test uuid :
                connect = new ConnectThread(device,bluetoothAdapter,handler);
                connect.start();
                //ConnectedThread rasp = new ConnectedThread(connect.getSocket());
                //rasp.start();

            }
        });
    }

    private Uri SMS_INBOX = Uri.parse("content://sms/");
    //    final String SMS_URI_ALL   = "content://sms/";
//    final String SMS_URI_SEND  = "content://sms/sent";
//    final String SMS_URI_DRAFT = "content://sms/draft";
    public String getSmsFromPhone() {

        StringBuilder smsBuilder = new StringBuilder();

        ContentResolver cr = getContentResolver();

        String[] projection = new String[]{"_id", "address", "person",
                "body", "date", "type"};
        String where = " address = '18717907831' ";
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur)
            return null;

        if (cur.moveToFirst()) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号

            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));

            smsBuilder.append(number + name + body);

        }
        return smsBuilder.toString();
    }


    private static final int GET_MESSAGE=1;
    private final Handler smsHandler = new Handler(){
        public void handleMessage(Message msg){
            if (msg.what == GET_MESSAGE){
                String sms = getSmsFromPhone();
                ConnectedThread send_msg = new ConnectedThread(connect.getSocket(),smsHandler);
                send_msg.write(sms.getBytes());


            }
        }
    };


    private static final int CONNECT_COMPLETE = 1;
    private final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            if( msg.what == CONNECT_COMPLETE){
                ConnectedThread rasp = new ConnectedThread(connect.getSocket(),smsHandler);
                rasp.start();
            }
        }
    };


    @Override
    protected void onDestroy(){
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blue_tooth_communite, menu);
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


    private class BluetoothReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context , Intent intent){
            String action = intent.getAction();

            if( BluetoothDevice.ACTION_FOUND.equals(action) ){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

//                if( isLock(device) ){
                    devices.add(device.getName());
//                }
                deviceList.add(device);
            }
            showDevices();
        }
    }

    private boolean isLock(BluetoothDevice device) {
        boolean isLockName = (device.getName()).equals(lockName);
        boolean isSingleDevice = devices.indexOf(device.getName()) == -1;
        return isLockName && isSingleDevice;
    }

    private void showDevices() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                devices);
        lv.setAdapter(adapter);
    }
}
