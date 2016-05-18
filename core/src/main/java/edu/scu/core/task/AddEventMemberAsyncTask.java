package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.Event;
import edu.scu.model.EventMemberDetail;

/**
 * Created by Hairong on 5/7/16.
 */
public class AddEventMemberAsyncTask extends BaseAsyncTask{

    private String eventId;
    private String memberEmail;
    private Event event;
    private String leaderId;

    public AddEventMemberAsyncTask(Api api, ActionCallbackListener<Event> listener, Handler handler, String eventId, String leaderId, String memberEmail, Event event) {
        super(api, listener, handler);
        this.eventId = eventId;
        this.memberEmail = memberEmail;
        this.event = event;
        this.leaderId = leaderId;
    }

    @Override
    protected ApiResponse<Event> doInBackground(Object... params) {
        return api.addEventMember(event, leaderId, memberEmail);
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
