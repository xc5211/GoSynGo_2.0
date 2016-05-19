package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.EventLeaderDetail;
import edu.scu.model.EventMemberDetail;

/**
 * Created by chuanxu on 5/6/16.
 */
public class ProposeEventTimestampsAsLeaderAsyncTask extends BaseAsyncTask {

    private String eventId;
    private EventLeaderDetail leaderDetail;

    public ProposeEventTimestampsAsLeaderAsyncTask(Api api, ActionCallbackListener<EventLeaderDetail> listener, Handler handler, String eventId, EventLeaderDetail leaderDetail) {
        super(api, listener, handler);
        this.eventId = eventId;
        this.leaderDetail = leaderDetail;
    }

    @Override
    protected ApiResponse<EventLeaderDetail> doInBackground(Object... params) {
        return api.proposeEventTimestampsAsLeader(leaderDetail);
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                EventLeaderDetail updatedEventLeaderDetail = (EventLeaderDetail) response.getObj();
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable(EventLeaderDetail.SERIALIZE_KEY, updatedEventLeaderDetail);
                message.setData(bundle);
                handler.sendMessage(message);
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
