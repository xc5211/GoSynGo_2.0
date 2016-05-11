package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.EventMemberDetail;

/**
 * Created by chuanxu on 5/5/16.
 */
public class DeclineEventAsyncTask extends BaseAsyncTask {

    private String eventId;
    private String memberId;
    private EventMemberDetail eventMemberDetail;

    public DeclineEventAsyncTask(final Api api, final ActionCallbackListener<Boolean> listener, Handler handler, String eventId, String memberId, EventMemberDetail eventMemberDetail) {
        super(api, listener, handler);
        this.eventId = eventId;
        this.memberId = memberId;
        this.eventMemberDetail = eventMemberDetail;
    }

    @Override
    protected ApiResponse<EventMemberDetail> doInBackground(Object... params) {
        return api.declineEvent(eventMemberDetail);
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                EventMemberDetail updatedEventMemberDetail = (EventMemberDetail) response.getObj();
                String leaderId = updatedEventMemberDetail.getLeaderId();
                api.publishEventChannelMemberEstimateInMin(eventId, memberId, leaderId, updatedEventMemberDetail.getMinsToArrive());

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable(EventMemberDetail.SERIALIZE_KEY, updatedEventMemberDetail);
                message.setData(bundle);
                handler.sendMessage(message);
                listener.onSuccess(true);
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
