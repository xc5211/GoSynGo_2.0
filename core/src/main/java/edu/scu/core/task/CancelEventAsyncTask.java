package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.Event;

/**
 * Created by chuanxu on 5/5/16.
 */
public class CancelEventAsyncTask extends BaseAsyncTask {

    private Event eventAsLeader;

    public CancelEventAsyncTask(final Api api, final ActionCallbackListener<Boolean> listener, final Handler handler, final Event eventAsLeader) {
        super(api, listener, handler);
        this.eventAsLeader = eventAsLeader;
    }

    @Override
    protected ApiResponse<Long> doInBackground(Object... params) {
        return api.removeEvent(eventAsLeader);
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                Long updatedEvent = (Long) response.getObj();
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Event.SERIALIZE_KEY, updatedEvent);
                message.setData(bundle);
                handler.sendMessage(message);
            } else {
                listener.onFailure(response.getMsg());
            }
        }

    }
}