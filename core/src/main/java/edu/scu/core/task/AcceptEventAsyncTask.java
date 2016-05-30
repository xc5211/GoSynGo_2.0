package edu.scu.core.task;

import android.os.Bundle;
import android.os.Handler;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/5/16.
 */
public class AcceptEventAsyncTask extends BaseAsyncTask {

    private Person baseMember;
    private String eventId;
    private String memberId;

    public AcceptEventAsyncTask(final Api api, final ActionCallbackListener<Boolean> listener, Handler handler, Person baseMember, String eventId, String memberId) {
        super(api, listener, handler);
        this.baseMember = baseMember;
        this.eventId = eventId;
        this.memberId = memberId;
    }

    @Override
    protected ApiResponse<Person> doInBackground(Object... params) {
        return api.acceptEvent(baseMember, eventId, memberId);
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                Person updatedPerson = (Person) response.getObj();

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
