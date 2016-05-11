package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.EventMemberDetail;

/**
 * Created by Hairong on 5/6/16.
 */
public class SetMinsToArriveAsMemberAsyncTask extends BaseAsyncTask{

    private EventMemberDetail eventMemberDetail;
    private String eventId;
    private String memberId;

    public SetMinsToArriveAsMemberAsyncTask(Api api, ActionCallbackListener<Integer> listener, Handler handler, EventMemberDetail eventMemberDetail, String eventId, String memberId) {
        super(api, listener, handler);
        this.eventMemberDetail = eventMemberDetail;
        this.eventId = eventId;
        this.memberId = memberId;
    }

    @Override
    protected ApiResponse<EventMemberDetail> doInBackground(Object... params) {
        return api.setMinsToArriveAsMember(eventMemberDetail);
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
                listener.onSuccess(updatedEventMemberDetail.getMinsToArrive());
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
