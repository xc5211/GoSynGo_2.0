package edu.scu.core.task;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.R;
import edu.scu.model.Event;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.Person;
import edu.scu.model.enumeration.StatusMember;

/**
 * Created by chuanxu on 5/5/16.
 */
public class DeclineEventAsyncTask extends BaseAsyncTask {

    private String eventId;

    public DeclineEventAsyncTask(final Api api, final ActionCallbackListener<Boolean> listener, Person hostPerson, String eventId) {
        super(api, listener, hostPerson);

        this.eventId = eventId;
    }

    @Override
    protected ApiResponse<EventMemberDetail> doInBackground(Object... params) {
        for (Event eventAsMember : hostPerson.getEventsAsMember()) {
            if (eventAsMember.getObjectId().equals(eventId)) {
                EventMemberDetail eventMemberDetail = eventAsMember.getEventMemberDetail().get(0);
                eventMemberDetail.setStatusMember(StatusMember.Declined.getStatus());
                return api.declineEvent(eventMemberDetail);
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
