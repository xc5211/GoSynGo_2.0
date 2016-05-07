package edu.scu.core.task;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.R;
import edu.scu.model.Event;
import edu.scu.model.Person;
import edu.scu.model.StatusEvent;

/**
 * Created by chuanxu on 5/5/16.
 */
public class CancelEventAsyncTask extends BaseAsyncTask {

    private String eventId;

    public CancelEventAsyncTask(final Api api, final ActionCallbackListener<Integer> listener, final Person hostPerson, final String eventId) {
        super(api, listener, hostPerson);
        this.eventId = eventId;
    }

    @Override
    protected ApiResponse<Event> doInBackground(Object... params) {
        for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
            if (eventAsLeader.getObjectId().equals(eventId)) {
                eventAsLeader.setStatusEvent(StatusEvent.Cancelled.getStatus());
                return api.cancelEvent(eventAsLeader);
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
                if (updatedEvent.getStatusEvent() != StatusEvent.Cancelled.getStatus()) {
                    listener.onFailure(String.valueOf(R.string.sync_with_server_error));
                    return;
                }
                for (Event eventAsleader : hostPerson.getEventsAsLeader()) {
                    if(eventAsleader.getObjectId().equals(eventId)) {
                        eventAsleader.setStatusEvent(updatedEvent.getStatusEvent());
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