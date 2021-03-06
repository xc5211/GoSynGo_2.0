package edu.scu.core.callback;

import android.util.Log;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;

import java.util.List;
import java.util.Map;

import edu.scu.model.EventUndecided;
import edu.scu.model.enumeration.BroadcastEventChannelArgKeyName;

/**
 * Created by chuanxu on 5/20/16.
 */
public class DefaultChannelMessageResponder implements AsyncCallback<List<Message>> {

    private static String selector;
    private List<EventUndecided> undecidedEventList;

    public DefaultChannelMessageResponder(List<EventUndecided> undecidedEventList, String hostPersonObjectId) {
        selector = "receiverId" + hostPersonObjectId.replace("-", "");
        this.undecidedEventList = undecidedEventList;
    }

    @Override
    public void handleResponse(List<Message> messages) {

        Map<String, String> msgHeader;
        Object msg;

        for (Message message : messages) {

            msgHeader = message.getHeaders();
            msg = message.getData();
            Map<String, Object> eventDataMap = ((Map<String, Object>) message.getData());
            assert msgHeader.get("sentFrom").equals("server");
            String receiverIdValue = msgHeader.get(selector);
            if (receiverIdValue == null) {
                continue;
            }
            assert receiverIdValue.equals("true");

            String title = (String) msgHeader.get(BroadcastEventChannelArgKeyName.EVENT_TITLE.getKeyName());
            String leaderName = (String) eventDataMap.get(BroadcastEventChannelArgKeyName.EVENT_LEADER_NAME.getKeyName());
            String eventId = (String) eventDataMap.get(BroadcastEventChannelArgKeyName.OBJECT_ID.getKeyName());
            String leaderId = (String) eventDataMap.get(BroadcastEventChannelArgKeyName.LEADER_ID.getKeyName());
            EventUndecided eventUndecided = new EventUndecided();
            eventUndecided.setTitle(title);
            eventUndecided.setLeaderName(leaderName);
            eventUndecided.setEventId(eventId);
            eventUndecided.setLeaderId(leaderId);
            undecidedEventList.add(eventUndecided);
        }
    }

    @Override
    public void handleFault(BackendlessFault backendlessFault) {
        Log.i("cxu", backendlessFault.getMessage());
    }

}
