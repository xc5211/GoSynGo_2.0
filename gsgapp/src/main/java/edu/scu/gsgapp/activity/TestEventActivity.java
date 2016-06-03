package edu.scu.gsgapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.scu.core.ActionCallbackListener;
import edu.scu.gsgapp.R;
import edu.scu.model.Event;
import edu.scu.model.EventLeaderDetail;

/**
 * Created by chuanxu on 5/19/16.
 */
public class TestEventActivity extends GsgBaseActivity {

    private Button proposeButton;
    private Button addMemberButton;
    private Button syncButton;
    private Button addEventInfoButton;
    private Button leaderProposeTimeButton;
    private Button sendEventInvitationButton;
    private Button cancelEventButton;

    private static String eventId = "61B5F3F8-6A3A-577F-FF6D-AA41F09CE100";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_event);

        initView();
        initListener();
    }

    private void initView() {
        proposeButton = (Button) findViewById(R.id.button_propose);
        addMemberButton = (Button) findViewById(R.id.button_add_member);
        syncButton = (Button) findViewById(R.id.button_sync);
        addEventInfoButton = (Button) findViewById(R.id.button_add_event_info);
        leaderProposeTimeButton = (Button) findViewById(R.id.button_leader_propose_time);
        sendEventInvitationButton = (Button) findViewById(R.id.button_send_invitation);
        cancelEventButton = (Button) findViewById(R.id.button_cancel_event);
    }

    private void initListener() {

        proposeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appAction.proposeEvent(new ActionCallbackListener<Event>() {
                    @Override
                    public void onSuccess(Event data) {
                        Toast.makeText(getApplicationContext(), "Propose event success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getApplicationContext(), "Propose event fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventId = "61B5F3F8-6A3A-577F-FF6D-AA41F09CE100";
                String memberEmail = "test2@gmail.com";
                appAction.addEventMember(eventId, memberEmail, new ActionCallbackListener<Event>() {
                    @Override
                    public void onSuccess(Event data) {
                        Toast.makeText(getApplicationContext(), "Add member success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getApplicationContext(), "Add member fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appAction.syncHostInformation(getAppAction().getHostUserId(), new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        Log.i("cxu", "1");
                    }

                    @Override
                    public void onFailure(String message) {
                        Log.i("cxu", "0");
                    }
                });
            }
        });

        addEventInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = "Test Event";
                String location = null;
                int durationInMin = 30;
                boolean hasReminder = true;
                int reminderInMin = 30;
                String note = null;

//                appAction.addEventInformation(eventId, title, location, durationInMin, hasReminder, reminderInMin, note, new ActionCallbackListener<Event>() {
//
//                    @Override
//                    public void onSuccess(Event data) {
//                        Toast.makeText(getApplicationContext(), "Add event info success", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(String message) {
//                        Toast.makeText(getApplicationContext(), "Add event info fail", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });

        leaderProposeTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> proposedEventTimestamps = new ArrayList<>();
                proposedEventTimestamps.add("2016/05/20 8:00:00");
                proposedEventTimestamps.add("2016/05/20 8:15:00");

                appAction.proposeEventTimestampsAsLeader(eventId, proposedEventTimestamps, new ActionCallbackListener<EventLeaderDetail>() {
                    @Override
                    public void onSuccess(EventLeaderDetail data) {
                        Toast.makeText(getApplicationContext(), "Leader propose time success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getApplicationContext(), "Leader propose time fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        sendEventInvitationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                appAction.sendEventInvitation(eventId, new ActionCallbackListener<Event>() {
//                    @Override
//                    public void onSuccess(Event data) {
//                        Toast.makeText(getApplicationContext(), "Send invitation success", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(String message) {
//                        Toast.makeText(getApplicationContext(), "Send invitation fail", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });

        cancelEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appAction.cancelEvent("eventId", new ActionCallbackListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        Toast.makeText(getApplicationContext(), "Cancel event success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getApplicationContext(), "Cancel event fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}
