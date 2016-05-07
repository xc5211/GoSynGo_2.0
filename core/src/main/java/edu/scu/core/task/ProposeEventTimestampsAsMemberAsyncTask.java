package edu.scu.core.task;

import java.util.List;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.R;
import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.MemberProposedTimestamp;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/6/16.
 */
public class ProposeEventTimestampsAsMemberAsyncTask extends BaseAsyncTask {

    private String eventId;
    private List<MemberProposedTimestamp> proposedEventTimestamps;

    public ProposeEventTimestampsAsMemberAsyncTask(Api api, ActionCallbackListener<EventMemberDetail> listener, Person hostPerson, String eventId, List<MemberProposedTimestamp> proposedEventTimestamps) {
        super(api, listener, hostPerson);
        this.eventId = eventId;
        this.proposedEventTimestamps = proposedEventTimestamps;
    }

    @Override
    protected ApiResponse<EventMemberDetail> doInBackground(Object... params) {
        for (Event eventAsMember : hostPerson.getEventsAsMember()) {
            if (eventAsMember.getObjectId().equals(eventId)) {
                EventMemberDetail memberDetail = eventAsMember.getEventMemberDetail().get(0);
                memberDetail.setProposedTimestamps(proposedEventTimestamps);
                return api.proposeEventTimestampsAsMember(memberDetail);
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
                for (Event eventAsMember : hostPerson.getEventsAsLeader()) {
                    if(eventAsMember.getObjectId().equals(eventId)) {
                        eventAsMember.updateEventMemberDetail(updatedEventMemberDetail);
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
