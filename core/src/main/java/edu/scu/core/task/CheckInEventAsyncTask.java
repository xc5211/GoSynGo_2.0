package edu.scu.core.task;

import android.os.Handler;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.R;
import edu.scu.model.Event;
import edu.scu.model.EventMemberDetail;

/**
 * Created by chuanxu on 5/6/16.
 */
public class CheckInEventAsyncTask extends BaseAsyncTask {

    private String eventId;

    public CheckInEventAsyncTask(final Api api, final ActionCallbackListener<Boolean> listener, final Handler handler, final String eventId) {
        super(api, listener, handler);
        this.eventId = eventId;
    }

    @Override
    protected ApiResponse<EventMemberDetail> doInBackground(Object... params) {
//        for (Event eventAsMember : hostPerson.getEventsAsMember()) {
//            if (eventAsMember.getObjectId().equals(eventId)) {
//                EventMemberDetail eventMemberDetail = eventAsMember.getEventMemberDetail().get(0);
//                eventMemberDetail.setIsCheckedIn(true);
//                return api.checkInEvent(eventMemberDetail);
//            }
//        }
//        assert false;
        return null;
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
//                EventMemberDetail updatedEventMemberDetail = (EventMemberDetail) response.getObj();
//                if (updatedEventMemberDetail.getIsCheckedIn() != true) {
//                    listener.onFailure(String.valueOf(R.string.sync_with_server_error));
//                    return;
//                }
//                for (Event eventAsMember : hostPerson.getEventsAsMember()) {
//                    if(eventAsMember.getObjectId().equals(eventId)) {
//                        eventAsMember.updateEventMemberDetail(updatedEventMemberDetail);
//                        listener.onSuccess(true);
//                        return;
//                    }
//                }
            } else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
