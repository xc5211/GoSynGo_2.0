package edu.scu.core.task;

import android.os.Handler;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.messaging.Message;

import java.util.List;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.Person;
import edu.scu.model.enumeration.StatusEvent;

/**
 * Created by chuanxu on 5/6/16.
 */
public class ProposeEventAsyncTask extends BaseAsyncTask {

    private String leaderId;
    private Event event;
    private AsyncCallback<List<Message>> memberMsgResponder;

    public ProposeEventAsyncTask(Api api, ActionCallbackListener listener, AsyncCallback<List<Message>> memberMsgResponder, String leaderId, Event event, Handler handler) {
        super(api, listener, handler);
        this.leaderId = leaderId;
        this.event = event;
        this.memberMsgResponder = memberMsgResponder;
    }

    @Override
    protected ApiResponse<Event> doInBackground(Object... params) {
        return api.proposeEvent(event);
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                Event updatedEvent = (Event) response.getObj();
                String channelName = updatedEvent.getObjectId();

                api.subscribeEventChannelAsLeader(channelName, leaderId, memberMsgResponder);
                api.registerEventChannelMessaging(channelName);
                listener.onSuccess(updatedEvent);
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
