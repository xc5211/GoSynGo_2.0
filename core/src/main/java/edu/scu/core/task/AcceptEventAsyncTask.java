package edu.scu.core.task;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.messaging.Message;

import java.util.List;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.R;
import edu.scu.model.Event;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/5/16.
 */
public class AcceptEventAsyncTask extends BaseAsyncTask {

    private String eventId;
    private AsyncCallback<List<Message>> leaderMsgResponder;

    public AcceptEventAsyncTask(final Api api, final ActionCallbackListener<Boolean> listener, AsyncCallback<List<Message>> leaderMsgResponder, Person hostPerson, String eventId) {
        super(api, listener, hostPerson);
        this.eventId = eventId;
        this.leaderMsgResponder = leaderMsgResponder;
    }

    @Override
    protected ApiResponse<Event> doInBackground(Object... params) {
        return api.acceptEvent(eventId, hostPerson.getObjectId());
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                Event updatedEvent = (Event) response.getObj();
                hostPerson.getEventsAsMember().add(updatedEvent);

                api.subscribeEventChannelAsMember(eventId, hostPerson.getObjectId(), leaderMsgResponder);
                String leaderId = updatedEvent.getEventLeaderDetail().getLeader().getObjectId();
                api.publishEventChannelMessageAsMember(eventId, hostPerson.getObjectId(), leaderId);
                listener.onSuccess(true);
                return;
            }
            listener.onFailure(String.valueOf(R.string.sync_with_server_error));
        } else {
            listener.onFailure(response.getMsg());
        }
    }

}
