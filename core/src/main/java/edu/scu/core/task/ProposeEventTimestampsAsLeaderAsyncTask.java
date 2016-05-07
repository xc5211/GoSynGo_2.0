package edu.scu.core.task;

import java.util.List;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.R;
import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.LeaderProposedTimestamp;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/6/16.
 */
public class ProposeEventTimestampsAsLeaderAsyncTask extends BaseAsyncTask {

    private String eventId;
    private List<LeaderProposedTimestamp> proposedEventTimestamps;

    public ProposeEventTimestampsAsLeaderAsyncTask(Api api, ActionCallbackListener<EventLeaderDetail> listener, Person hostPerson, String eventId, List<LeaderProposedTimestamp> proposedEventTimestamps) {
        super(api, listener, hostPerson);
        this.eventId = eventId;
        this.proposedEventTimestamps = proposedEventTimestamps;
    }

    @Override
    protected ApiResponse<EventLeaderDetail> doInBackground(Object... params) {
        for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
            if (eventAsLeader.getObjectId().equals(eventId)) {
                EventLeaderDetail leaderDetail = eventAsLeader.getEventLeaderDetail();
                leaderDetail.setProposedTimestamps(proposedEventTimestamps);
                return api.proposeEventTimestampsAsLeader(leaderDetail);
            }
        }
        assert false;
        return null;
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                EventLeaderDetail updatedEventLeaderDetail = (EventLeaderDetail) response.getObj();
                for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
                    if(eventAsLeader.getObjectId().equals(eventId)) {
                        eventAsLeader.setEventLeaderDetail(updatedEventLeaderDetail);
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
