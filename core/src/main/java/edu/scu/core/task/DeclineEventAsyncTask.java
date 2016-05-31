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

    public DeclineEventAsyncTask(final Api api, final ActionCallbackListener<Boolean> listener, Handler handler, String eventId, String memberId) {
        super(api, listener, handler);
        this.eventId = eventId;
        this.memberId = memberId;
    }

    @Override
    protected ApiResponse<EventMemberDetail> doInBackground(Object... params) {
        return api.declineEvent(eventId, memberId);
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                message.setData(bundle);
                handler.sendMessage(message);
                listener.onSuccess(true);
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
