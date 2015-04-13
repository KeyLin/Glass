package edu.shu.shu_glass;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    private Button openBlueToothBtn;
    private Button readSMSBtn;

    public static boolean isBlueTeethOpened = false;

    private BlueToothService mBlueToothService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openBlueToothBtn = (Button)findViewById(R.id.openBlueToothBtn);
        openBlueToothBtn.setOnClickListener(listener);


        readSMSBtn = (Button)findViewById(R.id.readSMSBtn);
        readSMSBtn.setOnClickListener(readSMSListener);
    }

    //---------* Get SMS from phone *---------------------------
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

    //------------* end *----------------------------

    private OnClickListener readSMSListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = getSmsFromPhone();
        }
    };


    private OnClickListener listener = new OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(MainActivity.this,BlueToothActivity.class);
            startActivity(intent);
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
