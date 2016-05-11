package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.messaging.Message;

import java.util.List;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.R;
import edu.scu.model.Event;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.enumeration.StatusMember;

/**
 * Created by chuanxu on 5/5/16.
 */
public class AcceptEventAsyncTask extends BaseAsyncTask {

    private String eventId;
    private String memberId;
    private AsyncCallback<List<Message>> leaderMsgResponder;

    public AcceptEventAsyncTask(final Api api, final ActionCallbackListener<Boolean> listener, AsyncCallback<List<Message>> leaderMsgResponder, Handler handler, String eventId, String memberId) {
        super(api, listener, handler);
        this.eventId = eventId;
        this.memberId = memberId;
        this.leaderMsgResponder = leaderMsgResponder;
    }

    @Override
    protected ApiResponse<Event> doInBackground(Object... params) {
        return api.acceptEvent(eventId, memberId);
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                Event updatedEvent = (Event) response.getObj();

                api.subscribeEventChannelAsMember(eventId, memberId, leaderMsgResponder);
                String leaderId = updatedEvent.getEventLeaderDetail().getLeader().getObjectId();
                for (EventMemberDetail eventMemberDetail : updatedEvent.getEventMemberDetail()) {
                    if (eventMemberDetail.getMember().getObjectId().equals(memberId)) {
                        api.publishEventChannelMemberStatus(eventId, memberId, leaderId, StatusMember.Accept.getStatus());
                        break;
                    }
                }

                android.os.Message message = new android.os.Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Event.SERIALIZE_KEY, updatedEvent);
                message.setData(bundle);
                handler.sendMessage(message);
                listener.onSuccess(true);
                return;
            }
            listener.onFailure(String.valueOf(R.string.sync_with_server_error));
        } else {
            listener.onFailure(response.getMsg());
        }
    }

}
