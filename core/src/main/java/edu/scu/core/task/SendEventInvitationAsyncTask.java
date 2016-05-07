package edu.scu.core.task;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.R;
import edu.scu.model.Event;
import edu.scu.model.Person;
import edu.scu.model.StatusEvent;

/**
 * Created by chuanxu on 5/6/16.
 */
public class SendEventInvitationAsyncTask extends BaseAsyncTask {

    private String eventId;

    public SendEventInvitationAsyncTask(Api api, ActionCallbackListener listener, Person hostPerson, String eventId) {
        super(api, listener, hostPerson);
        this.eventId = eventId;
    }

    @Override
    protected ApiResponse doInBackground(Object... params) {
        for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
            if (eventAsLeader.getObjectId().equals(eventId)) {
                eventAsLeader.setStatusEvent(StatusEvent.Pending.getStatus());
                return api.initiateEvent(eventAsLeader);
            }
        }
        assert false;
        return null;
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                Event updatedEvent = (Event) response.getObj();
                for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
                    if(eventAsLeader.getObjectId().equals(eventId)) {
                        eventAsLeader.setStatusEvent(updatedEvent.getStatusEvent());
                        listener.onSuccess(true);
                        return;
                    }
                }
                listener.onFailure(String.valueOf(R.string.sync_with_server_error));
            }else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
