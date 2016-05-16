package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.R;
import edu.scu.model.Event;

/**
 * Created by chuanxu on 5/6/16.
 */
public class RemoveEventMemberAsyncTask extends BaseAsyncTask {

    private Event targetEvent;

    public RemoveEventMemberAsyncTask(Api api, ActionCallbackListener<Event> listener, Handler handler, Event targetEvent) {
        super(api, listener, handler);;
        this.targetEvent = targetEvent;
    }

    @Override
    protected ApiResponse<Event> doInBackground(Object... params) {
        return api.removeEventMember(targetEvent);
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
                listener.onSuccess(null);
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
