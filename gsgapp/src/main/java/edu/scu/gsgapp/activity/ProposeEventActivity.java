package edu.scu.gsgapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import edu.scu.core.ActionCallbackListener;
import edu.scu.gsgapp.R;
import edu.scu.model.Event;

/**
 * Created by chuanxu on 5/16/16.
 */
public class ProposeEventActivity extends GsgBaseActivity {

    private String eventId;

    private EditText titleEdit;
    private EditText locationEdit;
    private EditText noteEdit;
    private CheckBox reminderCheckBox;
    private EditText reminderEdit;
    private EditText durationEdit;
    private Button pickDayButton;
    private Button pickHoursButton;
    private EditText emailEdit;
    private Button addMemberButton;
    private GridView memberShowGridView;
    private Button sendInvitationButton;
    private Button cancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose_event);

        eventId = (String) savedInstanceState.get("eventId");

        initViews();
        initListener();
    }



    protected void initViews() {
        this.titleEdit = (EditText) findViewById(R.id.edit_text_propose_event_title);
        this.locationEdit = (EditText) findViewById(R.id.edit_text_propose_event_location);
        this.noteEdit = (EditText) findViewById(R.id.edit_text_propose_event_note);
        this.reminderCheckBox = (CheckBox) findViewById(R.id.checkbox_propose_event_reminder);
        this.reminderEdit = (EditText) findViewById(R.id.edit_text_propose_event_reminder);
        this.durationEdit = (EditText) findViewById(R.id.edit_text_propose_event_duration);
        this.pickDayButton = (Button) findViewById(R.id.button_propose_event_pick_day);
        this.pickHoursButton = (Button) findViewById(R.id.button_propose_event_pick_hours);
        this.emailEdit = (EditText) findViewById(R.id.edit_text_propose_event_add_member_email);
        this.addMemberButton =(Button) findViewById(R.id.button_propose_event_add_member);
        this.sendInvitationButton = (Button) findViewById(R.id.button_propose_event_send_invitation);
        this.cancelButton = (Button) findViewById(R.id.button_propose_event_cancel);
    }

    protected void initListener() {

        this.reminderCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasReminder = reminderCheckBox.isChecked();
                if (hasReminder) {
                    reminderEdit.setEnabled(true);
                } else {
                    reminderEdit.setEnabled(false);
                }
            }
        });

        this.pickHoursButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String title = titleEdit.getText().toString();
                String location = locationEdit.getText().toString();
                int durationInMin = Integer.parseInt(durationEdit.getText().toString());
                boolean hasReminder = reminderCheckBox.isChecked();
                int reminderInMin = Integer.parseInt(reminderEdit.getText().toString());
                String note = noteEdit.getText().toString();

                getAppAction().addEventInformation(eventId,title,location,durationInMin,hasReminder,reminderInMin,note, new ActionCallbackListener<Event>() {
                    @Override
                    public void onSuccess(Event data) {
                        Toast.makeText(context, "Add event information successfully!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(context, "Add event information failed!", Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(v.getContext(), LeaderProposeEventTimeActivity.class);
                startActivityForResult(intent,0);
            }
        });

        this.pickHoursButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), LeaderProposeEventTimeActivity.class);
                startActivityForResult(intent,0);
            }
        });

        this.addMemberButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //evnetId
                String memberEmail = emailEdit.getText().toString();

                final ProgressDialog progressDialog = ProgressDialog.show( ProposeEventActivity.this, "", "Adding member...", true );
                getAppAction().addEventMember(eventId, memberEmail, new ActionCallbackListener<Event>() {
                    @Override
                    public void onSuccess(Event data) {
                        // notify dataset change to adapter
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(context, "This member does not exist.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        this.sendInvitationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendInvitation();
            }
        });

        this.cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getAppAction().cancelEvent(eventId, new ActionCallbackListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        if (data == true) {
                            Toast.makeText(context, "Cancel successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProposeEventActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(context, "Cancel failed, please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }


    protected void sendInvitation() {

        final ProgressDialog progressDialog = ProgressDialog.show( ProposeEventActivity.this, "", "Inviting...", true );
        super.appAction.sendEventInvitation(eventId,new ActionCallbackListener<Event>() {
            @Override
            public void onSuccess(Event data) {
                Toast.makeText(context, "Send invitation successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProposeEventActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                progressDialog.cancel();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                progressDialog.cancel();

            }
        });
    }

}
