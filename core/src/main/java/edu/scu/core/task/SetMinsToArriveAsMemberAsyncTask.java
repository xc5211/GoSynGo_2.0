package edu.scu.core.task;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.R;
import edu.scu.model.Event;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.Person;

/**
 * Created by Hairong on 5/6/16.
 */
public class SetMinsToArriveAsMemberAsyncTask extends BaseAsyncTask{

    private String eventId;
    private int estimateInMin;

    public SetMinsToArriveAsMemberAsyncTask(Api api, ActionCallbackListener<Integer> listener, Person hostPerson, String eventId, int estimateInMin) {
        super(api, listener, hostPerson);
        this.eventId = eventId;
        this.estimateInMin = estimateInMin;
    }

    @Override
    protected ApiResponse<EventMemberDetail> doInBackground(Object... params) {
        for (Event eventAsMember : hostPerson.getEventsAsMember()) {
            if (eventAsMember.getObjectId().equals(eventId)) {
                EventMemberDetail eventMemberDetail = eventAsMember.getEventMemberDetail().get(0);
                eventMemberDetail.setMinsToArrive(estimateInMin);
                return api.setMinsToArriveAsMember(eventMemberDetail);
            }
        }
        assert false;
        return null;
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                EventMemberDetail updatedEventMemberDetail = (EventMemberDetail) response.getObj();
                for (Event eventAsMember : hostPerson.getEventsAsMember()) {
                    if (eventAsMember.getObjectId().equals(eventId)) {
                        eventAsMember.updateEventMemberDetail(updatedEventMemberDetail);
                        listener.onSuccess(true);
                        return;
                    }
                }
                listener.onFailure(String.valueOf(R.string.sync_with_server_error));
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
