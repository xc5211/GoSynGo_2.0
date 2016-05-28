package edu.scu.core.callback;

import android.util.Log;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;

import java.util.List;
import java.util.Map;

import edu.scu.model.Event;
import edu.scu.model.EventUndecided;

/**
 * Created by chuanxu on 5/20/16.
 */
public class DefaultChannelMessageResponder implements AsyncCallback<List<Message>> {

    private List<EventUndecided> undecidedEventList;

    public DefaultChannelMessageResponder(List<EventUndecided> undecidedEventList) {
        this.undecidedEventList = undecidedEventList;
    }

    @Override
    public void handleResponse(List<Message> messages) {

        for (Message message : messages) {
            Map<String, Object> eventDataMap = ((Map<String, Object>) message.getData());

            Event undecidedEvent = new Event();
            undecidedEvent.setTitle((String) eventDataMap.get("title"));
            undecidedEvent.setNote((String) eventDataMap.get("note"));
            undecidedEvent.setLocation((String) eventDataMap.get("location"));
            undecidedEvent.setDurationInMin((Integer) eventDataMap.get("durationInMin"));
            undecidedEvent.setStatusEvent((Integer) eventDataMap.get("statusEvent"));
            undecidedEvent.setHasReminder((Boolean) eventDataMap.get("hasReminder"));
            undecidedEvent.setReminderInMin((Integer) eventDataMap.get("reminderInMin"));
            undecidedEvent.setObjectId((String) eventDataMap.get("objectId"));
            undecidedEvent.setOwnerId((String) eventDataMap.get("ownerId"));
            undecidedEventList.add(new EventUndecided((String) eventDataMap.get("title"), "leader name", (String) eventDataMap.get("objectId")));
        }
    }

    @Override
    public void handleFault(BackendlessFault backendlessFault) {
        Log.i("cxu", backendlessFault.getMessage());
    }

}
