package edu.scu.core.task.messaging;


import com.backendless.Subscription;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.messaging.Message;

import java.util.List;

/**
 * Created by chuanxu on 5/21/16.
 */
public class ChannelSubscription {

    private AsyncCallback<List<Message>> channelMsgResponder;
    private Subscription subscription;

    public ChannelSubscription(AsyncCallback<List<Message>> channelMsgResponder, Subscription subscription) {
        this.channelMsgResponder = channelMsgResponder;
        this.subscription = subscription;
    }

    public AsyncCallback<List<Message>> getChannelMessageResponder() {
        return channelMsgResponder;
    }

    public Subscription getSubscription() {
        return subscription;
    }

}
