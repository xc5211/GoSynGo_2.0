package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.Event;

/**
 * Created by chuanxu on 5/6/16.
 */
public class SendEventInvitationAsyncTask extends BaseAsyncTask {

    private Event event;

    public SendEventInvitationAsyncTask(Api api, ActionCallbackListener listener, Handler handler, Event event) {
        super(api, listener, handler);
        this.event = event;
    }

    @Override
    protected ApiResponse doInBackground(Object... params) {
        return api.sendEventInvitation(event);
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                Event updatedEvent = (Event) response.getObj();
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
