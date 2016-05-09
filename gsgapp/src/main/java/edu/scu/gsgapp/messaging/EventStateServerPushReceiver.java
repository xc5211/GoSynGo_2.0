package edu.scu.gsgapp.messaging;

import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.backendless.messaging.PublishOptions;
import com.backendless.push.BackendlessBroadcastReceiver;

/**
 * Created by chuanxu on 5/8/16.
 */
public class EventStateServerPushReceiver extends BackendlessBroadcastReceiver {

    @Override
    public boolean onMessage(Context context, Intent intent) {

        String eventId = intent.getStringExtra("eventId");
        String leaderId = intent.getStringExtra("leaderId");


        Message message = new Message();
        message.obj = intent.getStringExtra(PublishOptions.MESSAGE_TAG);


        return super.onMessage(context, intent);
    }

}
