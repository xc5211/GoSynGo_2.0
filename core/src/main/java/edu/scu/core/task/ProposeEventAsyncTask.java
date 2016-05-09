package edu.scu.core.task;

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

    private AsyncCallback<List<Message>> memberMsgResponder;

    public ProposeEventAsyncTask(Api api, ActionCallbackListener listener, AsyncCallback<List<Message>> memberMsgResponder, Person hostPerson) {
        super(api, listener, hostPerson);
        this.memberMsgResponder = memberMsgResponder;
    }

    @Override
    protected ApiResponse<Event> doInBackground(Object... params) {

        EventLeaderDetail eventLeaderDetail = new EventLeaderDetail();
        eventLeaderDetail.setLeader(hostPerson);
        eventLeaderDetail.setIsCheckedIn(false);

        Event event = new Event();
        event.setStatusEvent(StatusEvent.Tentative.getStatus());
        event.setEventLeaderDetail(eventLeaderDetail);
        return api.proposeEvent(event);
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                Event event = (Event) response.getObj();
                String channelName = event.getObjectId();

                api.subscribeEventChannelAsLeader(channelName, hostPerson.getObjectId(), memberMsgResponder);
                hostPerson.addEventAsLeader(event);
                api.registerEventChannelMessaging(channelName);
                listener.onSuccess(event);
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
