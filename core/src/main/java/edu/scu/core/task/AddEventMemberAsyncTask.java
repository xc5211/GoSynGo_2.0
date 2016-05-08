package edu.scu.core.task;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.R;
import edu.scu.model.Event;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.Person;
import edu.scu.model.StatusEvent;

/**
 * Created by Hairong on 5/7/16.
 */
public class AddEventMemberAsyncTask extends BaseAsyncTask{

    private  String eventId;
    private  String memberEmail;

    public AddEventMemberAsyncTask(Api api, ActionCallbackListener<EventMemberDetail> listener, Person hostPerson, String eventId, String memberEmail) {
        super(api, listener, hostPerson);
        this.eventId = eventId;
        this.memberEmail = memberEmail;
    }

    @Override
    protected ApiResponse<EventMemberDetail> doInBackground(Object... params) {
        for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
            if (eventAsLeader.getObjectId().equals(eventId)) {
                return api.addEventMember(eventAsLeader, memberEmail);
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
                for (Event leaderEvent : hostPerson.getEventsAsLeader()) {
                    if (leaderEvent.getObjectId().equals(eventId)) {
                        leaderEvent.addEventMemberDetail(updatedEventMemberDetail);
                        listener.onSuccess(updatedEventMemberDetail);
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
