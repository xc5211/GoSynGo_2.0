package edu.scu.gsgapp.activity;

import android.content.Intent;
import android.os.Bundle;

import edu.scu.gsgapp.R;

/**
 * Created by chuanxu on 5/26/16.
 */
public class EventDetailActivity extends GsgBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String eventId = intent.getStringExtra("eventId");
        String eventState = intent.getStringExtra("eventState");
        String hostRole = isEventLeader(eventId) ? "leader" : "member";

        String eventDetailProperty = hostRole + eventState;
        switch (eventDetailProperty) {
            case "leaderNotReady":
                setContentView(R.layout.activity_event_detail_not_ready_leader);
                initWidgetable();
                initListener();
                break;
            case "memberNotReady":
                setContentView(R.layout.activity_event_detail_not_ready_member);
                initWidgetable();
                initListener();
                break;
            default:    // "leaderReady" || "memberReady"
                setContentView(R.layout.activity_event_detail_ready);
                initWidgetable();
                initListener();
                break;
        }
    }

    private boolean isEventLeader(String eventId) {
        boolean isEventLeader = hostPerson.getEventAsLeader(eventId) != null;
        return isEventLeader;
    }

    private void initWidgetable() {

    }

    private void initListener() {

    }

}
