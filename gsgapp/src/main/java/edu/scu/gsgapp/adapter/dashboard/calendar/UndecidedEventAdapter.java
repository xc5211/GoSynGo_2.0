package edu.scu.gsgapp.adapter.dashboard.calendar;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.scu.core.ActionCallbackListener;
import edu.scu.core.AppAction;
import edu.scu.gsgapp.R;
import edu.scu.model.EventUndecided;

/**
 * Created by chuanxu on 6/3/16.
 */
public class UndecidedEventAdapter extends ArrayAdapter<EventUndecided> {

    private AppAction appAction;
    private List<EventUndecided> undecidedEventList;

    public UndecidedEventAdapter(Context context, int resource, AppAction appAction, List<EventUndecided> undecidedEventList) {
        super(context, resource, undecidedEventList);
        this.appAction = appAction;
        this.undecidedEventList = undecidedEventList;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.undecided_event_row, null);

        final EventUndecided undecidedEvent = undecidedEventList.get(position);

        TextView titleTextView = (TextView) row.findViewById(R.id.undecided_event_row_event_title);
        titleTextView.setText(undecidedEvent.title);

        TextView leaderNameTextView = (TextView) row.findViewById(R.id.undecided_event_row_leader_name);
        leaderNameTextView.setText(undecidedEvent.leaderName);

        ImageButton declineButton = (ImageButton) row.findViewById(R.id.button_decline_new);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appAction.declineEvent(undecidedEvent.eventId, undecidedEvent.leaderId, new ActionCallbackListener<Boolean>() {

                    final ProgressDialog progressDialog = ProgressDialog.show( parent.getContext(), "", "Declining...", true );
                    @Override
                    public void onSuccess(Boolean data) {
                        progressDialog.cancel();
                        Toast.makeText(getContext(), "Decline event success", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {
                        progressDialog.cancel();
                        Toast.makeText(getContext(), "Decline event fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ImageButton acceptButton = (ImageButton) row.findViewById(R.id.button_accept_new);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appAction.acceptEvent(undecidedEvent.eventId, undecidedEvent.leaderId, new ActionCallbackListener<Boolean>() {

                    final ProgressDialog progressDialog = ProgressDialog.show( parent.getContext(), "", "Accepting...", true );
                    @Override
                    public void onSuccess(Boolean data) {
                        progressDialog.cancel();
                        Toast.makeText(getContext(), "Accept event success", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {
                        progressDialog.cancel();
                        Toast.makeText(getContext(), "Accept event fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return row;
    }

}
