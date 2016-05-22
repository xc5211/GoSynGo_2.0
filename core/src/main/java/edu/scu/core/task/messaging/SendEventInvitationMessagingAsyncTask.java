package edu.scu.core.task.messaging;

import android.os.Handler;

import com.backendless.Subscription;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.messaging.Message;

import java.util.List;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.task.BaseAsyncTask;

/**
 * Created by chuanxu on 5/20/16.
 */
public class SendEventInvitationMessagingAsyncTask extends BaseAsyncTask {

    private String eventId;
    private String leaderId;
    private AsyncCallback<List<Message>> channelMsgResponderForLeader;
    private AsyncCallback<Subscription> subscriptionResponder;

    public SendEventInvitationMessagingAsyncTask(Api api, ActionCallbackListener listener, Handler handler, String eventId, String leaderId, AsyncCallback<List<Message>> channelMsgResponderForLeader, AsyncCallback<Subscription> subscriptionResponder) {
        super(api, listener, handler);
        this.eventId = eventId;
        this.leaderId = leaderId;
        this.channelMsgResponderForLeader = channelMsgResponderForLeader;
        this.subscriptionResponder = subscriptionResponder;
    }

    @Override
    protected ApiResponse doInBackground(Object... params) {
        //return api.subscribeEventChannel(eventId, leaderId, channelMsgResponderForLeader, subscriptionResponder);
        return null;
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        // nothing
    }
}
