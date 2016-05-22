package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.messaging.Message;

import java.util.List;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.Event;
import edu.scu.model.EventMemberDetail;
import edu.scu.model.Person;
import edu.scu.model.enumeration.StatusMember;

/**
 * Created by chuanxu on 5/5/16.
 */
public class AcceptEventAsyncTask extends BaseAsyncTask {

    private Person baseMember;
    private Event undecidedEvent;
    private String memberId;

    public AcceptEventAsyncTask(final Api api, final ActionCallbackListener<Boolean> listener, Handler handler, Person baseMember, Event undecidedEvent, String memberId) {
        super(api, listener, handler);
        this.baseMember = baseMember;
        this.undecidedEvent = undecidedEvent;
        this.memberId = memberId;
    }

    @Override
    protected ApiResponse<Person> doInBackground(Object... params) {
        return api.acceptEvent(baseMember, undecidedEvent, memberId);
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                Person updatedPerson = (Person) response.getObj();

//                String leaderId = updatedEvent.getEventLeaderDetail().getLeader().getObjectId();
//                for (EventMemberDetail eventMemberDetail : updatedEvent.getEventMemberDetail()) {
//                    if (eventMemberDetail.getMember().getObjectId().equals(memberId)) {
//                        api.publishEventChannelMemberStatus(undecidedEvent.getObjectId(), memberId, leaderId, StatusMember.Accept.getStatus());
//                        break;
//                    }
//                }

                android.os.Message message = new android.os.Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Person.SERIALIZE_KEY, updatedPerson);
                message.setData(bundle);
                handler.sendMessage(message);
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
