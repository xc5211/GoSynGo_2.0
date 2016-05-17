package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.EventMemberDetail;

/**
 * Created by chuanxu on 5/16/16.
 */
public class SelectEventTimestampsAsMemberAsyncTask extends BaseAsyncTask {

    private String eventId;
    private String memberId;
    private String leaderId;
    private EventMemberDetail eventMemberDetail;

    public SelectEventTimestampsAsMemberAsyncTask(Api api, ActionCallbackListener<EventMemberDetail> listener, Handler handler, String eventId, String memberId, String leaderId, EventMemberDetail eventMemberDetail) {
        super(api, listener, handler);
        this.eventId = eventId;
        this.memberId = memberId;
        this.leaderId = leaderId;
        this.eventMemberDetail = eventMemberDetail;
    }

    @Override
    protected ApiResponse<EventMemberDetail> doInBackground(Object... params) {
        return api.selectEventTimestampsAsMember(eventMemberDetail);
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                EventMemberDetail updatedEventMemberDetail = (EventMemberDetail) response.getObj();
                api.publishEventChannelMemberSelectedTimestamps(eventId, memberId, leaderId, updatedEventMemberDetail.getSelectedTimestamps());

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable(EventMemberDetail.SERIALIZE_KEY, updatedEventMemberDetail);
                message.setData(bundle);
                handler.sendMessage(message);
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
