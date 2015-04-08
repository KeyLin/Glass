package edu.shu.shu_glass;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ReadMessages extends Service {
    public ReadMessages() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
