package edu.scu.gsgapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.scu.core.ActionCallbackListener;
import edu.scu.gsgapp.R;
import edu.scu.gsgapp.adapter.propose.ProposeEventAddMemberAdapter;
import edu.scu.model.Event;

/**
 * Created by chuanxu on 5/16/16.
 */
public class ProposeEventActivity extends GsgBaseActivity {

    private String eventId;

    private EditText titleEdit;
    private EditText locationEdit;
    private EditText noteEdit;
    private Spinner durationSpinner;
    private Spinner reminderSpinner;
    private DatePicker datePicker;
    private EditText emailEdit;
    private Button addMemberButton;
    private GridView memberShowGridView;
    private Button sendInvitationButton;
    private Button cancelButton;

    //sichao
    GridView gridView;;
    public static int numNames = 0;
    ProposeEventAddMemberAdapter proposeEventAddMemberAdapter;
    List<String> personNames= new ArrayList<String>();
    public final static int LIST_LENGTH = 100;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose_event);

        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventId");

        initViews();
        initGridView();
        initListener();
    }

    protected void initViews() {
        this.titleEdit = (EditText) findViewById(R.id.edit_text_propose_event_title);
        this.locationEdit = (EditText) findViewById(R.id.edit_text_propose_event_location);
        this.noteEdit = (EditText) findViewById(R.id.edit_text_propose_event_note);

        this.durationSpinner = (Spinner) findViewById(R.id.spinner_propose_event_duration);
        Integer[] durations = { 30, 45, 60, 90, 120};
        ArrayAdapter<Integer> durationSpinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, durations);
        durationSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.durationSpinner.setAdapter(durationSpinnerArrayAdapter);

        this.reminderSpinner = (Spinner) findViewById(R.id.spinner_propose_event_reminder);
        Integer[] reminders = { 0, 15, 30, 45, 60 };
        ArrayAdapter<Integer> remindSpinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, reminders);
        remindSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.reminderSpinner.setAdapter(remindSpinnerArrayAdapter);

        this.datePicker = (DatePicker) findViewById(R.id.datePicker_propose_event);
        this.emailEdit = (EditText) findViewById(R.id.edit_text_propose_event_add_member_email);
        this.addMemberButton =(Button) findViewById(R.id.button_propose_event_add_member);
        this.sendInvitationButton = (Button) findViewById(R.id.button_propose_event_send_invitation);
        this.cancelButton = (Button) findViewById(R.id.button_propose_event_cancel);
    }

    protected void initListener() {

        this.addMemberButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //evnetId
                String memberEmail = emailEdit.getText().toString().trim();

                final ProgressDialog progressDialog = ProgressDialog.show( ProposeEventActivity.this, "", "Adding member...", true );
                appAction.addEventMember(eventId, memberEmail, new ActionCallbackListener<Event>() {
                    @Override
                    public void onSuccess(Event data) {
                        progressDialog.cancel();
                        String name = data.getEventMemberDetail().get(0).getMember().getFirstName();
                        proposeEventAddMemberAdapter.personNames.add(name);
                        gridView.setAdapter(proposeEventAddMemberAdapter);
                    }

                    @Override
                    public void onFailure(String message) {
                        progressDialog.cancel();
                        Toast.makeText(context, "Fail: " + message, Toast.LENGTH_SHORT).show();
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

                final ProgressDialog progressDialog = ProgressDialog.show( ProposeEventActivity.this, "", "Cancelling...", true );

                getAppAction().cancelEvent(eventId, new ActionCallbackListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        progressDialog.cancel();

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
                        progressDialog.cancel();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    protected void sendInvitation() {

        final ProgressDialog progressDialog = ProgressDialog.show( ProposeEventActivity.this, "", "Sending...", true );
        String title = this.titleEdit.getText().toString();
        String location = this.locationEdit.getText().toString();
        // TODO: next two lines
        int durationInMin = 30;
        int reminderInMin = 30;
        String note = this.noteEdit.getText().toString();
        boolean hasReminder = (reminderInMin == 0) ? false : true;
        Date date = getDateFromDatePicker(datePicker);

        super.appAction.sendEventInvitation(eventId, title, location, durationInMin, hasReminder, reminderInMin, note, new ActionCallbackListener<Event>() {
            @Override
            public void onSuccess(Event data) {
                progressDialog.cancel();
                Toast.makeText(context, "Send invitation success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProposeEventActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String message) {
                progressDialog.cancel();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            }
        });
    }

    //sichao
    private void initGridView() {
        proposeEventAddMemberAdapter = new ProposeEventAddMemberAdapter(this, personNames);
        gridView = (GridView) findViewById(R.id.gridview_show_members);
        gridView.setAdapter(proposeEventAddMemberAdapter);
    }

    public Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    /*private void addMember() {

        int whichName = numNames % 3;
        numNames++;
        switch (whichName) {
            case 0:
                proposeEventAddMemberAdapter.personNames.add("Sichao");
                break;
            case 1:
                proposeEventAddMemberAdapter.personNames.add("Chuan");
                break;
            case 2:
                proposeEventAddMemberAdapter.personNames.add("Hairong");
                break;
        }
    }*/

}
