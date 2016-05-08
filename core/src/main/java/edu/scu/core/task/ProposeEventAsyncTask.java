package edu.scu.core.task;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.Person;
import edu.scu.model.StatusEvent;

/**
 * Created by chuanxu on 5/6/16.
 */
public class ProposeEventAsyncTask extends BaseAsyncTask {

    public ProposeEventAsyncTask(Api api, ActionCallbackListener listener, Person hostPerson) {
        super(api, listener, hostPerson);
    }

    @Override
    protected ApiResponse<Event> doInBackground(Object... params) {
        // Propose new event
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
                api.initNewChannel(event.getObjectId());
                listener.onSuccess(event);
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }
}
