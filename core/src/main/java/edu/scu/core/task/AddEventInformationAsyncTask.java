package edu.scu.core.task;

import edu.scu.api.Api;
import edu.scu.api.ApiResponse;
import edu.scu.core.ActionCallbackListener;
import edu.scu.core.R;
import edu.scu.model.Event;
import edu.scu.model.Person;

/**
 * Created by chuanxu on 5/6/16.
 */
public class AddEventInformationAsyncTask extends BaseAsyncTask {

    private String eventId;
    private String title;
    private String location;
    private int durationInMin;
    private boolean hasReminder;
    private int reminderInMin;

    public AddEventInformationAsyncTask(Api api, ActionCallbackListener<Event> listener, Person hostPerson, String eventId, String title, String location, int durationInMin, boolean hasReminder, int reminderInMin) {
        super(api, listener, hostPerson);
        this.eventId = eventId;
        this.title = title;
        this.location = location;
        this.durationInMin = durationInMin;
        this.hasReminder = hasReminder;
        this.reminderInMin = reminderInMin;
    }

    @Override
    protected ApiResponse<Event> doInBackground(Object... params) {
        for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
            if (eventAsLeader.getObjectId().equals(eventId)) {
                eventAsLeader.setTitle(title);
                eventAsLeader.setLocation(location);
                eventAsLeader.setDurationInMin(durationInMin);
                eventAsLeader.setHasReminder(hasReminder);
                eventAsLeader.setReminderInMin(reminderInMin);
                return api.initiateEvent(eventAsLeader);
            }
        }
        assert false;
        return null;
    }

    @Override
    protected void onPostExecute(ApiResponse response) {
        if (listener != null && response != null) {
            if (response.isSuccess()) {
                Event updatedEvent = (Event) response.getObj();
                for (Event eventAsLeader : hostPerson.getEventsAsLeader()) {
                    if(eventAsLeader.getObjectId().equals(eventId)) {
                        eventAsLeader.setTitle(updatedEvent.getTitle());
                        eventAsLeader.setLocation(updatedEvent.getLocation());
                        eventAsLeader.setDurationInMin(updatedEvent.getDurationInMin());
                        eventAsLeader.setHasReminder(updatedEvent.getHasReminder());
                        eventAsLeader.setReminderInMin(updatedEvent.getReminderInMin());
                        listener.onSuccess(true);
                        return;
                    }
                }
                listener.onFailure(String.valueOf(R.string.sync_with_server_error));
            }else {
                listener.onFailure(response.getMsg());
            }
        }
    }

}
