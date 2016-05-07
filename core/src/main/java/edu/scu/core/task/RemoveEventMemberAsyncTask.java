package edu.scu.core.task;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.R;
import edu.scu.model.Event;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/6/16.
 */
public class RemoveEventMemberAsyncTask extends BaseAsyncTask {

    private String eventId;
    private String memberId;

    public RemoveEventMemberAsyncTask(Api api, ActionCallbackListener<Event> listener, Person hostPerson, String eventId, String memberId) {
        super(api, listener, hostPerson);
        this.eventId = eventId;
        this.memberId = memberId;
    }

    @Override
    protected ApiResponse<Event> doInBackground(Object... params) {
        Event targetEvent = null;
        for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
            if (eventAsLeader.getObjectId().equals(eventId)) {
                targetEvent = eventAsLeader;
                break;
            }
        }
        assert targetEvent != null;

        for (EventMemberDetail memberDetail : targetEvent.getEventMemberDetail()) {
            if (memberDetail.getObjectId().equals(memberId)) {
                // TODO: Check if event needs to be removed from member eventsAsMember list
                targetEvent.getEventMemberDetail().remove(memberDetail);
                return api.removeEventMember(targetEvent);
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
                for (Event eventAsleader : hostPerson.getEventsAsLeader()) {
                    if(eventAsleader.getObjectId().equals(eventId)) {
                        // TODO: Check next line
                        eventAsleader.getEventMemberDetail().remove(updatedEvent.getEventMemberDetail().get(0));
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
