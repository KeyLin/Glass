package edu.shu.shu_glass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

public class MessageInTime extends BroadcastReceiver{

    private static final String TAG = "MessageInTime";
    private static final String strACT = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context,Intent intent){

        if(intent.getAction().equals(strACT)){
            StringBuilder sb = new StringBuilder();
            Bundle bundle = intent.getExtras();

            if(bundle != null){

                Log.i(TAG,"BUNDLE"+bundle.get("pdus"));
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] msg = new SmsMessage[pdus.length];

                for(int i=0;i<pdus.length;i++)
                {
                   msg[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }

                for(SmsMessage currMsg:msg){
                    sb.append("From:");
                    sb.append(currMsg.getDisplayOriginatingAddress());
                    sb.append("\nMessage:");
                    sb.append(currMsg.getDisplayMessageBody());
                }

                //把控制权还给上一个activity
                Intent i = new Intent(context, ReadSMSActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

        }

    }


}
